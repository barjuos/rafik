# 📿 Rafiq - رفيقك في الذكر

**أهلاً بك في "رفيق" - رفيقك اليومي للأذكار.**
يمكنك تحميل تطبيق "رفيق" للأندرويد من خلال صفحة الإصدارات الرسمية على GitHub عبر الرابط التالي:
🔗 [تحميل تطبيق رفيق للأندرويد](https://github.com/barjuos/rafik/releases)

**طريقة التثبيت:**
1. قم بتحميل ملف الـ APK من الرابط أعلاه.
2. قم بتفعيل خيار "التثبيت من مصادر غير معروفة" (Install from Unknown Sources) في إعدادات الأمان بجهازك.
3. افتح ملف الـ APK واتبع التعليمات لإتمام عملية التثبيت.

<div align="center">
  <img src="public/icon-512x512.png" alt="Rafiq Logo" width="180"/>
  
  **رفيق (Rafiq)** — Your Daily Companion for Dhikr & Adhkar
  
  [![GitHub](https://img.shields.io/badge/GitHub-Rafiq-181717.svg?logo=github)](https://github.com)
  [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
  <br/>
  [🌐 Web App](#-web-app) • [📱 Android App](#-android-app) • [📚 Documentation](docs/)
</div>

---

## 🌟 About Rafiq

Rafiq is a dual-platform application designed to help Muslims perform their daily dhikr (remembrances) consistently and beautifully. Built with modern technologies for both web and mobile platforms.

### ✨ Key Features

- ✅ **13 Complete Categories** - Morning, Evening, Before Sleep, and more
- ✅ **115 Authentic Adhkar** - Sourced from Sahih Bukhari, Muslim, Tirmidhi, and Abu Dawud
- ✅ **Daily Tracker** - Intuitive touch-based counting system
- ✅ **Custom Adhkar** - Add your personal reminders
- ✅ **Theme Support** - Light, Dark, and System themes
- ✅ **100% Native Android** - No WebView, pure Jetpack Compose

---

## 📥 Quick Links

### Download
- **[Download Android APK](https://github.com/barjuos/rafik/releases/latest)** (Latest Release)
- **[View All Releases](https://github.com/barjuos/rafik/releases)** (History & Updates)

### Documentation
- **[Android Installation Guide](docs/release.md)** - Step-by-step setup
- **[Full Documentation](docs/)** - Complete user manual

## 🌐 Web App

A modern Next.js web application with beautiful Arabic typography.

### Tech Stack

| Technology | Version |
|------------|---------|
| Framework | Next.js 15 |
| Language | TypeScript |
| Styling | Tailwind CSS |
| UI Components | Radix UI |
| State Management | React Context + Zustand |

### Run Locally

```bash
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

### Features

- 🎨 **Beautiful RTL Design** - Arabic-first interface with proper Right-to-Left layout
- 🔤 **Amiri Font** - Optimized typography for authentic Arabic script rendering
- 💾 **Offline Support** - PWA-ready with service workers
- 📱 **Responsive** - Works on desktop and mobile browsers

---

## 📱 Android App

A beautiful native Android application built with Jetpack Compose.

### Download

🔥 **Download Latest APK:** [Rafiq v1.0.0 (3.1 MB)](https://github.com/barjuos/rafik/releases/latest)

Or visit our **[GitHub Releases](https://github.com/barjuos/rafik/releases)** for all versions.

### Installation

#### From GitHub Releases (Recommended)
1. Download the APK from [Releases page](https://github.com/barjuos/rafik/releases)
2. Enable "Install from Unknown Sources" in your device settings
3. Open the APK file and follow installation prompts

#### Build Locally (For Development)
```bash
# Build release APK with security features
cd android
./gradlew assembleRelease

# Install to device
adb install app/build/outputs/apk/release/app-release.apk
```

### 🛡️ Security Features
- ✅ **Code Obfuscation** - R8/ProGuard protection against reverse engineering
- ✅ **Disabled Backups** - Prevents data theft via `allowBackup="false"`
- ✅ **Minified Resources** - Smaller APK, harder to analyze
- ✅ **No Debug Symbols** - Secure production build

### Features
- 🏃 **Fast & Native** - No WebView, pure Kotlin + Jetpack Compose
- 📊 **Room Database** - Persistent offline storage
- 🔔 **Haptic Feedback** - Tactile response on each tap
- 🔊 **Custom Sounds** - Adjustable audio feedback
- 🌙 **System Integration** - Theme follows device settings

### Screenshots

<p align="center">
  <em>Coming soon - Add screenshots of your app here</em>
</p>

---

## 📁 Project Structure

```
RAFIQ/
├── 📱 android/                    # Native Android App (Kotlin + Jetpack Compose)
│   ├── app/src/main/java/com/rafiq/adhkar/
│   │   ├── data/                  # Room Database & Models
│   │   ├── ui/                    # Jetpack Compose Screens
│   │   │   ├── home/             # Home Screen
│   │   │   ├── tracker/          # Tracker Screen (13 categories)
│   │   │   ├── adhkar/           # My Adhkar Screen
│   │   │   └── components/       # Reusable Components
│   │   ├── navigation/            # Navigation Graph
│   │   └── util/                  # Haptics, SoundPlayer
│   └── build.gradle.kts
│
├── 🌐 src/                        # Web App (Next.js 15)
│   ├── app/                       # Pages & Layouts
│   ├── components/                # React Components
│   ├── data/                      # Adhkar Content (115 adhkar)
│   │   ├── adhkar.ts              # Complete adhkar database
│   │   ├── android_app_data.json  # Android app sync data
│   │   └── rizq.ts                # Rizq category data
│   └── styles/                    # Tailwind Config
│
├── 🌍 public/                     # Static Assets
│   ├── manifest.json              # PWA Manifest
│   ├── icons/                     # App Icons (192x192, 512x512)
│   └── sounds/                    # Audio Files
│
├── 📚 docs/                       # Documentation (GitHub Pages)
│   └── index.md                   # Main Documentation
│
└── 📦 scripts/                    # Build & Export Scripts
```

---

## 🛠 Development Setup

### Prerequisites

- **Node.js** 20+
- **Android Studio** (for Android development)
- **Android SDK** with Gradle

### Environment Variables

Create a `.env.local` file:

```bash
NEXT_PUBLIC_APP_NAME="Rafiq"
NEXT_PUBLIC_VERSION="1.0.0"
```

---

## 📦 Content

All 115 adhkar are carefully sourced from authentic Islamic references:

| Source | Categories | Count |
|--------|-----------|-------|
| Sahih Bukhari | Multiple | ~50 |
| Sahih Muslim | Multiple | ~40 |
| Sunan Abi Dawud | Various | ~15 |
| Al-Tirmidhi | Selected | ~10 |

Each dhikr includes:
- Arabic text (بني عروية/سعيد بن جبير) - authentic narration chains
- Descriptions when available
- References to original sources

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Report Bugs** - [Create an issue](https://github.com/barjuos/rafik/issues)
2. **Feature Requests** - Suggest improvements via issues
3. **Pull Requests** - Submit code changes
4. **Documentation** - Improve the docs at `/docs`

### Development Workflow

```bash
# Clone repository
git clone https://github.com/barjuos/rafik.git
cd rafik

# Web development
npm install
npm run dev

# Android development (requires Android Studio)
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 📄 License

### Web App
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Android App
The Android APK is protected against unauthorized redistribution and reverse engineering. Please respect copyright and license terms:
- ✅ You may install the APK on your personal device
- ❌ You may NOT redistribute the APK without permission
- ❌ You may NOT decompile or reverse engineer the code
- ✅ You may report bugs and suggest features

```
MIT License

Copyright (c) 2025 Rafiq Development

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

---
Android App Protection Notice:
The Android application is protected against unauthorized copying, redistribution, 
and reverse engineering. Please respect the developer's work and do not share APKs 
without explicit permission. This app is for personal use only.
```

---

## 🙏 Acknowledgments

- **Amiri Font** - Beautiful Arabic typography
- **Radix UI** - Accessible component library
- **Next.js Team** - Modern React framework
- **Jetpack Compose** - Declarative Android UI
- **Community Contributors** - Open source libraries and tools

---

<div align="center">
  <h3 style="margin-top: 40px;">🌟 Made with ❤️ for Muslim Communities</h3>
  <p><strong>رفيق (Rafiq)</strong> — رفيقك في الذكر</p>
  
  <div style="margin-top: 30px;">
    <a href="docs/" style="display: inline-block; margin: 10px; padding: 12px 24px; background: #0066CC; color: white; border-radius: 8px; text-decoration: none;">📚 Documentation</a>
    <a href="#android-app" style="display: inline-block; margin: 10px; padding: 12px 24px; background: #3DDC84; color: white; border-radius: 8px; text-decoration: none;">📱 Android App</a>
    <a href="#web-app" style="display: inline-block; margin: 10px; padding: 12px 24px; background: #6366F1; color: white; border-radius: 8px; text-decoration: none;">🌐 Web App</a>
  </div>
</div>

---

## 🔗 Links

- [GitHub Repository](https://github.com/barjuos/rafik)
- [Issue Tracker](https://github.com/barjuos/rafik/issues)
- [Documentation](docs/)
- [Android App Guide](android/README.md) (coming soon)
