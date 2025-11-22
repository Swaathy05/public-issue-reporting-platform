@echo off
echo ========================================
echo Starting Public Service Platform
echo ========================================
echo.

REM Set Java path
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo Checking Java...
java -version
if errorlevel 1 (
    echo ERROR: Java not found!
    pause
    exit /b 1
)

echo.
echo Starting application with low memory settings...
echo This will take 30-60 seconds to start...
echo.
echo Once you see "Started IssueReportingPlatformApplication"
echo Open your browser to: http://localhost:8080
echo.
echo Press Ctrl+C to stop the application
echo.

cd /d D:\finalyear\backend
java -Xmx256m -Xms128m -jar target\issue-reporting-platform-0.0.1-SNAPSHOT.jar

pause

