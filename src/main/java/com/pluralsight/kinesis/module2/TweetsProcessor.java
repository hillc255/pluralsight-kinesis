package com.pluralsight.kinesis.module2;


import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class TweetsProcessor implements IRecordProcessor {
    @Override
    public void initialize(InitializationInput initializationInput){
    }
    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput){
        for (Record record : processRecordsInput.getRecords()){
            System.out.println(getStatus(record));
            checkpoint(processRecordsInput.getCheckpointer());
        }
    }

    public void checkpoint(IRecordProcessorCheckpointer checkpointer){
        try {
            checkpointer.checkpoint();
        }catch (InvalidStateException e) {
            // Table exits
            e.printStackTrace();
        }catch (ShutdownException e){
            //Two processors are processing the same shard
            e.printStackTrace();
        }
    }

    private Status getStatus(Record record) {
        ByteBuffer data = record.getData();
        String tweetJson = new String(data.array(), StandardCharsets.UTF_8);
        return parseTweet(tweetJson);
    }

    private Status parseTweet(String tweetJson) {
        try{
            return TwitterObjectFactory.createStatus(tweetJson);
        } catch (TwitterException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput){
        ShutdownReason reason = shutdownInput.getShutdownReason();

        switch(reason){
            case TERMINATE:
            case REQUESTED:
                checkpoint(shutdownInput.getCheckpointer());
                break;
            case ZOMBIE:
                System.out.println("Zombie shard. No checkpoint");
                break;
        }
    }
}
