# springtest
Test Spring


### MyBatis
1. 自动生成Mapper，复杂的Mapper自定义
2. 自定义plugin必须是一个独立的jar，不能继承父项目

### 异步：Guava EventBus
1. biz/src/main/java/org/jsj/my/gov/TxGovernor.java

### 异步：Servlet
1. my_server/src/main/java/org/jsj/my/controller/AsyncTestController.java

### RocketMQ Consumer
1. my-service/src/main/java/org/jsj/my/config/RocketConfig.java#txConsumer
2. my-service/src/main/java/org/jsj/my/schedule/MaintenanceJob.java#startRocketConsumer

### RocketMQ Provider
1. my-service/src/main/java/org/jsj/my/config/RocketConfig.java#rocketMessageProducer

### Redis 订阅者
1. my-service/src/main/java/org/jsj/my/config/RedisConsumerConfig.java#messageListenerContainer

### 微服务
1. 服务端：my-service/src/main/java/org/jsj/my/micro/controller/TxController.java
2. 客户端：my-client/src/main/java/org/jsj/my/client/TxClient.java

