# GitHub Pages Deployment Guide

## Quick Start Options

Choose one of the following methods to publish your Rafiq project on GitHub Pages:

---

## Option 1: Create Repository Manually (Recommended for Beginners)

### Step 1: Create the repository on GitHub
1. Go to https://github.com/new
2. Repository name: `rafik`
3. Visibility: Public or Private (your choice)
4. Check "Add README", "Add .gitignore" (select Node)
5. Click "Create repository"

### Step 2: Configure Git Authentication
Open PowerShell and run:
```powershell
# First, check if you're already logged in
git remote -v

# If authentication fails, create SSH key or use Personal Access Token
# Option A: Using SSH (recommended)
ssh-keygen -t ed25519 -C "your-email@example.com"
# Paste the public key into GitHub: Settings > SSH and GPG keys > New SSH key

# Option B: Using Personal Access Token
git config --global credential.helper store
git remote set-url origin https://oauth2:YOUR_GITHUB_TOKEN@github.com/barjuos/rafik.git
```

### Step 3: Push Your Code
```powershell
cd C:\Users\ibrah\Downloads\RAFIQ
git branch -M main
git push -u origin main --force
```

### Step 4: Enable GitHub Pages
1. Go to your repository on GitHub
2. Settings > Pages
3. Source: Deploy from a branch
4. Branch: main / root
5. Click Save
6. Wait 1-2 minutes, then visit: `https://github.com/barjuos/rafik`

---

## Option 2: Create Repository via GitHub CLI (Faster)

```powershell
# Install GitHub CLI (if not already installed):
# Windows: https://cli.github.com/

# Login to GitHub
gh auth login

# Create repository
gh repo create barjuos/rafik --public --source=. --push

# Done! Your code is pushed and ready
```

### Then enable GitHub Pages:
1. Go to https://github.com/barjuos/rafik/settings/pages
2. Source: Deploy from branch → main → / (root)
3. Click Save

Your site will be live at: `https://barjuos.github.io/rafik`

---

## Option 3: Manual Repository Setup

If you prefer to do this step-by-step manually:

### Step 1: Create Repository on GitHub
Visit: https://github.com/new
- Name: `rafik`
- Description: "Rafiq - Daily Dhikr Companion App"
- Initialize with README: ✅
- Add .gitignore: Yes (select Node.js)

### Step 2: Configure Git Remote
```powershell
cd C:\Users\ibrah\Downloads\RAFIQ
git remote remove origin
git remote add origin https://github.com/barjuos/rafik.git
git fetch origin
```

### Step 3: Push Your Code
```powershell
git push -u origin main --force
```

### Step 4: Enable GitHub Pages
1. Navigate to your repository on GitHub
2. Click **Settings** tab
3. Scroll down to **Pages** section
4. Under "Source", select:
   - Branch: `main`
   - Folder: `/ (root)`
5. Click **Save**

Your website will be available at:
```
https://barjuos.github.io/rafik
```

---

## Static Site Generation Note

### For GitHub Pages Static Hosting

If you want to host the Next.js app as a static site, update `next.config.ts`:

```typescript
const nextConfig = {
  output: 'export', // Generate static HTML/CSS/JS files
  images: { unoptimized: true }, // Required for export mode
}
```

Then build with:
```bash
npm run build
```

This creates a `out/` folder that can be served by GitHub Pages.

---

## Troubleshooting

### "Invalid username or token" Error
- Create a Personal Access Token at: https://github.com/settings/tokens
  - Select scopes: `repo`, `pages`
  - Use the token when pushing via HTTPS

### Repository Already Exists with Different Content
```powershell
git push -u origin main --force-with-lease
```

### Cannot Push to "read-only" repository
1. Make sure the repository is Public (not private without access)
2. Check your GitHub account permissions
3. Verify the remote URL is correct

---

## Deployment Checklist

Before deploying:
- [ ] Repository created on GitHub
- [ ] Remote URL configured correctly
- [ ] Code pushed to main branch
- [ ] GitHub Pages enabled in Settings
- [ ] README.md updated with project info
- [ ] License file added
- [ ] Docs folder populated

After deployment:
- [ ] Visit `https://github.com/barjuos/rafik`
- [ ] Check all documentation pages load correctly
- [ ] Test links and navigation
- [ ] Share your GitHub Pages URL with team members

---

## Additional Features to Consider

### Deploy Android APK via Releases
```bash
cd android
./gradlew assembleRelease
# Upload APK to GitHub Releases at: https://github.com/barjuos/rafik/releases/new
```

### Add Analytics (Optional)
Add Google Analytics or Plausible to track visitors.

### Automatic Deployments
The `.github/workflows/pages.yml` file will automatically deploy updates when you push to main.

---

## Support

For issues or questions:
- Open an issue at https://github.com/barjuos/rafik/issues
- Email: dev@rafiq.com

---

**Ready to publish?** Choose Option 2 (GitHub CLI) for the fastest deployment, or Option 1 for manual control!
