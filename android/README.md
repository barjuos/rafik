# RAFIQ — Android app

Native Kotlin / Jetpack Compose port of the RAFIQ PWA (`../src`).

## Features

- 13 adhkar categories (115 entries) bundled offline in `app/src/main/assets/adhkar.json`
- Tap-to-count tracker with a circular progress ring, auto-advance, sound and haptics
- Custom adhkar stored locally with Room
- Light / dark / system theme, persisted with DataStore
- Arabic-only UI, forced RTL, Amiri font
- No `INTERNET` permission: everything is on-device

## Requirements

- Android Studio (Narwhal 3 or newer recommended)
- JDK 17
- Android SDK platform 36 and build-tools 36

## Build and run

```bash
cd android

# Debug build
./gradlew assembleDebug

# Install on a connected device
./gradlew installDebug

# Unit tests
./gradlew testDebugUnitTest

# Release APK (requires keystore.properties, see below)
./gradlew assembleRelease
```

On Windows use `gradlew.bat` instead of `./gradlew`.

Or open the `android/` folder directly in Android Studio and press Run.

## Refreshing the adhkar data

`adhkar.json` is generated from the web app's TypeScript data:

```bash
cd ..            # repository root
npm run android:data
```

The script validates every `(category, id)` pair against `src/data/*.ts` and fails
if the two data sets drift.

## Signing

The release keystore already exists at `android/keystore/rafiq-release.jks`
(alias `rafiq`, RSA 4096, valid until 2054) and its credentials are stored in
the gitignored `android/keystore.properties`:

```properties
storeFile=keystore/rafiq-release.jks
storePassword=...
keyAlias=rafiq
keyPassword=...
```

Export the signed APK:

```bash
./gradlew assembleRelease
# signed:  android/app/build/outputs/apk/release/app-release.apk
# copy:    android/dist/rafiq-1.0.0.apk
```

Verify the signature:

```bash
$ANDROID_HOME/build-tools/36.0.0/apksigner verify --print-certs \
  app/build/outputs/apk/release/app-release.apk
```

**Back up `keystore/rafiq-release.jks` together with its passwords.** Without
both files you can never update an installed copy — Android rejects APKs signed
with a different key, so the app would have to be uninstalled and reinstalled.
Passwords are also written to `keystore/SIGNING-INFO.txt`.

## Installing on a phone

```bash
adb install -r dist/rafiq-1.0.0.apk
```

On Xiaomi/HyperOS you must first unlock the screen and enable
**Developer options → Install via USB** (and usually
**USB debugging (Security settings)**), otherwise `adb` fails with
`INSTALL_FAILED_USER_RESTRICTED: Install canceled by user`.

Alternatively copy `dist/rafiq-1.0.0.apk` to the phone and open it from a file
manager, allowing "Install unknown apps" for that file manager.

## Structure

```
app/src/main/java/com/rafiq/adhkar/
├─ MainActivity.kt          edge-to-edge Compose host, RTL, theme collection
├─ RafiqApp.kt              manual dependency container
├─ data/
│  ├─ AdhkarRepository.kt   parses the bundled JSON asset
│  ├─ db/                   Room database for custom adhkar
│  ├─ model/                Dhikr + category metadata
│  └─ settings/             DataStore theme preference
├─ ui/
│  ├─ home/                 category grid
│  ├─ tracker/              counter screen + pure state machine
│  ├─ adhkar/               custom adhkar screen
│  ├─ components/           Kaaba icon, cards, count ring, theme menu
│  ├─ navigation/           NavHost
│  └─ theme/                colors, typography, Material 3 theme
└─ util/                    SoundPool + Vibration helpers
```
