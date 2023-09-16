
@REM start "redis" cmd /T:0b /k call G:\apps\redis\redis\redis-server.exe

@REM @REM 蓝色
@REM start "eureka" cmd  /T:01 /K call ./mvnw.cmd spring-boot:run -pl eureka-server

@REM @REM 紫色
@REM start "third-part-index-data"  cmd /T:05 /k call ./mvnw.cmd spring-boot:run -pl third-part-index-data-project

@REM 黄色
@REM start "gather-store" cmd /T:06 /k call ./mvnw spring-boot:run -pl index-gather-store-service
@REM 淡红色
@REM start "codes1" cmd /T:0c /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8011" -pl index-codes-service  
@REM start "codes2" cmd /T:0c /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8012" -pl index-codes-service  
@REM start "codes3" cmd /T:0c /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8013" -pl index-codes-service  
@REM 淡黄色
@REM start "data1" cmd /T:0e /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8021" -pl index-data-service 
@REM start "data2" cmd /T:0e /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8022" -pl index-data-service 
@REM start "data3" cmd /T:0e /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8023" -pl index-data-service 

@REM 红色
@REM start "zuul" cmd /T:04 /k call ./mvnw spring-boot:run -pl index-zuul-service

@REM 浅绿
@REM start "service1" cmd /T:0a /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8051" -pl trend-trading-backtest-service 
@REM start "service2" cmd /T:0a /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8052" -pl trend-trading-backtest-service
@REM start "service3" cmd /T:0a /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8053" -pl trend-trading-backtest-service

@REM 浅蓝
start "view1" cmd /T:09 /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8041" -pl trend-trading-backtest-view
start "view2" cmd /T:09 /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8042" -pl trend-trading-backtest-view
@REM start "view3" cmd /T:09 /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="port=8043" -pl trend-trading-backtest-view