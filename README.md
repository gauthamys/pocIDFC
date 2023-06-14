# pocIDFC
A Proof-Of-Concept microservice to log and commit all customer side events - such as account creation and transaction journeys using Apache Kafka and SpringBoot

All atomic events are logged using Kafka topics `account-events` and `transaction-events` and every committed event is persisted in a local SQL instance.


## Models
- [x] Accounts
- [x] Transactions
- [x] Account Stream Payload
- [x] Transaction Stream Payload

## Event Types
- [x] Account CREATE
- [x] Transaction SUCCESS
- [x] Transaction INSUFFICIENT_BALANCE
- [x] Transaction ACCOUNT_NON_EXISTENT

## Services
- [x] Kafka Producer 
- [x] Transactions & Accounts

## Endpoints
- [x] /accounts/post
- [x] /accounts/getAll


- [x] /transactions/post
- [x] /transactions/getAll

## Data Flow
todo
