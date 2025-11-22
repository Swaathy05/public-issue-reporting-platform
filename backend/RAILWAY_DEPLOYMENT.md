# Deploy to Railway - Step by Step Guide

## Prerequisites
1. GitHub account (or GitLab/Bitbucket)
2. Railway account (sign up at https://railway.app)
3. Your code pushed to a Git repository

## Step 1: Prepare Your Code

### 1.1 Push to GitHub
```bash
cd D:\finalyear
git init
git add .
git commit -m "Initial commit - Public Service Issue Reporting Platform"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
git push -u origin main
```

### 1.2 Create .gitignore (if not exists)
Create `.gitignore` in the root:
```
target/
*.class
*.log
*.jar
*.war
*.ear
.DS_Store
.idea/
.vscode/
*.iml
data/
uploads/
*.db
```

## Step 2: Deploy to Railway

### 2.1 Create Railway Project
1. Go to https://railway.app
2. Click **"New Project"**
3. Select **"Deploy from GitHub repo"**
4. Connect your GitHub account
5. Select your repository
6. Railway will auto-detect the Dockerfile

### 2.2 Add PostgreSQL Database
1. In your Railway project, click **"+ New"**
2. Select **"Database"** → **"Add PostgreSQL"**
3. Railway will create a PostgreSQL database
4. Note the connection details (you'll need them)

### 2.3 Configure Environment Variables
In Railway project settings, go to **"Variables"** and add:

#### Required Variables:
```
SPRING_PROFILES_ACTIVE=railway
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
```

#### Database Variables (Auto-set by Railway):
Railway automatically sets:
- `DATABASE_URL` (PostgreSQL connection string)
- `DB_USER`
- `DB_PASSWORD`
- `DB_HOST`
- `DB_PORT`

#### Optional Variables:
```
# Email Configuration (if you want email notifications)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# CORS (update with your Railway domain)
CORS_ORIGINS=https://your-app.railway.app
```

### 2.4 Configure Build Settings
1. Go to **Settings** → **Build**
2. Set **Root Directory**: `backend`
3. Railway will use the `Dockerfile` in the backend folder

### 2.5 Deploy
1. Railway will automatically start building
2. Watch the build logs
3. Once deployed, Railway will provide a URL like: `https://your-app.railway.app`

## Step 3: Access Your Application

### 3.1 Get Your App URL
- Railway provides a URL like: `https://your-app.up.railway.app`
- You can also set a custom domain in Railway settings

### 3.2 Create Admin User
1. Go to your deployed app: `https://your-app.railway.app`
2. Register a new user
3. Connect to Railway PostgreSQL database:
   - Use Railway's database dashboard
   - Or use a PostgreSQL client with Railway's connection string
4. Run SQL to make user admin:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```

## Step 4: Configure Custom Domain (Optional)

1. In Railway project, go to **Settings** → **Domains**
2. Click **"Generate Domain"** or add your custom domain
3. Update `CORS_ORIGINS` environment variable with your domain

## Step 5: File Storage Note

⚠️ **Important**: Railway uses ephemeral storage. Uploaded files will be lost on redeploy.

### Solutions:
1. **Use Railway Volumes** (for persistent storage)
2. **Use Cloud Storage** (AWS S3, Cloudinary, etc.)
3. **Use Database** (store images as base64 - not recommended for large files)

## Troubleshooting

### Build Fails
- Check build logs in Railway
- Ensure `Dockerfile` is in the `backend` directory
- Verify Java 17 is used

### Database Connection Error
- Check `DATABASE_URL` environment variable
- Ensure PostgreSQL service is running
- Verify `SPRING_PROFILES_ACTIVE=railway` is set

### Application Crashes
- Check logs in Railway dashboard
- Verify all required environment variables are set
- Check memory limits (Railway free tier has limits)

### Port Issues
- Railway sets `PORT` environment variable automatically
- Application should use `${PORT}` or default to 8080

## Environment Variables Summary

| Variable | Required | Description |
|----------|----------|-------------|
| `SPRING_PROFILES_ACTIVE` | Yes | Set to `railway` |
| `JWT_SECRET` | Yes | Secret key for JWT tokens |
| `DATABASE_URL` | Auto | Set by Railway PostgreSQL |
| `PORT` | Auto | Set by Railway |
| `MAIL_USERNAME` | No | Email for notifications |
| `MAIL_PASSWORD` | No | Email password |
| `CORS_ORIGINS` | No | Allowed CORS origins |

## Quick Deploy Checklist

- [ ] Code pushed to GitHub
- [ ] Railway project created
- [ ] PostgreSQL database added
- [ ] Environment variables set
- [ ] Build directory set to `backend`
- [ ] Application deployed successfully
- [ ] Admin user created
- [ ] Application accessible via Railway URL

## Support

If you encounter issues:
1. Check Railway build/deploy logs
2. Check application logs in Railway dashboard
3. Verify all environment variables are set correctly
4. Ensure database is running and connected

