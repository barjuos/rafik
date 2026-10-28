# 🔐 Secure Android App Setup - Step by Step

This guide will implement **maximum security** for your Rafiq Android app while allowing distribution through GitHub Releases.

---

## 📋 What This Guide Does:

1. ✅ Creates custom keystore for APK signing
2. ✅ Configures release build with security features  
3. ✅ Implements runtime license checking
4. ✅ Adds anti-tamper protection
5. ✅ Sets up legal protection
6. ✅ Prepares for secure release

---

## 🚀 Step-by-Step Instructions

### Step 1: Generate Custom Keystore 🔐

**⚠️ IMPORTANT:** Do this ONCE. Keep the keystore file and passwords SAFE!

1. Open PowerShell in your project folder
2. Run the command below (replace `YourStrongPassword123` with a strong password you'll remember):

```powershell
cd C:\Users\ibrah\Downloads\RAFIK\android

# Generate keystore - run ONCE only!
$keytool = "C:\Program Files\Android\Android Studio\jbr\bin\keytool.bat"
& $keytool -genkey -v `
  -keystore C:\Users\ibrah\Downloads\RAFIK\android\rafik-release.keystore `
  -alias rafik-release `
  -keyalg RSA `
  -keysize 2048 `
  -validity 10000 `
  -storepass YOUR_STRONG_PASSWORD_HERE_!@# $null `
  -keypass YOUR_STRONG_PASSWORD_HERE_!@# $null `
  -dname "CN=Rafiq Developer, OU=Mobile Dev, O=Rafiq App, L=City, ST=State, C=US"
```

**Better approach - using a text editor:**

1. Create this file: `create-keystore.bat` in `android/` folder:

```batch
@echo off
echo ====================================================================
echo Creating Rafiq Release Keystore
echo ====================================================================
echo.
set KEYTOOL="C:\Program Files\Android\Android Studio\jbr\bin\keytool.bat"
set KEystore_FILE=rafik-release.keystore
set ALIAS=rafik-release
set KEY_SIZE=2048
set VALIDITY=10000

echo Enter your keystore password (minimum 8 characters):
set /p STORE_PASSWORD=
echo Re-enter password to confirm:
set /p CONFIRM_PASSWORD=

if /i "%STORE_PASSWORD%" neq "%CONFIRM_PASSWORD%" goto ERROR_PWD
if not "%STORE_PASSWORD%"=="" goto GENERATE_KEY
goto :EOF

:ERROR_PWD
echo Passwords don't match! Please try again.
pause
exit /b 1

:GENERATE_KEY
echo Generating keystore...
& %KEYTOOL% -genkey -v ^
  -keystore %KEystore_FILE% ^
  -alias %ALIAS% ^
  -keyalg RSA ^
  -keysize %KEY_SIZE% ^
  -validity %VALIDITY% ^
  -storepass "%STORE_PASSWORD%" ^
  -keypass "%STORE_PASSWORD%" ^
  -dname "CN=Rafiq Developer, OU=Mobile Dev, O=Rafiq App, L=City, ST=State, C=US"

if errorlevel 1 (
    echo ERROR: Keystore generation failed!
) else (
    echo.
    echo ✓ SUCCESS! Keystore created at: android\rafik-release.keystore
    echo.
    echo ⚠️ IMPORTANT - Write down your password and store it safely!
    echo Password used: %STORE_PASSWORD%
    echo Keystore file: android\rafik-release.keystore
    echo.
    echo NEVER commit these to Git:
    echo   - android\rafik-release.keystore
    echo   - android\keystore.properties
    echo.
)

pause
```

2. Save as `android\create-keystore.bat`
3. Double-click it and follow the prompts

**Alternative - Manual Creation:**

Open `keytool` directly:

```powershell
# Navigate to Android Studio's JDK bin folder
cd "C:\Program Files\Android\Android Studio\jbr\bin"

# Run keytool
.\keytool.bat -genkey -v ^
  -keystore C:\Users\ibrah\Downloads\RAFIK\android\rafik-release.keystore ^
  -alias rafik-release ^
  -keyalg RSA ^
  -keysize 2048 ^
  -validity 10000 ^
  -dname "CN=Your Name, OU=Developer, O=Rafiq, L=City, ST=State, C=US"

# When prompted:
#   First and last name → Your Name
#   Organizational unit → Mobile Development
#   Organizational name → Rafiq App
#   City → Your City
#   State → Your State  
#   Country code → US (or your country)
```

**Record these values:**
- Keystore password: [YOUR PASSWORD]
- Key alias: `rafik-release`
- Keystore path: `C:\Users\ibrah\Downloads\RAFIK\android\rafik-release.keystore`

---

### Step 2: Configure Build Files 📝

#### Update `android/app/build.gradle.kts`:

<tool_call>
<function=read>
<parameter=path>
C:\Users\ibrah\Downloads\RAFIQ\android\app\build.gradle.kts