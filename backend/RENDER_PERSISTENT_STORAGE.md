# Render Persistent Storage Setup Guide

This guide explains how to configure persistent storage for file uploads on Render to ensure uploaded images persist across deployments.

## Overview

By default, files stored in the container filesystem are lost when you redeploy. To prevent data loss, we've configured persistent disk storage for the `uploads/` directory.

## Configuration

### 1. Automatic Configuration (via render.yaml)

The `render.yaml` file has been configured with persistent disk storage:

```yaml
disk:
  name: uploads-disk
  mountPath: /opt/render/project/src/uploads
  sizeGB: 1
```

This automatically creates a 1GB persistent disk mounted at `/opt/render/project/src/uploads`.

### 2. Manual Configuration (via Render Dashboard)

If you prefer to configure via the Render dashboard:

1. Go to your service in the Render dashboard
2. Navigate to **Settings** → **Disks**
3. Click **Add Disk**
4. Configure:
   - **Name**: `uploads-disk`
   - **Mount Path**: `/opt/render/project/src/uploads`
   - **Size**: `1 GB` (or more if needed)
5. Save the configuration

### 3. Environment Variable

The application uses the `UPLOAD_DIR` environment variable (defaults to `/opt/render/project/src/uploads` if not set).

To override, add in Render dashboard:
- **Key**: `UPLOAD_DIR`
- **Value**: `/opt/render/project/src/uploads`

## How It Works

1. **Database Data**: Already persists automatically (managed PostgreSQL database)
2. **File Uploads**: Now persist via persistent disk mounted at `/opt/render/project/src/uploads`
3. **Application Code**: Redeploys without affecting data

## Verification

After deployment:

1. Upload a test image through the application
2. Check that the file exists at `/opt/render/project/src/uploads` in the container
3. Redeploy the application
4. Verify the uploaded image still exists and is accessible

## Important Notes

- **Disk Size**: Start with 1GB. You can increase it later if needed (Render will charge for additional storage)
- **Backup**: Consider implementing automated backups for critical uploads
- **Alternative**: For production, consider using cloud storage (S3, Cloudinary) instead of persistent disk for better scalability

## Troubleshooting

### Files Still Lost After Deployment

1. Verify the disk is mounted: Check Render dashboard → Disks section
2. Verify environment variable: Check `UPLOAD_DIR` is set correctly
3. Check application logs: Look for file path errors

### Permission Issues

If you encounter permission errors:
1. The persistent disk should have correct permissions automatically
2. If issues persist, you may need to adjust file permissions in your startup script

## Cost Considerations

- **Free Tier**: Limited persistent disk storage
- **Paid Plans**: More storage available
- **Alternative**: Using external storage (S3) may be more cost-effective for large-scale deployments

## Migration from Existing Deployment

If you already have files in a non-persistent location:

1. Before enabling persistent disk, note the current file paths
2. After enabling persistent disk, you may need to manually copy existing files
3. Or wait for users to re-upload (if acceptable)

---

**Summary**: Your data (database + file uploads) will now persist across deployments! 🎉

