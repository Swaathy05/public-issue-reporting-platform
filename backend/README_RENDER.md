# 🚀 Render Deployment Guide

## Quick Deploy (5 Steps)

### 1️⃣ Push to GitHub
Your code is already on GitHub at:
**https://github.com/Swaathy05/public-issue-reporting-platform**

### 2️⃣ Create Render Account
1. Go to **https://render.com**
2. Sign up/Login (use GitHub to sign in)

### 3️⃣ Create New Web Service
1. Click **"New +"** → **"Web Service"**
2. Connect your GitHub account
3. Select repository: **`Swaathy05/public-issue-reporting-platform`**
4. Render will auto-detect the configuration

### 4️⃣ Add PostgreSQL Database
1. In Render dashboard → **"New +"** → **"PostgreSQL"**
2. Name: `public-service-db`
3. Database: `public_service_issues`
4. User: `public_service_user`
5. Plan: **Free** (or choose paid)
6. Click **"Create Database"**

### 5️⃣ Configure Web Service
1. **Name**: `public-service-platform`
2. **Environment**: `Java`
3. **Build Command**: `cd backend && mvn clean package -DskipTests`
4. **Start Command**: `cd backend && java -jar target/issue-reporting-platform-0.0.1-SNAPSHOT.jar`
5. **Environment Variables**:
   ```
   SPRING_PROFILES_ACTIVE=render
   JWT_SECRET=your-random-secret-key-here
   DB_HOST=<from PostgreSQL service>
   DB_PORT=<from PostgreSQL service>
   DB_NAME=public_service_issues
   DB_USER=<from PostgreSQL service>
   DB_PASSWORD=<from PostgreSQL service>
   ```
6. Click **"Create Web Service"**

## ✅ Done! Your app is live!

Render URL: `https://your-app.onrender.com`

---

## 📋 Detailed Configuration

### Environment Variables

**Required:**
```
SPRING_PROFILES_ACTIVE=render
JWT_SECRET=generate-a-random-secret-key
DB_HOST=<from PostgreSQL service>
DB_PORT=5432
DB_NAME=public_service_issues
DB_USER=<from PostgreSQL service>
DB_PASSWORD=<from PostgreSQL service>
```

**Optional:**
```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
CORS_ORIGINS=https://your-app.onrender.com
```

### Getting Database Credentials

1. Go to your PostgreSQL service in Render
2. Click on the service
3. Go to **"Info"** tab
4. Copy:
   - **Internal Database URL** (for DB_HOST)
   - **Port** (usually 5432)
   - **Database Name**
   - **User**
   - **Password**

### Using render.yaml (Alternative)

If you use the `render.yaml` file:
1. Render will auto-detect it
2. Most settings are pre-configured
3. You still need to set `JWT_SECRET` manually

## 🔧 Configuration Files

✅ `render.yaml` - Render service configuration
✅ `application-render.properties` - Render-specific Spring config
✅ `Dockerfile` - Can be used if needed
✅ PostgreSQL dependency in `pom.xml`

## ⚠️ Important Notes

### File Storage
✅ Render provides **persistent disk storage** - uploaded files will persist!

### Database
✅ PostgreSQL is **persistent** - your data is safe!

### Free Tier Limitations
- Services spin down after 15 minutes of inactivity
- First request after spin-down takes ~30 seconds
- Upgrade to paid plan for always-on service

### Environment Variables
Render automatically provides:
- `PORT` (for the application)
- Database credentials (if using internal database)

## 🐛 Troubleshooting

### Build Fails
- ✅ Check Render build logs
- ✅ Ensure Maven is available
- ✅ Verify Java 17 is used
- ✅ Check `pom.xml` is correct

### App Crashes
- ✅ Check Render logs
- ✅ Verify `SPRING_PROFILES_ACTIVE=render`
- ✅ Check all environment variables are set
- ✅ Verify database is running

### Database Connection Error
- ✅ Ensure PostgreSQL service is running
- ✅ Check database credentials are correct
- ✅ Verify `DB_HOST`, `DB_USER`, `DB_PASSWORD` are set
- ✅ Check database is in same region as web service

### Port Issues
- ✅ Render sets `PORT` automatically
- ✅ Application uses `${PORT}` or defaults to 8080

## 📊 Monitoring

Render provides:
- **Logs**: Real-time application logs
- **Metrics**: CPU, Memory usage
- **Events**: Deployment history

## 🎯 Next Steps

1. ✅ Deploy to Render
2. ✅ Test your application
3. ✅ Create admin user (connect to PostgreSQL)
4. ✅ Set up custom domain (optional)
5. ✅ Configure email (optional)

## 📚 Resources

- Render Docs: https://render.com/docs
- Render Support: https://render.com/docs/support

---

**Your application is ready to deploy to Render! 🚀**

