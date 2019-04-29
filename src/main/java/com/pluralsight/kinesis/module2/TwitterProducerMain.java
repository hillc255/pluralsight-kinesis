package com.pluralsight.kinesis.module2;


import twitter4j.*;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;


public class TwitterProducerMain {
    public static void main(String[] args) {
        TwitterStream twitterStream = createTwitterStream();
        twitterStream.addListener(createListener());
        twitterStream.sample();
    }

    //read and write permissions
    private static TwitterStream createTwitterStream(){

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("a")
                .setOAuthConsumerSecret("b")
                .setOAuthAccessToken("c")
                .setOAuthAccessTokenSecret("d");
             //   .setJSONStoreEnabled(true);

        return new TwitterStreamFactory(cb.build()).getInstance();
    }


    private static RawStreamListener createListener(){
        return new TweetsStatusListener();
    }

    private static class TweetsStatusListener implements RawStreamListener {
        public void onMessage(String tweetJson){
            try {
                Status status = TwitterObjectFactory.createStatus(tweetJson);
                //if new tweet
                if (status.getUser() != null) {
                    System.out.println(tweetJson);
                    System.out.println(status.getUser());
                    System.out.println(status.getText());
                    }
                } catch (TwitterException e) {
                //Hide stacktrace
               // e.printStackTrace();
            }

        }
        public void onException(Exception e){
            e.printStackTrace();
        }
    }
}