# 🚂 Railway Deployment - Complete Guide

## Quick Deploy (5 Steps)

### 1️⃣ Push to GitHub
```bash
git init
git add .
git commit -m "Ready for Railway"
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

### 2️⃣ Create Railway Project
1. Go to **https://railway.app** → Sign up/Login
2. **New Project** → **Deploy from GitHub repo**
3. Select your repository
4. Railway auto-detects Dockerfile

### 3️⃣ Add PostgreSQL Database
1. In Railway project → **"+ New"**
2. **Database** → **Add PostgreSQL**
3. Railway auto-configures connection!

### 4️⃣ Set Environment Variables
In Railway → **Variables** tab:
```
SPRING_PROFILES_ACTIVE=railway
JWT_SECRET=your-random-secret-key-here
```

### 5️⃣ Configure Build
1. **Settings** → **Build**
2. **Root Directory**: `backend`
3. Railway rebuilds automatically

## ✅ Done! Your app is live!

Railway URL: `https://your-app.up.railway.app`

---

## 📋 Detailed Steps

### Step 1: Prepare Repository

**Create `.gitignore`** (already created):
```
target/
*.class
*.log
data/
uploads/
*.db
```

**Push to GitHub:**
```bash
cd D:\finalyear
git init
git add .
git commit -m "Public Service Platform - Ready for Railway"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

### Step 2: Railway Setup

1. **Sign up**: https://railway.app (use GitHub to sign in)
2. **New Project** → **Deploy from GitHub repo**
3. **Authorize Railway** to access your GitHub
4. **Select repository**
5. Railway starts building automatically

### Step 3: Database Configuration

Railway PostgreSQL provides these environment variables:
- `PGHOST` - Database host
- `PGPORT` - Database port (usually 5432)
- `PGDATABASE` - Database name
- `PGUSER` - Database user
- `PGPASSWORD` - Database password

**These are set automatically!** No manual configuration needed.

### Step 4: Environment Variables

Go to Railway project → **Variables** tab:

**Required:**
```
SPRING_PROFILES_ACTIVE=railway
JWT_SECRET=generate-a-random-secret-key-here
```

**Optional (for email):**
```
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
CORS_ORIGINS=https://your-app.up.railway.app
```

### Step 5: Build Configuration

1. Go to **Settings** → **Build**
2. Set **Root Directory**: `backend`
3. Railway will rebuild

### Step 6: Access Your App

1. Railway provides a URL: `https://your-app.up.railway.app`
2. Open in browser
3. Register a user
4. Create admin (see below)

### Step 7: Create Admin User

**Option A: Using Railway Database Dashboard**
1. In Railway → Click on PostgreSQL service
2. Go to **"Data"** tab
3. Run SQL:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

**Option B: Using psql (if you have it)**
```bash
# Get connection string from Railway PostgreSQL service
psql $DATABASE_URL
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

## 🔧 Configuration Files Created

✅ `railway.toml` - Railway configuration
✅ `application-railway.properties` - Railway-specific Spring config
✅ `Dockerfile` - Updated for Railway
✅ `.dockerignore` - Excludes unnecessary files
✅ PostgreSQL dependency added to `pom.xml`

## ⚠️ Important Notes

### File Storage
Railway uses **ephemeral storage**. Uploaded files will be lost on redeploy.

**Solutions:**
- Use Railway Volumes (persistent storage)
- Use cloud storage (AWS S3, Cloudinary, etc.)
- Store in database (not recommended for large files)

### Database
✅ PostgreSQL is **persistent** - your data is safe!

### Environment Variables
Railway automatically sets:
- `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`
- `PORT` (for the application)

### Custom Domain
1. Railway → **Settings** → **Domains**
2. Click **"Generate Domain"** or add custom domain
3. Update `CORS_ORIGINS` variable with your domain

## 🐛 Troubleshooting

### Build Fails
- ✅ Check Railway build logs
- ✅ Ensure `Dockerfile` is in `backend/` folder
- ✅ Verify Java 17 is used

### App Crashes
- ✅ Check Railway logs
- ✅ Verify `SPRING_PROFILES_ACTIVE=railway`
- ✅ Check all environment variables are set
- ✅ Verify database is running

### Database Connection Error
- ✅ Ensure PostgreSQL service is running
- ✅ Check `PGHOST`, `PGUSER`, etc. are set (Railway sets these automatically)
- ✅ Verify `SPRING_PROFILES_ACTIVE=railway`

### Port Issues
- ✅ Railway sets `PORT` automatically
- ✅ Application uses `${PORT}` or defaults to 8080

## 📊 Monitoring

Railway provides:
- **Logs**: Real-time application logs
- **Metrics**: CPU, Memory usage
- **Deployments**: Deployment history

## 🎯 Next Steps

1. ✅ Deploy to Railway
2. ✅ Test your application
3. ✅ Create admin user
4. ✅ Set up custom domain (optional)
5. ✅ Configure email (optional)
6. ✅ Set up file storage solution (for production)

## 📚 Resources

- Railway Docs: https://docs.railway.app
- Railway Discord: https://discord.gg/railway
- Your deployment guide: `RAILWAY_DEPLOYMENT.md`

---

**Your application is now ready to deploy to Railway! 🚀**

