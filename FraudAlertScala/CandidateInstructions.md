#Candidate Instructions:

Acme Travel company is upgrading its central fraud detection system and you are required to implement new fraud detection rules for credit card payments. The system will serve all the transaction done by Acme Travel for a specific area (i.e. for UK). Because the company is processing a very large number of transactions your code should perform well under load and be thread-safe.

###Provided:
* Empty `Transaction` object.
* `Alert` object referencing the original transaction and containing additional information about the alert, such as alert message.
* `FraudAlertService` interface with single method `alert()` that accepts the `Alert` object. Use this service to submit fraud alert notifications.
* `FraudDetectorService` interface with single method `process()` that your fraud detector must implement. This method might be called from multiple threads at the same time.

###Requirements:
* Describe the architecture of the system in terms of main classes and their responsibilities.
* Complete implementation of the `Transaction` object to contain the credit card number, timestamp and amount.
* Implement `FraudDetectorService` to contain following fraud-detection rules:

  * If total amount of transactions on a credit card exceed £10 000 within moving window of 30 minutes you must submit an alert to `FraudAlertService` with message "Amount exceeded threshold.".
  * If total number of transactions on a credit card exceed 3 within moving window of 30 seconds you must submit an alert to `FraudAlertService` with message "Number of transactions exceeded threshold."

You are not required to validate credit card numbers.

You can create new classes or interfaces but you cannot alter the interface of the one present.

You are required to complete the implementation of the service `FraudDetectorService`, but you can omit the implementation of other components.

###Example Behaviour:

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

Expected generated Alert:

    10:00:05.000 4111111111111111 Amount exceeded threshold.
    10:00:14.000 4012888888881881 Number of transactions exceeded threshold.
    10:05:55.000 4111111111111111 Amount exceeded threshold.
