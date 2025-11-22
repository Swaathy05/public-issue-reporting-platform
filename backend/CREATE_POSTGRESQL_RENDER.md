# How to Create PostgreSQL on Render (FREE)

## Step-by-Step:

### 1. In Render Dashboard
1. Click **"New +"** button (top right)
2. Select **"PostgreSQL"**

### 2. Configure Database
Fill in the form:

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
- Choose same region as your web service (Singapore if that's what you selected)

**Plan**: 
- Select **"Free"** ($0/month)
- Or choose paid if you want better performance

### 3. Create Database
Click **"Create Database"**

### 4. Get Credentials
After creation:
1. Click on your PostgreSQL service
2. Go to **"Info"** tab
3. You'll see:
   - **Internal Database URL** (for DB_HOST)
   - **Port** (usually 5432)
   - **Database Name**
   - **User**
   - **Password**

### 5. Copy These Values
You'll need them for your web service environment variables:
- `DB_HOST` = from Internal Database URL
- `DB_PORT` = 5432 (usually)
- `DB_NAME` = public_service_issues
- `DB_USER` = from Info tab
- `DB_PASSWORD` = from Info tab

## Alternative: Use H2 Database (Not Recommended for Production)

If you want to skip PostgreSQL for now, I can configure the app to use H2 database, but:
- ⚠️ Data will be lost on redeploy
- ⚠️ Not suitable for production
- ✅ But works for quick testing

Let me know if you want me to configure H2 instead!

