package sn.bolt;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import sn.type.TxnRawData;
import sn.util.TupleProcessorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import static sn.util.NamesUtil.*;

/**
 * Aggregates and Batches the incoming data
 * Aggregates the txn data for summary for a minute time frame
 * Batches the TxnRawData for better persist
 * User: sumanthn
 * Date: 8/8/13
 */
public class TxnAggregateBolt extends GenericBolt  {
    private Map<Integer, TxnDataCache> responseTimeData = new HashMap<Integer, TxnDataCache>();

    private int secondTicker = 0;


    private Map<Integer,List<TxnRawData>> tuplesByIdMap = new HashMap<Integer, List<TxnRawData>>();

    private ReentrantLock lock = new ReentrantLock();


    @Override
    public void execute(Tuple tuple) {
        if (TupleProcessorUtil.isTickTuple(tuple)) {
            //process summary data and batch data

            try{
                lock.lock();

                //deque all tuples
                //  localCollector.emit(RAW_DATA_STREAM_ID,allTuples);


                //emit the tuples right away
                if (secondTicker!=0){
                    //access a global config to get the frequency of tick
                    //Can tick tuple have meta data to indicate the kind of tick
                    for(Integer txnId : tuplesByIdMap.keySet()){

                        List<TxnRawData> rawDataBag =  tuplesByIdMap.get(txnId);

                        //pass it on to persist
                        if (rawDataBag!=null && (!rawDataBag.isEmpty())){
                        localCollector.emit(RAW_DATA_STREAM_ID, new Values(txnId, rawDataBag));
                         //for next iteration
                        tuplesByIdMap.put(txnId, new ArrayList<TxnRawData>());
                        }

                    }


                    if (secondTicker%6==0){
                        //at 60
                        //publish the results
                        for(Integer txnId : responseTimeData.keySet()){
                            TxnDataCache cachedData = responseTimeData.get(txnId);

                            localCollector.emit(SUMMARY_STREAM_ID,new Values(txnId,cachedData.getResponseTimeArr().getMean(),
                                    cachedData.getResponseTimeArr().getPercentile(50.0d),
                                    cachedData.getResponseTimeArr().getPercentile(90.0d),
                                    cachedData.getResponseTimeArr().getMin(),
                                    cachedData.getResponseTimeArr().getMax(),
                                    cachedData.getGoodCount(),
                                    cachedData.getFailedCount(),
                                    (cachedData.getGoodCount()+cachedData.getFailedCount())

                            ));
                            cachedData.reset();
                        }

                    }

                }

                secondTicker++;

            }finally {
                lock.unlock();
            }

        } else {
            //non timer based
            try{
                lock.lock();




                //allTuples.add(tuple);


                int txnId = tuple.getInteger(0);
                TxnRawData rawData = (TxnRawData) tuple.getValue(1);
                if (tuplesByIdMap.containsKey(txnId)){
                    tuplesByIdMap.get(txnId).add((TxnRawData) tuple.getValue(1));
                }else{
                    List<TxnRawData> rawDataBatch = new ArrayList<TxnRawData>();
                    rawDataBatch.add(rawData);
                    tuplesByIdMap.put(txnId,rawDataBatch);

                }

                if (responseTimeData.containsKey(txnId)) {
                    TxnDataCache dataCache = responseTimeData.get(txnId);
                    dataCache.updateResponseCodeCount(rawData.getResponseCode());
                    dataCache.updateResponseTime(rawData.getResponseTime());


                } else {
                    TxnDataCache dataCache = new TxnDataCache();
                    dataCache.updateResponseCodeCount(rawData.getResponseCode());
                    dataCache.updateResponseTime(rawData.getResponseTime());
                    responseTimeData.put(txnId, dataCache);
                }

            }finally {
                lock.unlock();
            }

        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //produce 2 streams
        //stream one 'TxnSummaryStream' an aggregate
        //
        declarer.declareStream(SUMMARY_STREAM_ID, new Fields(TXN_ID_FLD,
                MEAN_RT_FLD,Median_RT_FLD,
                PECENT90_RT_FLD,MIN_RT_FLD,MAX_RT_FLD,
                TXN_SUCCESS_FLD,TXN_FAILURE_FLD,TXN_TOTAL));

        //raw data stream
  /*    declarer.declareStream(RAW_DATA_STREAM_ID, new Fields(
              NamesUtil.TXN_ID_FLD,
               NamesUtil.TXN_RAW_DATA_BATCH_FLD));*/
        declarer.declareStream(RAW_DATA_STREAM_ID, new Fields(TXN_ID_FLD,TXN_RAW_DATA_BATCH_FLD));
    }

    //poor mans agg data cache
    private static class TxnDataCache {
        //this should be an arrary  but since it again processing it stored as stats
        private DescriptiveStatistics responseTimeArr;
        private int goodCount;
        private int failedCount;

        private TxnDataCache() {
            responseTimeArr = new DescriptiveStatistics();
        }

        void updateResponseTime(final float responseTime) {
            responseTimeArr.addValue(responseTime);
        }

        void updateResponseCodeCount(final int code) {
            switch (code) {
                case 200:
                case 201:
                    goodCount++;
                    break;
                case 400:
                case 401:


                case 500:
                    failedCount++;

            }
        }

        void reset() {
            responseTimeArr.clear();
            goodCount = 0;
            failedCount = 0;
        }

        public DescriptiveStatistics getResponseTimeArr() {
            return responseTimeArr;
        }

        public int getGoodCount() {
            return goodCount;
        }

        public int getFailedCount() {
            return failedCount;
        }
    }
}
