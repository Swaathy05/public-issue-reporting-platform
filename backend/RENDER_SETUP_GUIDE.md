# Render Deployment Configuration Guide

## Form Configuration

### 1. Source Code
✅ Already set: `github.com/Swaathy05/public-issue-reporting-platform`

### 2. Service Configuration

**Name**: `public-issue-reporting-platform` ✅ (already set)

**Language**: `Docker` ✅ (correct - you have a Dockerfile)

**Branch**: `main` ✅ (correct)

**Region**: `Singapore` ✅ (or choose your preferred region)

**Root Directory**: 
```
backend
```
⚠️ **IMPORTANT**: Set this to `backend` because your Dockerfile and code are in the backend folder!

**DockerfilePath**: 
```
backend/Dockerfile
```
Or leave empty if Render auto-detects it.

### 3. Instance Type

**For testing**: Free ($0/month) - Services spin down after 15 min
**For production**: Starter ($7/month) - Always on, better performance

### 4. Environment Variables

Click **"+ Add Environment Variable"** and add these:

**Required:**
```
SPRING_PROFILES_ACTIVE = render
JWT_SECRET = your-random-secret-key-here-change-this
```

**Database Variables** (get these from your PostgreSQL service):
```
DB_HOST = <from PostgreSQL service>
DB_PORT = 5432
DB_NAME = public_service_issues
DB_USER = <from PostgreSQL service>
DB_PASSWORD = <from PostgreSQL service>
```

**Optional:**
```
CORS_ORIGINS = https://your-app.onrender.com
MAIL_USERNAME = your-email@gmail.com
MAIL_PASSWORD = your-app-password
```

### 5. Before You Deploy

**First, create PostgreSQL database:**
1. In Render dashboard → **"New +"** → **"PostgreSQL"**
2. Name: `public-service-db`
3. Database: `public_service_issues`
4. Copy the credentials
5. Then come back to web service form and add them as environment variables

### 6. Deploy!

Click **"Deploy Web Service"** and wait for build (~5-10 minutes)

## Quick Checklist

- [ ] Root Directory set to: `backend`
- [ ] DockerfilePath: `backend/Dockerfile` (or auto-detect)
- [ ] PostgreSQL database created first
- [ ] Environment variables added:
  - [ ] SPRING_PROFILES_ACTIVE=render
  - [ ] JWT_SECRET (random secret)
  - [ ] DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
- [ ] Instance type selected (Free or Starter)
- [ ] Click "Deploy Web Service"

## After Deployment

1. Wait for build to complete
2. Your app will be at: `https://your-app.onrender.com`
3. Register a user
4. Create admin (connect to PostgreSQL and run SQL)

