package sn.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichBolt;

import java.util.Map;

/**
 * User: sumanthn
 * Date: 26/7/13
 */
public abstract  class GenericBolt extends BaseRichBolt{
    protected   OutputCollector localCollector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
      this.localCollector = outputCollector;
    }


}
