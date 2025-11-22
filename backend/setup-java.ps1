# Script to find and set JAVA_HOME on Windows
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Java Setup Helper" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if running as administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)

if (-not $isAdmin) {
    Write-Host "WARNING: Not running as Administrator" -ForegroundColor Yellow
    Write-Host "Some changes may require admin privileges." -ForegroundColor Yellow
    Write-Host ""
}

# Common Java installation locations
$javaPaths = @(
    "C:\Program Files\Java",
    "C:\Program Files (x86)\Java",
    "C:\Program Files\Eclipse Adoptium",
    "C:\Program Files\Microsoft",
    "C:\Program Files\OpenJDK",
    "C:\Program Files\Amazon Corretto",
    "$env:LOCALAPPDATA\Programs\Eclipse Adoptium"
)

Write-Host "Searching for Java installations..." -ForegroundColor Green
Write-Host ""

$foundJava = @()

foreach ($path in $javaPaths) {
    if (Test-Path $path) {
        Write-Host "Checking: $path" -ForegroundColor Gray
        $javaDirs = Get-ChildItem $path -Directory -ErrorAction SilentlyContinue
        
        foreach ($dir in $javaDirs) {
            $javaExe = Join-Path $dir.FullName "bin\java.exe"
            if (Test-Path $javaExe) {
                $version = & $javaExe -version 2>&1 | Select-Object -First 1
                Write-Host "  Found: $($dir.FullName)" -ForegroundColor Green
                Write-Host "    Version: $version" -ForegroundColor Gray
                Write-Host ""
                $foundJava += @{
                    Path = $dir.FullName
                    Version = $version
                }
            }
        }
    }
}

if ($foundJava.Count -eq 0) {
    Write-Host "No Java installation found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Java 17 or higher:" -ForegroundColor Yellow
    Write-Host "1. Go to: https://adoptium.net/" -ForegroundColor Cyan
    Write-Host "2. Download Java 17 LTS for Windows" -ForegroundColor Cyan
    Write-Host "3. Install it" -ForegroundColor Cyan
    Write-Host "4. Run this script again" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Or see SETUP_JAVA.md for detailed instructions" -ForegroundColor Yellow
    pause
    exit
}

Write-Host "Found $($foundJava.Count) Java installation(s):" -ForegroundColor Green
Write-Host ""

for ($i = 0; $i -lt $foundJava.Count; $i++) {
    Write-Host "[$($i+1)] $($foundJava[$i].Path)" -ForegroundColor Cyan
    Write-Host "     $($foundJava[$i].Version)" -ForegroundColor Gray
    Write-Host ""
}

# Select Java version (default to first one)
$selectedIndex = 0
if ($foundJava.Count -gt 1) {
    $choice = Read-Host "Select Java version (1-$($foundJava.Count)) [1]"
    if ($choice -ne "") {
        $selectedIndex = [int]$choice - 1
        if ($selectedIndex -lt 0 -or $selectedIndex -ge $foundJava.Count) {
            Write-Host "Invalid selection, using first option" -ForegroundColor Yellow
            $selectedIndex = 0
        }
    }
}

$javaHome = $foundJava[$selectedIndex].Path

Write-Host ""
Write-Host "Selected: $javaHome" -ForegroundColor Green
Write-Host ""

# Set JAVA_HOME for current session
$env:JAVA_HOME = $javaHome
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Set JAVA_HOME for current session" -ForegroundColor Green
Write-Host ""

# Verify
Write-Host "Verifying Java..." -ForegroundColor Cyan
$javaVersion = java -version 2>&1 | Select-Object -First 1
Write-Host "  Java version: $javaVersion" -ForegroundColor Gray

if ($javaVersion -like "*version*") {
    Write-Host "Java is working!" -ForegroundColor Green
    Write-Host ""
    
    # Ask to set permanently
    Write-Host "Do you want to set JAVA_HOME permanently (requires admin)?" -ForegroundColor Yellow
    $setPermanent = Read-Host "Yes/No [No]"
    
    if ($setPermanent -eq "Yes" -or $setPermanent -eq "Y" -or $setPermanent -eq "y") {
        if ($isAdmin) {
            try {
                [System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "Machine")
                
                # Add to PATH if not already there
                $currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
                if ($currentPath -notlike "*$javaHome\bin*") {
                    [System.Environment]::SetEnvironmentVariable("Path", "$currentPath;$javaHome\bin", "Machine")
                }
                
                Write-Host "JAVA_HOME set permanently!" -ForegroundColor Green
                Write-Host "  Note: You may need to restart your terminal" -ForegroundColor Yellow
            }
            catch {
                Write-Host "Failed to set JAVA_HOME permanently: $_" -ForegroundColor Red
            }
        }
        else {
            Write-Host "Need administrator privileges to set permanently" -ForegroundColor Red
            Write-Host "  Right-click PowerShell → Run as Administrator" -ForegroundColor Yellow
        }
    }
}
else {
    Write-Host "Java verification failed" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "You can now run: mvn spring-boot:run" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
pause

