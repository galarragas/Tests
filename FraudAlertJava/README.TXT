Expedia Coding Test 1

Acme Travel company is upgrading its fraud detection system and you are 
required to implement new fraud detection rules for credit card payments. 
Because the company is processing a very large number of transactions 
your code should perform well under load and be thread-safe.

Following is provided for you:
   * Empty Transaction object
   * Alert object referencing the original transaction and containing 
     additional information about the alert, such as alert message
   * FraudAlertService interface with single method alert() that accepts 
     Alert object - use this service to submit fraud alert notifications
   * FraudDetectorService interface with single method process() that your 
     fraud detector must implement. This method might be called from 
     multiple threads at the same time.

You are required to:
   * Complete implementation of the Transaction object to contain credit 
     card number, timestamp and amount
   * Implement FraudAlertService interface where implementation simply 
     prints current timestamp, credit card number and message from the 
     Alert object separated by space
   * Implement FraudDetectorService to contain following fraud-detection rules
      - If total amount of transactions on a credit card exceed 10000 within 
        moving window of 30 minutes you must submit an alert to 
        FraudAlertService with message "Amount exceeded threshold."
      - If total number of transactions on a credit card exceed 3 within 
        moving window of 30 seconds you must submit an alert to 
        FraudAlertService with message 
        "Number of transactions exceeded threshold."

You are not allowed to use any 3rd party libraries except for testing libraries. 

You are not required to validate credit card numbers.

At a minimum you must demonstrate that your code prints following lines for 
this set of input.

Input data:

Timestamp    CC#                Amount
10:00:01.000 4111111111111111   150.00
10:00:05.000 4111111111111111 10000.00 
10:00:10.000 4012888888881881   150.00
10:00:11.000 4012888888881881   150.00
10:00:12.000 4012888888881881   150.00
10:00:14.000 4012888888881881   150.00 
10:05:55.000 4111111111111111   100.00 
11:00:14.000 4012888888881881   150.00 

Expected console output:

10:00:05.000 4111111111111111 Amount exceeded threshold.
10:00:14.000 4012888888881881 Number of transactions exceeded threshold.
10:05:55.000 4111111111111111 Amount exceeded threshold.