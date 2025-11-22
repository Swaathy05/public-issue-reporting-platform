@echo off
echo ========================================
echo Java Installation Verification
echo ========================================
echo.

echo Checking Java installation...
echo.

java -version >nul 2>&1
if errorlevel 1 (
    echo [X] Java is NOT installed or not in PATH
    echo.
    echo Please:
    echo 1. Install Java from the download page that opened
    echo 2. Make sure to check "Add to PATH" during installation
    echo 3. Close and reopen this window
    echo 4. Run this script again
    echo.
) else (
    echo [OK] Java is installed!
    echo.
    java -version
    echo.
    echo Checking JAVA_HOME...
    if defined JAVA_HOME (
        echo [OK] JAVA_HOME is set to: %JAVA_HOME%
    ) else (
        echo [WARNING] JAVA_HOME is not set
        echo You may need to set it manually
    )
    echo.
    echo ========================================
    echo You can now run your application!
    echo ========================================
    echo.
    echo Run this command:
    echo   mvn spring-boot:run
    echo.
)

pause

