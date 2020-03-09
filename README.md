# Transaction Processor Service

Transaction Processor Service takes batch of transactions and apply them to the accounts, Compute and store
the account balances. It exposes to API's "/transactions" and "/accounts"


# Getting Started

### Built With

* Java Spring Boot that has inbuilt tomcat server.

##  Build and Start the server using docker

It has a DockerFile that takes care of running the service in a container. Execute the following commands in the home directory of the project to get the service up and running.

```
docker build -t tps .
```

```
docker run -p 8080:8080 tps
```

## Build and Start the server using gradle

Starting the server manually using gradle if there are any issues running using docker. Execute the following command in the 
home directory of the project.

```
./gradlew build && java -jar build/libs/transactionprocessor-0.0.1-SNAPSHOT.jar
```


## Tests

All the major components has good unit test coverage and the tests are in "src/test/java/" and the results can be found at "build/test-results/test/"

Cammand to run test cases

```
 ./gradlew test 
```
Summary of Tests

* Create new Account 
* Deposit amount to account, Success and Failure in case of Freeze
* Deposit amount to account, Success and Failure in case of Freeze
* Transfer from one account to another Success
* Transfer from one account to another Failure and balance validations
* Validity of Cmd for the transaction
* Validity of transaction attributes, check for required attributes.
* Freeze Account
* Thaw Account

## High Level Components

#### Transactions Controller

Transactions Controller exposes REST API to take the batch of transactions and does basic validations of the transactions, 
understand the transaction format and convert to Java Objects for further processing.

#### Transactions Service

Transaction Service processes the batch of transactions talking to Accounts Service, handles any kind of rollback if required in the transaction execution and return the failed transactions. Atomicity is guaranteed by this service.

#### Accounts Service

Accounts Service manages all the accounts and execute individual operations like Deposit, Withdraw, Freeze, Thaw. It also create new accounts based on the input.

## Deployment and Optimizations

At a high level Transactions Service and Accounts Service can be separated and deployed independently. Below optimizations can improve 
the performance

* Adding a Job queue in Transactions Service will improve performance to process the batch of transactions. It off loads the processing to worker threads.
* By deploying Accounts Service in multiple nodes and use "Account Locator" and shard the account instances across the Accounts services can scale.



