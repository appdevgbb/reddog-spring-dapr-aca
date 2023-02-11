TODO:

Services to enable locally:

+ [X] Kafka 
+ [X] Cosmos
+ MySQL
+ [X] Redis
+ [X] Storage
+ 



Dapr components:

+ [ ] Binding receipt - localstorage
+ [ ] Binding virtual worker - cron
+ [ ] PubSub redis - order-service, make-line-service, loyalty-service, receipt-generation-service, accounting-service
+ [ ] State store - loyalty - redis
+ [ ] State store - makeline - redis




- [ ] Accounting service - pub sub, subcription on ordertopic and ordercompletedtopic, stores orders to DB (statestore?)
- [ ] Loyalty service - subscribes to ordertopic and updates the loyalty for the loyaltyId using statestore - reddog.state.loyalty
- [ ] Makeline service - subscribes to ordertopic and stores to statestore (reddog.state.makeline), when completes an order (http), it publishes to ordercompletedtopic
- [X] Order service - creating new order publishes an event to ordertopic, gets all products (no dapr)
- [x] receipt generation service - subscribes to ordertopic and is using a binding to generate a local file with the receipt (can be changed to something else)

virtual customer - uses dapr invoke the order service get the products and create the order

virtual worker - gets and completes orders by using dapr method invocation