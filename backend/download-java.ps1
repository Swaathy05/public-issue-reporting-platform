# Script to open Java download page
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Opening Java Download Page..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Instructions:" -ForegroundColor Yellow
Write-Host "1. Select: Java 17 LTS (or Java 21 LTS)" -ForegroundColor White
Write-Host "2. Select: Windows" -ForegroundColor White
Write-Host "3. Select: x64" -ForegroundColor White
Write-Host "4. Click: Download .msi installer" -ForegroundColor White
Write-Host "5. IMPORTANT: During installation, check 'Add to PATH' or 'Set JAVA_HOME'" -ForegroundColor Green
Write-Host ""
Write-Host "Opening browser in 3 seconds..." -ForegroundColor Gray
Start-Sleep -Seconds 3

# Open the download page
Start-Process "https://adoptium.net/temurin/releases/?version=17"

Write-Host ""
Write-Host "Download page opened!" -ForegroundColor Green
Write-Host ""
Write-Host "After downloading and installing Java:" -ForegroundColor Yellow
Write-Host "1. Close and reopen this terminal" -ForegroundColor White
Write-Host "2. Run: java -version" -ForegroundColor White
Write-Host "3. Then run: cd D:\finalyear\backend" -ForegroundColor White
Write-Host "4. Then run: mvn spring-boot:run" -ForegroundColor White
Write-Host ""
pause

