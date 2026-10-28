# Rafiq Android App - Secure Keystore Setup Script
# ⚠️ WARNING: NEVER run this on a shared/public computer!

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "          RAFIQ ANDROID APP - SECURITY KESTORE SETUP" -ForegroundColor Yellow
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

$CurrentDir = Get-Location
Write-Host "Working directory: $CurrentDir" -ForegroundColor Green
Write-Host ""

# Check if keystore already exists
if (Test-Path ".\rafik-release.keystore") {
    Write-Host "[1/3] Keystore already exists: rafik-release.keystore" -ForegroundColor Yellow
} else {
    Write-Host "[1/3] Generating keystore..." -ForegroundColor Yellow
    Write-Host ""
    
    # Get password from user
    Write-Host "Please enter a strong password (minimum 8 characters):" -ForegroundColor White
    $StorePassword = Read-Host
    
    Write-Host "Re-enter the same password to confirm:" -ForegroundColor White
    $ConfirmPassword = Read-Host
    
    if ([string]::IsNullOrWhiteSpace($StorePassword)) {
        Write-Host "ERROR: You must provide a password." -ForegroundColor Red
        pause
        exit 1
    }
    
    if ($StorePassword -ne $ConfirmPassword) {
        Write-Host "ERROR: Passwords don't match! Please try again." -ForegroundColor Red
        pause
        exit 1
    }
    
    Write-Host "Generating keystore with your password..." -ForegroundColor Cyan
    
    # Get Android Studio keytool path
    $AndroidStudioPath = "C:\Program Files\Android\Android Studio"
    $KeytoolPath = "$AndroidStudioPath\jbr\bin\keytool.bat"
    
    if (-not (Test-Path $KeytoolPath)) {
        Write-Host "ERROR: keytool not found at expected location." -ForegroundColor Red
        Write-Host "Please ensure Android Studio is installed." -ForegroundColor Red
        pause
        exit 1
    }
    
    # Create keystore
    Write-Host "`nCreating keystore..." -ForegroundColor Cyan
    & $KeytoolPath -genkey -v `
        -keystore rafik-release.keystore `
        -alias rafik-release `
        -keyalg RSA `
        -keysize 2048 `
        -validity 10000 `
        -storepass "$StorePassword" `
        -keypass "$StorePassword" `
        -dname "CN=Rafiq App Developer, OU=Mobile Development, O=Rafiq Application, L=City, ST=State, C=US"
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "ERROR: Keystore generation failed!" -ForegroundColor Red
        pause
        exit 1
    }
}

Write-Host "`n[2/3] Creating keystore.properties template..." -ForegroundColor Yellow

# Create keystore.properties template
$KeystoreProps = @'
# Rafiq Release Keystore Configuration
# WARNING: DO NOT SHARE THIS FILE WITH ANYONE!
# Add to .gitignore - NEVER commit to Git!

storePassword=CHANGE_ME_TO_YOUR_PASSWORD
keyAlias=rafik-release
keyPassword=CHANGE_ME_TO_YOUR_PASSWORD
'@

$KeystoreProps | Out-File -FilePath ".\keystore.properties" -Encoding UTF8

Write-Host "Created: keystore.properties" -ForegroundColor Green
Write-Host ""

# Update .gitignore
Write-Host "[3/3] Updating .gitignore..." -ForegroundColor Yellow

$GitignorePath = ".gitignore"

if (-not (Test-Path $GitignorePath)) {
    Write-Host "Creating new .gitignore..." -ForegroundColor Cyan
    
    $IgnoreContent = @"
# Keystore and signing files - NEVER commit!
android/*.keystore
android/*.jks
android/*.p12
*.keystore
*.jks
*.p12
"@
    $IgnoreContent | Out-File -FilePath $GitignorePath -Encoding UTF8
} else {
    # Check if keystore entries already exist
    $Content = Get-Content $GitignorePath -Raw
    
    if ($Content -notmatch "rafik-release\.keystore") {
        Write-Host "Adding keystore entries..." -ForegroundColor Cyan
        
        $NewContent = $Content + "`n# Keystore and signing files - NEVER commit!`nandroid/rafik-release.keystore`nrafik-release.keystore`n"
        $NewContent | Out-File -FilePath $GitignorePath -Encoding UTF8
        
        Write-Host "Updated .gitignore with keystore exclusions." -ForegroundColor Green
    } else {
        Write-Host ".gitignore already has keystore exclusions." -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "                        ✓ SETUP COMPLETED SUCCESSFULLY!" -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "YOUR KESTORE FILES:" -ForegroundColor Cyan
Write-Host "  1. Keystore file: $(Get-Location)\rafik-release.keystore" -ForegroundColor Yellow
Write-Host "  2. Properties template: $(Get-Location)\keystore.properties" -ForegroundColor Yellow
Write-Host ""

Write-Host "🔒 IMPORTANT NEXT STEPS:" -ForegroundColor Cyan
Write-Host "  1. Edit keystore.properties and REPLACE the passwords with your actual password" -ForegroundColor White
Write-Host "  2. Add these lines to .gitignore (if not already added):" -ForegroundColor White
Write-Host "     android/rafik-release.keystore" -ForegroundColor Gray
Write-Host "     rafik-release.keystore" -ForegroundColor Gray
Write-Host ""

Write-Host "⚠️  SECURITY WARNING:" -ForegroundColor Cyan
Write-Host "   - Keep the keystore file and passwords SECRET" -ForegroundColor White
Write-Host "   - NEVER commit keystore files to Git or any version control" -ForegroundColor White
Write-Host "   - Build release APKs only on your trusted machine" -ForegroundColor White
Write-Host "   - If you lose the keystore, you cannot re-sign previous APKs!" -ForegroundColor White
Write-Host ""

Write-Host "`nScript completed successfully." -ForegroundColor Green
pause
