package sn.bolt;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import sn.type.TxnRawData;
import sn.util.TupleProcessorUtil;

import java.util.List;

/**
 * User: sumanthn
 * Date: 26/7/13
 */
public class TxnRawDataPersistBolt extends GenericBolt {
    @Override
    public void execute(Tuple tuple) {
        if (!TupleProcessorUtil.isTickTuple(tuple)){
            //Gson gson = new Gson();
          //  System.out.println(gson.toJson(tuple.getFields()));
            List<TxnRawData> rawDataBag = (List<TxnRawData>) tuple.getValue(1);
            System.out.println("Received Raw data for " +  tuple.getInteger(0) + " " + rawDataBag.size());
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     *
     */
}
