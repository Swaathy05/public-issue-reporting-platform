@echo off
echo ========================================
echo Starting Application (Low Memory Mode)
echo ========================================
echo.

REM Set Java path
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM Set low memory JVM options
set MAVEN_OPTS=-Xmx512m -Xms256m -XX:MaxMetaspaceSize=256m

echo Using reduced memory settings:
echo   Max Heap: 512MB
echo   Initial Heap: 256MB
echo   Max Metaspace: 256MB
echo.

cd /d D:\finalyear\backend
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m -Xms256m -XX:MaxMetaspaceSize=256m"

pause

