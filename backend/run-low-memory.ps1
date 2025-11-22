# Run Spring Boot with reduced memory settings
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Application (Low Memory Mode)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set Java path
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Set low memory JVM options
$env:MAVEN_OPTS = "-Xmx512m -Xms256m -XX:MaxMetaspaceSize=256m"

Write-Host "Using reduced memory settings:" -ForegroundColor Yellow
Write-Host "  Max Heap: 512MB" -ForegroundColor Gray
Write-Host "  Initial Heap: 256MB" -ForegroundColor Gray
Write-Host "  Max Metaspace: 256MB" -ForegroundColor Gray
Write-Host ""

Set-Location D:\finalyear\backend

# Run with memory limits
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx512m -Xms256m -XX:MaxMetaspaceSize=256m"

