@echo off

REM Arahkan ke direktori Kafka
cd /d D:\kafka

REM Menjalankan Zookeeper
echo Starting Zookeeper...
start cmd /k "bin\windows\zookeeper-server-start.bat config\zookeeper.properties"

REM Menjalankan Kafka
echo Starting Kafka...
start cmd /k "bin\windows\kafka-server-start.bat config\server.properties"

REM Menjalankan Aplikasi Spring Boot
echo Starting Spring Boot application...
cd /d "D:\Padepokan 79\GAP Analysis\Readiness Locomotive\Project\BE\generate_service"
mvn spring-boot:run
