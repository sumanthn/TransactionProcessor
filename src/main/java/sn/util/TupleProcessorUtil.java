package sn.util;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

/**
 * User: sumanthn
 * Date: 26/7/13
 */
public class TupleProcessorUtil {
    public static boolean isTickTuple(final Tuple tuple) {
        return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
                && tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
    }
}
