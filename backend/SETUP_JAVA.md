# How to Install Java and Fix JAVA_HOME Error

## Step 1: Download Java 17 or Higher

### Option A: Oracle JDK (Free for Development)
1. Go to: https://www.oracle.com/java/technologies/downloads/
2. Download **Java 17** or **Java 21** for Windows x64
3. Choose the installer (.exe file)

### Option B: OpenJDK (Recommended - Free and Open Source)
1. Go to: https://adoptium.net/ (Temurin - Recommended)
2. Or: https://www.microsoft.com/openjdk
3. Download **Java 17 LTS** or **Java 21 LTS** for Windows x64
4. Choose the `.msi` installer

## Step 2: Install Java
1. Run the downloaded installer (.exe or .msi)
2. **IMPORTANT**: Check the box that says "Set JAVA_HOME variable" or "Add to PATH"
3. Click "Install" and follow the wizard
4. Complete the installation

## Step 3: Verify Installation

Open a **NEW** PowerShell/CMD window and run:
```powershell
java -version
```

You should see something like:
```
java version "17.0.x" ...
```

## Step 4: Set JAVA_HOME Manually (If Step 2 didn't work)

### Method A: Using GUI
1. Press `Win + R`, type `sysdm.cpl`, press Enter
2. Click **"Environment Variables"** button
3. Under **"System variables"**, click **"New"**
4. Variable name: `JAVA_HOME`
5. Variable value: `C:\Program Files\Java\jdk-17` (or wherever Java is installed)
   - Common locations:
     - `C:\Program Files\Java\jdk-17`
     - `C:\Program Files\Eclipse Adoptium\jdk-17.0.x-hotspot`
     - `C:\Program Files\Microsoft\jdk-17.0.x`
6. Find `Path` variable, click **"Edit"**
7. Click **"New"** and add: `%JAVA_HOME%\bin`
8. Click OK on all windows

### Method B: Using PowerShell (Run as Administrator)
```powershell
# Find Java installation path first
Get-ChildItem "C:\Program Files\Java" -Directory

# Then set JAVA_HOME (replace path with your actual Java path)
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")

# Add to PATH
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
[System.Environment]::SetEnvironmentVariable("Path", "$currentPath;%JAVA_HOME%\bin", "Machine")
```

## Step 5: Restart Terminal
**IMPORTANT**: Close and reopen your PowerShell/CMD window for changes to take effect.

## Step 6: Verify JAVA_HOME
```powershell
echo $env:JAVA_HOME
java -version
mvn -version
```

If you see the Java version, you're good to go!

## Step 7: Run Your Application
```powershell
cd backend
mvn spring-boot:run
```

---

## Alternative: Use IntelliJ IDEA or Eclipse

If you have IntelliJ IDEA or Eclipse installed, you can:
1. Open the project in the IDE
2. The IDE will use its bundled JDK
3. Right-click `IssueReportingPlatformApplication.java` → Run

This doesn't require setting JAVA_HOME manually.

