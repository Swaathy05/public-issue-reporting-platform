# Quick Railway Deployment Guide

## 🚀 Fast Track (5 minutes)

### 1. Push to GitHub
```bash
cd D:\finalyear
git init
git add .
git commit -m "Ready for Railway deployment"
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git
git push -u origin main
```

### 2. Deploy on Railway
1. Go to **https://railway.app** → Sign up/Login
2. Click **"New Project"** → **"Deploy from GitHub repo"**
3. Select your repository
4. Railway will auto-detect and start building

### 3. Add Database
1. In Railway project → Click **"+ New"**
2. Select **"Database"** → **"Add PostgreSQL"**
3. Railway auto-configures it!

### 4. Set Environment Variables
In Railway → **Variables** tab, add:
```
SPRING_PROFILES_ACTIVE=railway
JWT_SECRET=change-this-to-a-random-secret-key
```

### 5. Configure Build
1. Go to **Settings** → **Build**
2. Set **Root Directory**: `backend`
3. Railway will rebuild automatically

### 6. Access Your App
- Railway gives you a URL like: `https://your-app.up.railway.app`
- Open it in browser!

### 7. Create Admin User
1. Register on your deployed app
2. Connect to Railway PostgreSQL (use Railway's database dashboard)
3. Run: `UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';`

## ✅ Done!

Your app is now live on Railway!

## 📝 Important Notes

- **File Uploads**: Railway uses ephemeral storage. Files will be lost on redeploy.
  - Consider using cloud storage (S3, Cloudinary) for production
  
- **Database**: Railway PostgreSQL is persistent and will keep your data

- **Environment Variables**: Railway automatically sets `DATABASE_URL` and `PORT`

- **Custom Domain**: You can add a custom domain in Railway Settings → Domains

## 🔧 Troubleshooting

**Build fails?**
- Check Railway build logs
- Ensure Dockerfile is in `backend/` folder

**App crashes?**
- Check Railway logs
- Verify `SPRING_PROFILES_ACTIVE=railway` is set
- Check all environment variables

**Database connection error?**
- Ensure PostgreSQL service is running
- Check `DATABASE_URL` is set (Railway sets this automatically)

## 📚 Full Guide

See `RAILWAY_DEPLOYMENT.md` for detailed instructions.

