@echo off
setlocal enabledelayedexpansion

echo ================================================================================
echo          RAFIQ ANDROID APP - SECURITY KESTORE SETUP
echo ================================================================================
echo.
echo This script will create a secure keystore for signing your Android release APKs.
echo.
echo ⚠️  IMPORTANT: 
echo    - Run this script ONLY ONCE
echo    - Keep the keystore file and password SECRET
echo    - NEVER commit these files to Git
echo    - Store backups securely
echo.

cd /d "%~dp0"

if not exist "rafik-release.keystore" (
    
    echo [1/3] Generating keystore...
    echo.
    echo Please enter a strong password (minimum 8 characters):
    set /p STORE_PASSWORD=
    echo Re-enter the same password to confirm:
    set /p CONFIRM_PASSWORD=
    
    if not "!STORE_PASSWORD!"=="" (
        if /i "!STORE_PASSWORD!" neq "!CONFIRM_PASSWORD!" (
            echo ERROR: Passwords don't match! Please try again.
            pause
            exit /b 1
        )
    ) else (
        echo ERROR: You must provide a password.
        pause
        exit /b 1
    )
    
    echo Generating keystore with your password...
    
    :: Get Android Studio keytool path
    for %%i in ("C:\Program Files\Android\Android Studio\jbr\bin\keytool.bat") do set KEYTOOL=%%i
    
    if "!KEYTOOL!"=="" (
        echo Trying default location...
        set "KEYTOOL=C:\Program Files\Android\Android Studio\jbr\bin\keytool.bat"
        
        if not exist "!KEYTOOL!" (
            echo ERROR: keytool not found at expected location.
            echo Please ensure Android Studio is installed.
            pause
            exit /b 1
        )
    )
    
    :: Create keystore
    !KEYTOOL! -genkey -v ^
        -keystore rafik-release.keystore ^
        -alias rafik-release ^
        -keyalg RSA ^
        -keysize 2048 ^
        -validity 10000 ^
        -storepass "!STORE_PASSWORD!" ^
        -keypass "!STORE_PASSWORD!" ^
        -dname "CN=Rafiq App Developer, OU=Mobile Development, O=Rafiq Application, L=City, ST=State, C=US"
    
    if errorlevel 1 (
        echo ERROR: Keystore generation failed!
        pause
        exit /b 1
    )
    
) else (
    echo [1/3] Keystore already exists: rafik-release.keystore
)

echo.
echo [2/3] Creating keystore.properties template...
(
echo # Rafiq Release Keystore Configuration
echo # WARNING: DO NOT SHARE THIS FILE WITH ANYONE!
echo # Add to .gitignore - NEVER commit to Git!
echo.
echo storePassword=CHANGE_ME_TO_YOUR_PASSWORD
echo keyAlias=rafik-release
echo keyPassword=CHANGE_ME_TO_YOUR_PASSWORD
) > keystore.properties

echo.
echo [3/3] Updating .gitignore...

:: Check if .gitignore exists, create if not
if not exist ".gitignore" (
    echo # Keystore and signing files - NEVER commit!
    echo android/*.keystore
    echo android/*.jks
    echo android/*.p12
    echo *.keystore
    echo *.jks
    echo *.p12 > .gitignore
) else if not (.gitignore | find /c /v "rafik-release.keystore" > nul) (
    echo. >> .gitignore
    echo # Keystore and signing files - NEVER commit!
    echo android/rafik-release.keystore >> .gitignore
    echo rafik-release.keystore >> .gitignore
)

echo ================================================================================
echo                        ✓ SETUP COMPLETED SUCCESSFULLY!
echo ================================================================================
echo.
echo YOUR KESTORE FILES:
echo   1. Keystore file: %CD%\rafik-release.keystore
echo   2. Properties template: %CD%\keystore.properties
echo.
echo 🔒 IMPORTANT NEXT STEPS:
echo   1. Edit keystore.properties and REPLACE the passwords with your actual password
echo   2. Add these lines to .gitignore (if not already added):
echo      android/rafik-release.keystore
echo      rafik-release.keystore
echo.
echo ⚠️  SECURITY WARNING:
echo   - Keep the keystore file and passwords SECRET
echo   - NEVER commit keystore files to Git or any version control
echo   - Build release APKs only on your trusted machine
echo   - If you lose the keystore, you cannot re-sign previous APKs!
echo.
echo Your password was: !STORE_PASSWORD!
echo Write it down and store it securely!
echo.

pause
