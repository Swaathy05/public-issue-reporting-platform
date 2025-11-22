# Create PostgreSQL on Render (FREE) - Step by Step

## 🆓 It's FREE! Here's how:

### Step 1: In Render Dashboard
1. You should see a **"New +"** button (usually top right)
2. Click it
3. Select **"PostgreSQL"** from the dropdown

### Step 2: Fill the Form

**Name**: 
```
public-service-db
```

**Database**: 
```
public_service_issues
```

**User**: 
```
public_service_user
```

**Region**: 
- Choose **Singapore** (same as your web service) or your preferred region

**Plan**: 
- Select **"Free"** - It's $0/month! ✅

### Step 3: Create
Click **"Create Database"**

### Step 4: Get Your Credentials
After it's created:
1. Click on the PostgreSQL service name (`public-service-db`)
2. Go to **"Info"** tab
3. You'll see all the connection details:
   - **Internal Database URL**
   - **Port** (usually 5432)
   - **Database Name**
   - **User**
   - **Password**

### Step 5: Use in Web Service
Copy these values and add them as environment variables in your web service:
- `DB_HOST` = extract from Internal Database URL
- `DB_PORT` = 5432
- `DB_NAME` = public_service_issues
- `DB_USER` = from Info tab
- `DB_PASSWORD` = from Info tab

## 🎯 Quick Visual Guide

```
Render Dashboard
  └─> Click "New +"
      └─> Select "PostgreSQL"
          └─> Fill form:
              - Name: public-service-db
              - Database: public_service_issues
              - User: public_service_user
              - Plan: Free
          └─> Click "Create Database"
          └─> Click on database → "Info" tab
          └─> Copy credentials
```

## ⚠️ Important Notes

- **Free tier**: 90 days free, then $7/month (but you can delete and recreate)
- **Data persistence**: Your data is safe and persistent
- **Same region**: Keep database and web service in same region for better performance

## 🚀 After Creating Database

Go back to your web service form and add the database credentials as environment variables!

