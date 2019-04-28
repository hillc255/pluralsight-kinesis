package com.pluralsight.kinesis.module2;

import com.amazonaws.services.kinesis.producer.*;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class TwitterProduceMain {
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

        return new TwitterStreamFactory(cb.build()).getInstance();
    }

    private static RawStreamListener createListener(){
        //Kinesis producer created
        KinesisProducer kinesisProducer = createKinesisProducer();
        return new TweetsStatusListener(kinesisProducer);
    }

    //Kinesis configuration
    private static KinesisProducer createKinesisProducer(){
        KinesisProducerConfiguration config = new KinesisProducerConfiguration()
                .setRequestTimeout(6000)
                .setRecordMaxBufferedTime(15000)
                .setRegion("us-east-1") ;
        return new KinesisProducer(config);
    }

    static class TweetsStatusListener implements RawStreamListener {

        //Kinesis producer - TweetStatusListener is listening for it
        private KinesisProducer kinesisProducer;
        private int count;

        public TweetsStatusListener(KinesisProducer kinesisProducer){
            this.kinesisProducer = kinesisProducer;
        }

        public void onMessage(String tweetJson){
            if (count++ % 5 !=0) return;
            try{
                Status status = TwitterObjectFactory.createStatus(tweetJson);
                //if new tweet
                if (status.getUser() != null) {
                    byte[] tweetBytes = tweetJson.getBytes(StandardCharsets.UTF_8);
                    String partitionKey = status.getLang();
                    ListenableFuture<UserRecordResult> f = kinesisProducer.addUserRecord(
                            "tweets-stream",
                            partitionKey,
                            ByteBuffer.wrap(tweetBytes)
                    );

                    Futures.addCallback(f, new FutureCallback<UserRecordResult>() {
                        @Override
                        public void onSuccess(UserRecordResult userRecordResult) {
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (throwable instanceof UserRecordFailedException) {
                                UserRecordFailedException e =
                                        (UserRecordFailedException) throwable;
                                UserRecordResult result = e.getResult();

                                Attempt last = Iterables.getLast(result.getAttempts());
                                System.err.println(String.format(
                                        "Put failed - %s",
                                        last.getErrorMessage()));
                            }

                        }

                    });
                }
            } catch (TwitterException e){
                e.printStackTrace();
            }
        }
        public void onException(Exception ex){
            ex.printStackTrace(); }
    }
}