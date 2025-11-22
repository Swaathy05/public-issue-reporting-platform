@echo off
echo ====================================
echo Starting Public Service Platform
echo ====================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo.
echo Building and starting the application...
echo This may take a minute on first run...
echo.

REM Run the application
mvn spring-boot:run

pause

