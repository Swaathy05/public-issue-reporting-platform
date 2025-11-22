# Quick Admin Setup Guide

## Step 1: Register a User
1. Go to: **http://localhost:8080**
2. Click **"Register"**
3. Fill in the form and create an account
4. **Remember your username!**

## Step 2: Make Yourself Admin

### Option A: Using H2 Console (Easiest)
1. Go to: **http://localhost:8080/h2-console**
2. Login:
   - **JDBC URL**: `jdbc:h2:file:./data/public_service_issues`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click **Connect**
4. Run this SQL (replace `your_username` with your actual username):
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```
5. Click **Run**

### Option B: Check Your Username First
If you forgot your username, run this in H2 console:
```sql
SELECT id, username, email, role FROM users;
```
Then update the role:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

## Step 3: Access Admin Dashboard
1. **Logout** from the website (if logged in)
2. **Login** again with your account
3. You should now see **"Admin"** link in the navigation
4. Click **"Admin"** to access the dashboard

## Direct Access (After Login as Admin)
You can also go directly to:
```
http://localhost:8080#admin
```

## What You'll See in Admin Dashboard:
- **Statistics**: Counts of issues by status
- **Kanban Board**: All issues organized in columns:
  - Pending
  - In Progress
  - Resolved
  - Rejected
- **Drag & Drop**: Move issues between statuses
- **Click Issues**: View full details and update with comments

## Troubleshooting:

**If Admin link doesn't appear:**
1. Make sure you logged out and logged back in after updating role
2. Check in H2 console: `SELECT username, role FROM users;`
3. Role should be exactly `ADMIN` (all caps, no quotes in database)

**If you get "Admin access required" message:**
- Your account is not set to ADMIN role
- Follow Step 2 again to update your role

