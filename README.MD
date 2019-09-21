Simple Accounts REST App
=========================

Java RESTful API for money transfers between accounts

Contents
--------

The mapping of the URI path space is presented in the following table:

URI path             | Resource class      | HTTP methods | Notes
-------------------- | ------------------- | ------------ | --------------------------------------------------------
**_/ping_**          | PingEndpoint        |  GET         |  Returns `pong`

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