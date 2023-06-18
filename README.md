# pocIDFC
A Proof-Of-Concept microservice to log and commit all customer side events - such as account creation and transaction journeys using Apache Kafka and SpringBoot

All atomic events are logged using Kafka topics `account-events.{accountNum}` (dynamically generated topic) and `transaction-events` and every committed event is persisted in a local SQL instance.


AntLogging - Account 'N Transaction Logging


## Models
- [x] Accounts
- [x] Transactions
- [x] Account Stream Payload
- [x] Transaction Stream Payload

## Event Types
- [x] Account CREATE
- [x] Account DEPOSIT
- [x] Account INSUFFICIENT_BALANCE
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
![WhatsApp Image 2023-06-18 at 11 57 32 PM](https://github.com/gauthamys/pocIDFC/assets/66833388/f8cec5f5-7493-4b9f-8b97-1155394a3f73)

