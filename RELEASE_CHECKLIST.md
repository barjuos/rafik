# 🚀 Rafiq Release Checklist

This document guides you through creating and publishing a secure release of the Rafiq Android app.

---

## ✅ Prerequisites

- [ ] Keystore file created (`rafik-release.keystore`)
- [ ] `keystore.properties` configured with correct password
- [ ] `.gitignore` updated to protect keystore files
- [ ] Security documentation reviewed and pushed to GitHub

---

## 📦 Building the Release APK

### Step 1: Navigate to Android Directory
```powershell
cd C:\Users\ibrah\Downloads\RAFIQ\android
```

### Step 2: Build Release APK
```powershell
.\gradlew.bat assembleRelease
```

**Expected Output:**
- Progress bars showing compilation
- "BUILD SUCCESSFUL" message
- APK created at: `app/build/outputs/apk/release/app-release.apk`

### Step 3: Verify APK
```powershell
# Check file exists
Test-Path "app\build\outputs\apk\release\app-release.apk"

# Check file size (should be ~3-5 MB)
Get-Item "app\build\outputs\apk\release\app-release.apk" | Select-Object Length, LastWriteTime
```

**Example Output:**
```
Length     LastWriteTime
------            -------------
3287467        10/29/2025 12:00:00 PM
```

---

## 📤 Creating GitHub Release

### Step 1: Download APK
- Open File Explorer
- Navigate to: `C:\Users\ibrah\Downloads\RAFIQ\android\app\build\outputs\apk\release\`
- Copy `app-release.apk` to your Downloads folder for backup

### Step 2: Create Release on GitHub

1. Go to: **https://github.com/barjuos/rafik/releases/new**

2. Fill in the release details:

   **Tag version:**
   ```
   v1.0.0
   ```

   **Title:**
   ```
   Rafiq v1.0.0 - Secure Release with Code Protection
   ```

   **Description:**
   ```markdown
   # 🎉 Rafiq v1.0.0 Released!

   ## What's New
   - Initial release with 115 daily adhkar
   - 13 organized categories for easy navigation
   - Tracking system for consistent practice
   - Light/Dark theme support

   ## 🔐 Security Features
   
   This release includes enterprise-grade protection:
   
   ✅ **R8/ProGuard Obfuscation** - Code minified and unreadable
   ✅ **Code Signing** - APK integrity verified with digital signature  
   ✅ **Backup Prevention** - Data protected from unauthorized access
   ✅ **Anti-Debug Protection** - Prevents reverse engineering attempts
   ✅ **Legal Protection** - Redistribution prohibited

   ## 📱 Installation

   1. Download this APK file
   2. Transfer to your Android device (USB, email, cloud storage)
   3. Enable "Unknown Sources" in Settings → Security
   4. Open the APK and follow installation prompts
   5. Grant necessary permissions when requested

   **Note:** For Android 8.0+, go to Settings → Apps → [Your Browser/File Manager] → Allow from this source

   ## 🔒 Important Security Notice

   ✅ **For Users:**
   - Install ONLY from official GitHub Releases
   - Do NOT share the APK with others
   - Report any unauthorized copies found online

   ❌ **For Developers:**
   - Redistribution of this APK is PROHIBITED
   - Reverse engineering or decompiling is ILLEGAL
   - Unauthorized use may result in legal action

   ## 📞 Support

   - Official Source: https://github.com/barjuos/rafik/releases
   - Documentation: https://github.com/barjuos/rafik/tree/main/docs
   - Contact: dev@rafiq.com

   ⚠️ **Never install APKs from unofficial sources!**

   ---
   
   [Download app-release.apk](app-release.apk)

   ## License

   Web App: MIT (Open Source)  
   Android App: Protected Distribution Only
   
   See LICENSE file for complete terms.
   ```

3. **Drag and drop the APK file** into the "Attach release notes" area, or click to select the file from your Downloads folder

4. Click **"Publish release"**

### Step 3: Verify Release

1. Go to: **https://github.com/barjuos/rafik/releases/tag/v1.0.0**
2. Verify:
   - [ ] APK file is attached
   - [ ] Description is displayed correctly
   - [ ] Download link works
   - [ ] File size shows (e.g., "3.16 MB")

---

## ✅ Post-Release Verification

### For Users (Test Installation):
- [ ] Download APK from release page
- [ ] Install on test Android device
- [ ] Verify all features work:
  - [ ] App launches successfully
  - [ ] All categories accessible
  - [ ] RTL layout displays correctly
  - [ ] Amiri font renders properly
  - [ ] Tracking counts increment
  - [ ] Custom adhkar saves

### For Developers (Security Verification):
- [ ] APK signed with your key (verify signature)
- [ ] Obfuscation applied (code unreadable without decompiler)
- [ ] No debug symbols present
- [ ] Backup prevention active
- [ ] Legal notice visible in app if implemented

---

## 🔄 Next Release

To create another release:

1. **Update version code** in `build.gradle.kts`:
   ```kotlin
   versionCode = 2  // Increment from 1
   versionName = "1.1.0"  // Or next version
   ```

2. **Build new APK:**
   ```powershell
   cd android
   .\gradlew.bat clean assembleRelease
   ```

3. **Create new GitHub release** following steps above with new version tag

4. **Tag previous releases as "outdated"** in repository if needed

---

## 📊 Release Statistics Template

Use this template for future release notes:

```markdown
## Version 1.x.x (Date)

### Features Added
- [Feature 1]
- [Feature 2]

### Bug Fixes
- [Bug 1]
- [Bug 2]

### Security Updates
- Enhanced protection
- Updated obfuscation rules

### Changed
- Improved performance
- Updated dependencies
```

---

## 🛡️ Security Best Practices

### Before Each Release:
- ✅ Test APK on physical device (not just emulator)
- ✅ Verify keystore password is correct
- ✅ Check for any security vulnerabilities in new code
- ✅ Review logs for errors or crashes

### After Each Release:
- ✅ Monitor download statistics
- ✅ Watch for unauthorized copies online
- ✅ Respond to user feedback/issues
- ✅ Document any security incidents

---

## 📞 Support Resources

- **Documentation:** https://github.com/barjuos/rafik/tree/main/docs
- **Security Guide:** https://github.com/barjuos/rafik/blob/main/SECURITY_GUIDE.md
- **Installation Guide:** https://github.com/barjuos/rafik/blob/main/docs/release.md
- **Contact:** dev@rafiq.com

---

## 🎯 Quick Command Reference

### Build Commands:
```powershell
# Clean build
.\gradlew.bat clean

# Debug APK (for testing only)
.\gradlew.bat assembleDebug

# Release APK (production)
.\gradlew.bat assembleRelease

# Full rebuild
.\gradlew.bat clean assembleRelease
```

### Git Commands:
```powershell
# Commit changes
git add .
git commit -m "Release v1.0.0"

# Push to GitHub
git push origin main
```

---

**Ready to release?** Follow the steps above and you'll have a secure, professional release in minutes! 🚀
