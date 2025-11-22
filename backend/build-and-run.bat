@echo off
echo ========================================
echo Building and Running Application
echo ========================================
echo.

REM Set Java path
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

REM Set very low memory for Maven build
set MAVEN_OPTS=-Xmx256m -Xms128m

cd /d D:\finalyear\backend

echo Step 1: Building application (this may take a few minutes)...
echo.
mvn clean package -DskipTests -Dmaven.test.skip=true

if errorlevel 1 (
    echo.
    echo Build failed! Trying with even lower memory...
    set MAVEN_OPTS=-Xmx128m -Xms64m
    mvn clean package -DskipTests -Dmaven.test.skip=true
)

if errorlevel 1 (
    echo.
    echo Build still failed. Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Step 2: Starting application...
echo.
java -Xmx256m -Xms128m -jar target\issue-reporting-platform-0.0.1-SNAPSHOT.jar

pause

