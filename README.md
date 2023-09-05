# stocks-trend
> (I'm still working on this project !!! ... )、

## eureka-server(8761)
> using debug to start this service  

>作为 SpringCloud 项目，里面是有很多的微服务的。 为了管理这些微服务，SpringCloud 提供了现成的产品： Eureka 注册中心

配置文件，提供 eureka 的相关信息。
- hostname: localhost 表示主机名称。
- registerWithEureka：false. 表示是否注册到服务器。 因为它本身就是服务器，所以就无需把自己注册到服务器了。
- fetchRegistry: false. 表示是否获取服务器的注册信息，和上面同理，这里也设置为 false。
- defaultZone： `http://${eureka.instance.hostname}:${server.port}/eureka/` 自己作为服务器，公布出来的地址。 比如后续某个微服务要把自己注册到 eureka server, 那么就要使用这个地址： `http://localhost:8761/eureka/`

name: eurka-server 表示这个微服务本身的名称是 eureka-server

```yml
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/

spring:
  application:
    name: eureka-server
```
## third-part-index-data-project(8090)
> ./mvnw spring-boot:run -pl third-part-index-data-project 

> 这是一个模拟的第三方数据服务，用来模拟第三方用以提供项目所需的数据
```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: third-part-index-data-project
```

## index-gather-store-service(8001)
> ./mvnw spring-boot:run -pl index-gather-store-service 

>第三方数据服务 的数据其实是存放在第三方的，今天能访问，明天或许就不能访问了，所以我们就需要把它采集下来，并且存储在本地。

>采集使用的是 RestTemplate 方式来做， 存储本地我们会采用 nosql redis 来保存。
- IndexService
    1. 增加 @CacheConfig(cacheNames="indexes") 表示缓存的名称是 indexes. 如图所示，保存到 redis 就会以 indexes 命名
    2. 在fetch_indexes_from_third_part 方法上增加： @Cacheable(key="'all_codes'") 表示保存到 redis 用的 key 就会使 all_codes.

```yml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: index-gather-store-service
```

## index-codes-service(8011 8012 8013)
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8011" -pl index-codes-service  
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8012" -pl index-codes-service  
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8013" -pl index-codes-service  
```
spring:
  application:
    name: index-codes-service
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```


## index-data-service(8021 8022 8023)
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8021" -pl index-data-service 
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8022" -pl index-data-service 
> ./mvnw spring-boot:run -Dspring-boot.run.arguments="8023" -pl index-data-service 
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: index-data-service
```




## index-zuul-service
> you'd better use debug to start index-zuul-service

访问网关地址：
http://127.0.0.1:8031/api-codes/codes

可以达到访问以下任意个微服务的效果
http://127.0.0.1:8011/codes
http://127.0.0.1:8012/codes
http://127.0.0.1:8013/codes

因为这3个微服务是属于一个集群的，所以访问网关会自动在集群里的这些微服务之间进行切换，这样就达到了动态平衡的效果

```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
spring:
  application:
    name: index-zuul-service
zuul:
  routes:
    api-a:
      path: /api-codes/**
      serviceId: INDEX-CODES-SERVICE
```

## index-config-server


## trend-trading-backtest-service
> ./mvnw spring-boot:run -pl trend-trading-backtest-service 

## trend-trading-backtest-view
> ./mvnw spring-boot:run -pl trend-trading-backtest-view 
