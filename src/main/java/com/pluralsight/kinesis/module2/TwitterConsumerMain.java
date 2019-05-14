package com.pluralsight.kinesis.module2;


import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

public class TwitterConsumerMain {
    public static void main(String... args){
        KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(
                "tweets-processor",
                //initially "tweets-stream"
                "tweets-stream-chill",
                new DefaultAWSCredentialsProviderChain(),
                "worker-1"
        );
        config.withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);
        config.withIdleTimeBetweenReadsInMillis(200);

        IRecordProcessorFactory factory = new TweetsProcessorFactory();

        Worker worker = new Worker.Builder()
                .config(config)
                .recordProcessorFactory(factory)
                .build();
        worker.run();
    } //public static void
} //class
