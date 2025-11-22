# Quick Start Guide

## Step 1: Download and Install Java

The download page should have opened in your browser. If not:
- Go to: **https://adoptium.net/temurin/releases/**
- Select: **Java 17 LTS** (or Java 21 LTS)
- Operating System: **Windows**
- Architecture: **x64**
- Click: **Download** (the .msi installer)

**IMPORTANT during installation:**
- ✅ Check the box "Add to PATH" or "Set JAVA_HOME variable"
- ✅ Complete the installation

## Step 2: Verify Java Installation

After installing Java:

1. **Close and reopen your PowerShell/CMD window**

2. Run the verification script:
   ```powershell
   cd D:\finalyear\backend
   .\verify-java.bat
   ```

   Or manually check:
   ```powershell
   java -version
   ```

   You should see something like:
   ```
   openjdk version "17.0.x" ...
   ```

## Step 3: Run Your Application

Once Java is verified:

```powershell
cd D:\finalyear\backend
mvn spring-boot:run
```

Wait for the message:
```
Started IssueReportingPlatformApplication
```

## Step 4: Access the Application

Open your browser and go to:
- **Main Application**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html

## Troubleshooting

### If `java -version` doesn't work:
1. Make sure you **restarted your terminal** after installing Java
2. Check if Java was added to PATH during installation
3. Manually set JAVA_HOME (see SETUP_JAVA.md)

### If port 8080 is already in use:
- Change port in `src/main/resources/application.properties`:
  ```
  server.port=8081
  ```

### Database Connection Error:
- Option 1: Install and configure MySQL (see application.properties)
- Option 2: Use H2 database (easier - see instructions below)

### Using H2 Database (No MySQL needed):

1. Rename configuration files:
   ```powershell
   cd D:\finalyear\backend\src\main\resources
   ren application.properties application.properties.mysql
   ren application-h2.properties application.properties
   ```

2. Run the application:
   ```powershell
   cd D:\finalyear\backend
   mvn spring-boot:run
   ```

3. Access H2 Console: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:file:./data/public_service_issues`
   - Username: `sa`
   - Password: (leave empty)

## Need Help?

- See `SETUP_JAVA.md` for detailed Java setup instructions
- See `README.md` for more information

