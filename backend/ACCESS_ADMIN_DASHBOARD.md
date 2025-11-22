# How to Access Admin Dashboard

## Quick Steps:

### 1. Open Application
Go to: **http://localhost:8080**

### 2. Create Admin User

#### Method 1: Via H2 Console (Recommended)
1. Go to: **http://localhost:8080/h2-console**
2. Login:
   - JDBC URL: `jdbc:h2:file:./data/public_service_issues`
   - Username: `sa`
   - Password: (leave empty)
3. Click **Connect**
4. First, register a user on the main website
5. Then in H2 console, run:
   ```sql
   UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
   ```
   Replace `your_username` with the username you registered with.

#### Method 2: Direct SQL (if you know the username)
```sql
-- Check existing users
SELECT id, username, email, role FROM users;

-- Make a user admin
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

### 3. Access Dashboard
1. **Logout** if you're logged in
2. **Login** with your admin account
3. Click **"Admin"** in the top navigation bar
4. You'll see the Admin Dashboard with:
   - Statistics cards
   - Kanban board with all issues
   - Ability to view and update issues

### 4. Features Available:
- **View all issues** organized by status (Pending, In Progress, Resolved, Rejected)
- **Drag and drop** issues between status columns
- **Click any issue card** to view full details
- **Update issue status** with comments
- **View statistics** (counts by status)
- **Analytics dashboard** (click "Analytics" in navigation)

## Troubleshooting:

**If "Admin" link doesn't appear:**
- Make sure you're logged in with an ADMIN role user
- Check in H2 console: `SELECT username, role FROM users;`
- The role should be exactly `ADMIN` (case-sensitive)

**If dashboard is empty:**
- Create some test issues first (as a regular user)
- Then view them in the admin dashboard

