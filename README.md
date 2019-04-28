# pluralsight-kinesis
Java app from Pluralsight course to get streaming tweets used with AWS Kinesis

# Notes

Steps to create app on Twitter
1. Go to Twitter Development Platform: https://developer.twitter.com/content/developer-twitter/en.html
2. Log into the website
3. Click on Apps > Create an App
4. Complete the following:
     app name
     website url: http://www.example.com
     enable sign-in with twitter
     callback url: http://www.example.com
     app usage:  brief description
     <go to tab - permissions> make sure access permissions: Read and Wrie
     <go to tab - keys and tokens> generate 4 keys: Consumer API keys (2), Access token & access token secret (2)
5. Add the 2 keys to this app  
          
*Currently, when this app is run some tweets stream but there are intermittent errors:
JSONObject["text"] not found.
     
     
