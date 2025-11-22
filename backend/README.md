# Public Service Issue Reporting Platform

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (for building)
- **MySQL** (optional, for production) OR **H2 Database** (included, for development)

## Quick Start (Using H2 Database)

### 1. Navigate to Backend Directory
```bash
cd backend
```

### 2. Run the Application
```bash
# Using Maven
mvn spring-boot:run

# OR using Java directly (after building)
mvn clean package
java -jar target/issue-reporting-platform-0.0.1-SNAPSHOT.jar
```

### 3. Access the Application
- **Frontend/Web UI**: http://localhost:8080
- **API Documentation (Swagger)**: http://localhost:8080/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/public_service_issues`
  - Username: `sa`
  - Password: (leave empty)

## Setup with MySQL (Production)

### 1. Create MySQL Database
```sql
CREATE DATABASE public_service_issues;
CREATE USER 'ps_user'@'localhost' IDENTIFIED BY 'ps_pass';
GRANT ALL PRIVILEGES ON public_service_issues.* TO 'ps_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Update Configuration
Edit `src/main/resources/application.properties`:
- Update database username/password if different
- Ensure MySQL server is running on port 3306

### 3. Run Application
```bash
mvn spring-boot:run
```

## Running on Windows

### Using Command Prompt or PowerShell:
```cmd
cd backend
mvn spring-boot:run
```

### Using IntelliJ IDEA:
1. Open the project in IntelliJ
2. Navigate to `IssueReportingPlatformApplication.java`
3. Right-click → Run 'IssueReportingPlatformApplication'
4. Or use the Run button (▶️)

### Using Eclipse:
1. Import as Maven project
2. Right-click project → Run As → Spring Boot App

## Using H2 Profile (Easier for Development)

To use H2 database instead of MySQL, you can:

### Option 1: Set Active Profile
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### Option 2: Rename Configuration File
Temporarily rename:
- `application.properties` → `application.properties.bak`
- `application-h2.properties` → `application.properties`

### Option 3: Update application.properties
Comment out MySQL settings and uncomment H2 settings.

## Default Features

### User Roles:
- **CITIZEN**: Can report issues and view their own issues
- **ADMIN**: Can view all issues, update status, access analytics

### First Time Setup:
1. Register a new user account (default role: CITIZEN)
2. To create an admin user, you can:
   - Manually update the database to change role to ADMIN
   - Use the admin endpoint: `POST /api/admin/users` (requires admin authentication)
   - Or create via H2 console

## API Endpoints

- **Authentication**: `/api/auth/signin`, `/api/auth/signup`
- **Issues**: `/api/issues` (GET, POST)
- **Admin**: `/api/admin/issues` (GET all issues)
- **Admin Stats**: `/api/admin/stats`
- **Analytics**: `/api/analytics/dashboard`

## Troubleshooting

### Port 8080 Already in Use
Change port in `application.properties`:
```properties
server.port=8081
```

### Database Connection Error
- For MySQL: Ensure MySQL server is running and credentials are correct
- For H2: The database file will be created automatically in `backend/data/`

### Build Errors
```bash
# Clean and rebuild
mvn clean install
mvn spring-boot:run
```

## File Uploads
Uploaded photos are stored in `backend/uploads/` directory (created automatically).

## Email Configuration (Optional)
If you want email notifications, update in `application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

Note: For Gmail, you need to use an "App Password" instead of your regular password.

