package sn;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import sn.bolt.TxnAggregateBolt;
import sn.bolt.TxnEnrichBolt;
import sn.bolt.TxnRawDataPersistBolt;
import sn.bolt.TxnSummaryBolt;
import sn.spout.TxnSourceSpout;
import sn.util.NamesUtil;

import static sn.util.NamesUtil.SUMMARY_STREAM_ID;

/**
 * User: sumanthn
 * Date: 26/7/13
 */
public class TxnAggregateTopology {

     public void kickStart(){
        TopologyBuilder builder =  new TopologyBuilder();

        LocalCluster cluster = new LocalCluster();
        Config conf = new Config();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,10);

        // conf.setDebug(true);
        conf.setNumWorkers(20);
        builder.setSpout(NamesUtil.EMITTER_SPOUT_ID, new TxnSourceSpout());

        builder.setBolt(NamesUtil.ENRICH_BOLT_ID, new TxnEnrichBolt(),2).shuffleGrouping(NamesUtil.EMITTER_SPOUT_ID);

        builder.setBolt(NamesUtil.AGGREGATE_BOLT_ID, new TxnAggregateBolt(),2).
                fieldsGrouping(NamesUtil.ENRICH_BOLT_ID, new Fields(NamesUtil.TXN_ID_FLD));

        builder.setBolt(NamesUtil.RAW_DATA_PERSISTER_BOLT_ID, new TxnRawDataPersistBolt(),2).
                 shuffleGrouping(NamesUtil.AGGREGATE_BOLT_ID, NamesUtil.RAW_DATA_STREAM_ID);

         builder.setBolt(NamesUtil.DATA_SUMMARY_PERSISTER_BOLT_ID,new TxnSummaryBolt(),2).
         shuffleGrouping(NamesUtil.AGGREGATE_BOLT_ID,SUMMARY_STREAM_ID) ;



      /*  builder.setBolt("TransactionAgg",new TransactionCounterBolt(),2).
                fieldsGrouping("TransactionEmitter", new Fields("Id"));
        builder.setBolt("TransactionCountSummary",new TransactionSummaryBolt()).shuffleGrouping("TransactionAgg","SummationStream") ;
*/
        cluster.submitTopology("TransactionStreamProcessor", conf, builder.createTopology());

    }


    public static void main(String [] args){
        TxnAggregateTopology aggTopology = new TxnAggregateTopology();
        aggTopology.kickStart();
    }
}
