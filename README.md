# Score Card

Simple HTTP and JMS implemenation of the Score Card pattern. This project is made up of several modules:

## Core

URL: `http://localhost:8080`

This is the Core Score Card application. This application tracks Services, Actions and Transactions. Individual executions are tracked as Sore Cards. The Core module exposes several web APIs these are described at `http://localhost:8080/swagger-ui.html`.

## API

The API module wraps the Core Web APIs in a Java API for easy consumption.

## Monitor

URL: `http://localhost:8080/monitor`

The Monitor module allows the user to monitor the status of score card executions.

## Portal

URL: `http://localhost:8080/portal`

The Portal module allows the user to configure services, actions and transactions.

## Examples

### Simple

The simple example starts 3 actions on a single service combined into a transacton with dependencies. It may be launched by sending a GET request to `http://localhost:8080/app/example`.

### Bank

This example is a JMS based example of a bank transfer. It includes processing a bank trasfer from oue account to antoher. I also includes handling exception cases such as the NSF, Debit Account Not Exsting and Credit Account not existing. When one of these exceptions occurs it utilzes the Score Card model to detect the failure and correct it. This example may be launced by sending a GET reqeust to `http://localhost:8080/app/bank/transfer?from_account_id=17&to_account_id=18&amount=1.01`.