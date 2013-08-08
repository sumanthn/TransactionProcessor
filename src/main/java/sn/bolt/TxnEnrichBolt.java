package sn.bolt;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import sn.type.TxnRawData;
import sn.util.NamesUtil;
import sn.util.TupleProcessorUtil;
import sn.util.TxnUrlProcessor;

/**
 * Enriches the incoming tuples and make an object out of it
 * User: sumanthn
 * Date: 8/8/13
 */
public class TxnEnrichBolt extends GenericBolt  {

    @Override
    public void execute(Tuple tuple) {
        if (!TupleProcessorUtil.isTickTuple(tuple)) {

            //does a simple lookup and figures out the transaction id
            //assembles the object and pushes to next stage
            final TxnRawData rawData = assembleTxn(tuple);
            if (rawData!=null){
                localCollector.emit(new Values(rawData.getTxnId(),rawData));
            } else{
                System.out.println("Failed processing for " + tuple.getString(0));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(NamesUtil.TXN_ID_FLD,
              NamesUtil.TXN_RAW_DATA));
    }

    private TxnRawData assembleTxn(final Tuple tuple){
        int txnId = TxnUrlProcessor.getTxnId(tuple.getString(0));
        TxnRawData rawData = new TxnRawData(txnId);
        rawData.setClientIp(tuple.getLongByField(NamesUtil.CLIENT_IP_FLD));
        rawData.setResponseTime(tuple.getIntegerByField(NamesUtil.RESPONSE_TIME_FLD));
        rawData.setResponseCode(tuple.getIntegerByField(NamesUtil.RESPONSE_CODE_FLD));
        //TODO: provide hook for lookup user id from username
        //rawData.setUserId(tuple.getIntegerByField(NamesUtil.USRID_FLD));
        //pushing the current ts, should also come from generator
         rawData.setStartTs(tuple.getLongByField(NamesUtil.TXN_START_TS));
         rawData.setUrl(tuple.getStringByField(NamesUtil.URL_FLD));

        //set additional as key value maps


        return rawData;
    }
}
