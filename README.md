# PubSub System

This was done as a part of Distributed Systems course at The University of Minnesota. 

In this project, we have implemented a simple publish subscribe system.
We have used 2 forms of communication, UDP and Java RMI to achieve this. 
The components of this system are clients, group servers and a registry server. 
##
### Client
* The client-side functionality is implemented in the Client package, with two files, Client.java and PublishedClient.java.
* **Client.java** handles the RPC connection to the server, where it joins a server, subscribes, unsubscribes and/or publishes an article, and leaves a server. 
* This also handles the connection to the Registry Server to get the list of active servers for the client to connect. 
* **PublishedClient.java** runs on a separate thread that handles the UDP connection between the client and the server. 
* It receives the published article from the server.

### Server
* The server-side functionality is written in the Server package, with Communicate.java (interface), CommunicateImpl.java which has the core functionality of the publisher-subscriber system, 
a) **CommunicateHelper.java** for the helper functions to validate requests, and connect to the Registry Server, 
b) **GroupServer.java** which registers itself to the registry server and helps in propagating the publisher-subscriber system by using the client’s request, 
c) **GroupServerHeartbeat.java** which handles the connection with the Registry Server. 
* If the registry server does not receive a heartbeat from the group server for 5 seconds, it deregisters the server automatically.

### Registry Server
* It has a list of servers and their IPs and Port numbers in case a new client wants to join the system. 
* It also listens to hearbeats from the servers. If there is a failure, there is a failover case which is implemented which allows the client to automatically switch to a running server. 
* This was already given for the sake of this project. (Written in C and uses Linux RPC calls) 

##
### How to run
* To run the project, there is a file called **artifacts.zip** which contains all the jar files required. 
* There is one jar file called **pubsub_srv.jar**, which runs the Group Server instance. It can be run on several machines.
* Then there are multiple client jars, each of which has a defined role, done specifically for testing different scenarios (more details in the Clients section). The filenames are **pubsub_clnt.jar, pubsub_clnt1.jar, pubsub_clnt2.jar, pubsub_clnt3.jar**.  

* Now, the first step is to ensure that the RMI registry (Registry Server) is up and running on one of the machines. 

*java -jar pubsub_srv.jar <public ip addr of registry server>*

*java -jar pubsub_clnt.jar <public ip addr of registry server>*

##
### Group Members
* Bhaargav Sriraman (srira048)
* Soumya Agrawal (agraw184)
* Garima Mehta (mehta250)

##
For more information please refer the project document. 
