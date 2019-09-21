Simple Accounts REST App
=========================

Java RESTful API for money transfers between accounts

Contents
--------

The mapping of the URI path space is presented in the following table:

URI path                                | Resource class      | HTTP methods | Notes
--------------------------------------- | ------------------- | ------------ | -------------------------
**_/ping_**                             | PingEndpoint        |  GET         |  Application HealthCheck, returns `pong`
**_/accounts/all_**                     | AccountEndpoint     |  GET         |  Returns all `Accounts`
**_/accounts/{id}_**                    | AccountEndpoint     |  GET         |  Returns `Account` by `{id}`
**_/accounts/create_**                  | AccountEndpoint     |  PUT         |  Create `Account`
**_/accounts/delete/{id}_**             | AccountEndpoint     |  DELETE      |  Delete `Account`
**_/accounts/{id}/withdraw/{amount}_**  | AccountEndpoint     |  POST        |  Withdraw money from `Account`
**_/accounts/transfer_**                | AccountEndpoint     |  POST        |  Transfer money from one `Account` to another

Technologies
------------
- JAX-RS API
- H2 in memory database
- Grizzly Web Server

Running the Example
-------------------

Run the example as follows:

>     mvn clean package -P run

This deploys the example using [Grizzly](http://grizzly.java.net/) container.

App HealthCheck:
-   <http://localhost:8080/acc-app/ping>