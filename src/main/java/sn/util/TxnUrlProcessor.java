package sn.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Stores a regex pattern and processes the input data
 * User: sumanthn
 * Date: 26/7/13
 */
public class TxnUrlProcessor {

    //this data should come from DB or other repo
    private static Map<String, Integer> txnNamesToIdMap;
    private static List<String> txnNames;

    //regex for parsing URLs
//    private static Map<Pattern,String> patternsStrings;
     static {
        init();
    }

    static void init() {

        //used for contains matching
        txnNames = ImmutableList.
                of("SearchItemsByRegion", "ViewItem", "SearchItemsByCategory", "ViewUserInfo", "ViewBidHistory", "StoreBid", "StoreComment", "AboutMe");

        ImmutableMap.Builder<String, Integer> mapBuilder =
                new ImmutableMap.Builder<String, Integer>().
                        put("SearchItemsByRegion", 1).
                        put("ViewItem", 2).
                        put("SearchItemsByCategory", 3).
                        put("ViewUserInfo", 4).
                        put("ViewBidHistory", 5).put("StoreBid", 6).put("StoreComment", 7).put("AboutMe", 8);
        txnNamesToIdMap = mapBuilder.build();

    }


    /**
     * does matching for the passed URL to find the TransactionName and Id
     *
     * @return Id  for valid transaction
     *         -1 for non-matching url
     */
    public static int getTxnId(final String url) {

        //here no pattern matching or url formation
        //just contains
        //TODO: need to check which is faster, contains or regex parsing
         for (String txnName : txnNames) {
           if (url.contains(txnName)) {
                return txnNamesToIdMap.get(txnName);
            }
        }
        //invalid url
        return -1;
       }

}
