package sn.bolt;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import sn.util.NamesUtil;
import sn.util.TupleProcessorUtil;

/**
 * User: sumanthn
 * Date: 8/8/13
 */
public class TxnSummaryBolt extends GenericBolt {

    @Override
    public void execute(Tuple tuple) {
        if (!TupleProcessorUtil.isTickTuple(tuple)){
            System.out.println("rcvd stats for " +  tuple.getInteger(0) +
                    " Median " +  tuple.getDoubleByField(NamesUtil.Median_RT_FLD));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
