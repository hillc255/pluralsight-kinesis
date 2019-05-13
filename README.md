# pluralsight-kinesis
Java app from Pluralsight course to get streaming tweets used with AWS Kinesis

# Notes

### Part I: Steps to create app on Twitter
1. Go to Twitter Development Platform: https://developer.twitter.com/content/developer-twitter/en.html
2. Log into the website
3. Click on Apps > Create an App
4. Complete the following THEN add the 4 keys to the app:
* app name
* website url: http://www.example.com
* enable sign-in with twitter
* callback url: http://www.example.com
* app usage:  brief description
* go to tab - permissions: select: Read and Write
* go to tab - keys and tokens: generate 4 keys: Consumer API keys (2), Access token & access token secret (2)  

### Part II: Run the Java app to stream tweet          
*Currently, when this app is run some tweets stream but there are intermittent errors:
JSONObject["text"] not found.

### Part III: Kinesis Connect

I. AWS Set-up - permissions
1. AWS - create account
2. IAM (identity and access management) > Users > User name = kinesis-user
3. AWS Access type = Programmatic access [next permissions]
4. Attach exisiting policies directly > search for Kinesis and add
* AmazonKinesisAnalyticsFullAccess
* AmazonKinesisFirehoseFullAccess
* AmazonKinesisFullAccess
* AWSLambbdaKinesisExecutionRole
* CloudWatchFullAccess
* AmazonDynamoDBFullAccess 

II. Create Kinesis   
1. Kinesis > Create Kinesis stresm:  tweet-stream
2. Number of shards 1
3. Run Java Kinesis-connect branch   
     
     
* See Tweets consumed after adding below - run TwitterProducerMain then run TwitterConsumerMain
* TweetsProcessor     
* TweetsProcessorFactory
* TwitterConsumerMain
* TwitterProducerMain
     
