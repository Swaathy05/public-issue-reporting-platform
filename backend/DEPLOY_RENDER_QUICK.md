# 🚀 Quick Render Deployment (5 Minutes)

## Step 1: Create Render Account
1. Go to **https://render.com**
2. Sign up/Login (use GitHub)

## Step 2: Create PostgreSQL Database
1. In Render dashboard → **"New +"** → **"PostgreSQL"**
2. Name: `public-service-db`
3. Database: `public_service_issues`
4. Plan: **Free**
5. Click **"Create Database"**
6. **Copy the credentials** (you'll need them)

## Step 3: Create Web Service
1. **"New +"** → **"Web Service"**
2. Connect GitHub → Select: `Swaathy05/public-issue-reporting-platform`
3. Configure:
   - **Name**: `public-service-platform`
   - **Environment**: `Java`
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `cd backend && java -jar target/issue-reporting-platform-0.0.1-SNAPSHOT.jar`

## Step 4: Set Environment Variables
In the web service settings → **Environment** tab, add:

```
SPRING_PROFILES_ACTIVE=render
JWT_SECRET=your-random-secret-key-here
DB_HOST=<from PostgreSQL service>
DB_PORT=5432
DB_NAME=public_service_issues
DB_USER=<from PostgreSQL service>
DB_PASSWORD=<from PostgreSQL service>
```

**Get database credentials from PostgreSQL service → Info tab**

## Step 5: Deploy!
1. Click **"Create Web Service"**
2. Wait for build to complete (~5-10 minutes)
3. Your app will be live at: `https://your-app.onrender.com`

## ✅ Done!

## Create Admin User
1. Register on your deployed app
2. Connect to Render PostgreSQL (use Render's database dashboard or psql)
3. Run: `UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';`

## 📝 Notes
- **Free tier**: Services spin down after 15 min inactivity (first request takes ~30 sec)
- **File storage**: Render provides persistent disk - files are saved!
- **Database**: PostgreSQL is persistent - data is safe

See `README_RENDER.md` for detailed guide.

