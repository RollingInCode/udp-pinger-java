# UDP-Pinger Java

This is a school assignment I worked on for a Computer Networking class.

# Server Code 

The server will run on a fixed port value. It will wait for a client to connect via the “datagram” sockets. This way we are able to receive and send datagram packets. Datagrams uses the UDP protocol so there are instances that a packet from a client might not reach the server or a response packet form server might not reach the target client.
When a packet from client never reaches the server, then obviously the server will not be able to respond. However, if the server receives a packet from a client, it will randomly decide whether to respond to it or not. 30% of the time it will decide not to respond to the client. 
The server runs indefinitely.

# Client Code

The client code will need the server’s IP address and port. The client connects (via UDP) and attempts to send 10 ping messages. For each message, the client will wait for a response from the server. The client will have a 1 second timeout. If after a second and no response from server is received, then the client will retransmit again another message. Retransmission will happen at most 3 times before finally giving up and moving to the next ping message to send to server.
