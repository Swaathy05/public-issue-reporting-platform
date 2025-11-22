@echo off
echo ========================================
echo Installing Java...
echo ========================================
echo.
echo The Java installer will open now.
echo.
echo IMPORTANT: During installation, please check:
echo   [x] Add to PATH
echo   [x] Set JAVA_HOME variable
echo.
echo This is usually checked by default.
echo.
pause

echo Opening installer...
start "" "C:\Users\swaat\Downloads\OpenJDK17U-jdk_x64_windows_hotspot_17.0.17_10.msi"

echo.
echo ========================================
echo After installation is complete:
echo ========================================
echo 1. Close and reopen this terminal window
echo 2. Run: verify-java.bat
echo 3. Then run: mvn spring-boot:run
echo.
pause

