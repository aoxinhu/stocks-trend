
@REM start "eureka" cmd /k call ./mvnw spring-boot:run -pl eureka-server

start " third-part-index-data" cmd /k call ./mvnw spring-boot:run -pl third-part-index-data-project

start "gather-store" cmd /k call ./mvnw spring-boot:run -pl index-gather-store-service

start "codes1" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8011" -pl index-codes-service  
start "codes2" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8012" -pl index-codes-service  
start "codes3" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8013" -pl index-codes-service  

start "data1" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8021" -pl index-data-service 
start "data2" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8022" -pl index-data-service 
start "data3" cmd /k call ./mvnw spring-boot:run -Dspring-boot.run.arguments="8023" -pl index-data-service 