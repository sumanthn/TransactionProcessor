package sn.type;

import java.io.Serializable;

/**
 * User: sumanthn
 * Date: 8/8/13
 */
public class TxnRawData implements Serializable {

    //txn id
    private int txnId;
    //client ip from where it was initiated
    private long clientIp;

    //response time for transaction in ms
    private int responseTime;

    //user id or client id
    private int userId;
    //HTTP response code
    private int responseCode;

    //start time stamp
    private long startTs;

    private String url;
    //additional
    /*

    //for custom attributes
    private Map<String,String> customAttrs = new HashMap<String,String>();
     */

    public TxnRawData(){

    }

    public TxnRawData(int txnId) {
        this.txnId = txnId;
    }

    public int getTxnId() {
        return txnId;
    }

    public void setTxnId(int txnId) {
        this.txnId = txnId;
    }

    public long getClientIp() {
        return clientIp;
    }

    public void setClientIp(long clientIp) {
        this.clientIp = clientIp;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
