package sn.util;

/**
 * User: sumanthn
 * Date: 26/7/13
 */
public class NamesUtil {

    //the emitter or source Spot
    public static final String EMITTER_SPOUT_ID="TransactionEmitter";

    //Stage 1 processor Bolt
    public static final String ENRICH_BOLT_ID="TransactionEnricher";

    //Stage 2 Aggregate Bolt
    public static final String AGGREGATE_BOLT_ID="TransactionAggregator";

    //Stream Emitted from Aggregate Bolt
    public static final String SUMMARY_STREAM_ID="TransactionSummaryStream";

    //RawDataStream Emitted from Aggregate Bolt
    public static final String RAW_DATA_STREAM_ID="TransactionRawDataStream";

    //persister bolts
    public static final String RAW_DATA_PERSISTER_BOLT_ID="TransactionRawDataPersist";
    public static final String DATA_SUMMARY_PERSISTER_BOLT_ID="TransactionSummaryPersist";


    //field names
    public static final String RESPONSE_TIME_FLD="RTime";
    public static final String TXN_ID_FLD ="TxnId";
    public static final String RESPONSE_CODE_FLD ="RCode";
    public static final String CLIENT_IP_FLD ="ClientIP";
    public static final String URL_FLD="Url";

    public static final String USRID_FLD ="UserId";
    public static final String TXN_START_TS="StartTs";

    public static final String TXN_RAW_DATA_BATCH_FLD="DataChunk";
    public static final String TXN_RAW_DATA="RawData";

    public static final String MEAN_RT_FLD="MeanRT";
    public static final String Median_RT_FLD ="MedianRT";
    public static final String PECENT90_RT_FLD ="90P_RT";
    public static final String MIN_RT_FLD="MinRT";
    public static final String MAX_RT_FLD="MaxRT";
    public static final String TXN_SUCCESS_FLD="Success";
    public static final String TXN_FAILURE_FLD="Failed";
    public static final String TXN_TOTAL ="Total";





}
