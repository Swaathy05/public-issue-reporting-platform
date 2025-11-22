# Script to find and configure Java
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Finding Java Installation..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Common Java installation locations
$searchPaths = @(
    "C:\Program Files\Eclipse Adoptium",
    "C:\Program Files\Java",
    "C:\Program Files (x86)\Java",
    "C:\Program Files\Microsoft",
    "C:\Program Files\OpenJDK",
    "C:\Program Files\Amazon Corretto"
)

$foundJava = $null

foreach ($basePath in $searchPaths) {
    if (Test-Path $basePath) {
        Write-Host "Checking: $basePath" -ForegroundColor Gray
        $javaDirs = Get-ChildItem $basePath -Directory -ErrorAction SilentlyContinue
        
        foreach ($dir in $javaDirs) {
            $javaExe = Join-Path $dir.FullName "bin\java.exe"
            if (Test-Path $javaExe) {
                Write-Host "  Found Java at: $($dir.FullName)" -ForegroundColor Green
                $version = & $javaExe -version 2>&1 | Select-Object -First 1
                Write-Host "  Version: $version" -ForegroundColor Gray
                
                if (-not $foundJava) {
                    $foundJava = $dir.FullName
                }
            }
        }
    }
}

if (-not $foundJava) {
    Write-Host ""
    Write-Host "Java not found in common locations!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please try:" -ForegroundColor Yellow
    Write-Host "1. Restart your computer" -ForegroundColor White
    Write-Host "2. Open a NEW PowerShell/CMD window" -ForegroundColor White
    Write-Host "3. Run: java -version" -ForegroundColor White
    Write-Host ""
    Write-Host "If that doesn't work, you may need to:" -ForegroundColor Yellow
    Write-Host "- Reinstall Java and make sure 'Add to PATH' is checked" -ForegroundColor White
    Write-Host "- Or manually add Java to PATH" -ForegroundColor White
    pause
    exit
}

Write-Host ""
Write-Host "Setting JAVA_HOME for this session..." -ForegroundColor Green
$env:JAVA_HOME = $foundJava
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host ""
Write-Host "Verifying Java..." -ForegroundColor Cyan
$javaVersion = java -version 2>&1 | Select-Object -First 1
Write-Host "  $javaVersion" -ForegroundColor Green

if ($javaVersion -like "*version*") {
    Write-Host ""
    Write-Host "Java is working!" -ForegroundColor Green
    Write-Host ""
    Write-Host "To make this permanent:" -ForegroundColor Yellow
    Write-Host "1. Close and reopen your terminal (PATH will be updated)" -ForegroundColor White
    Write-Host "2. Or run this as Administrator to set permanently:" -ForegroundColor White
    Write-Host "   [System.Environment]::SetEnvironmentVariable('JAVA_HOME', '$foundJava', 'Machine')" -ForegroundColor Gray
    Write-Host ""
    Write-Host "Now you can run: mvn spring-boot:run" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "Java verification failed. Please restart your terminal." -ForegroundColor Red
}

Write-Host ""
pause

