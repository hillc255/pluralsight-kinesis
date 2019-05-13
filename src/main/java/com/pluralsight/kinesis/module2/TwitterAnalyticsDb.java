package com.pluralsight.kinesis.module2;

import java.util.HashMap;

public class TwitterAnalyticsDb {
    private HashMap<LangAndTime, Integer> db = new HashMap<>();

    /**
     * Save data into a fake "database"
     *
     * @param langAndTime language and time pair (e.g. "en" at "09:45")
     * @param count number of tweets
     * @retun true if new of idempotent write false otherwise
     */
    public boolean saveStatistics(LangAndTime langAndTime, int count){
        if(!db.containsKey(langAndTime)){
            db.put(langAndTime, count);
            return true;
        }
        return db.get(langAndTime) == count;
    }
}
