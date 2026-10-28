# 📿 Rafiq - أذكار رفيق

<div style="text-align: center; margin: 40px 0;">
  <a href="https://github.com/barjuos/rafik" target="_blank">
    <img src="/icon-512x512.png" alt="Rafiq Logo" style="width: 180px; height: 180px; border-radius: 50%; box-shadow: 0 4px 20px rgba(0,0,0,0.1);"/>
  </a>
  <p><strong>Rafiq (رفيق)</strong> — Your Daily Companion for Dhikr & Adhkar</p>
  <div style="display: flex; gap: 20px; justify-content: center; margin-top: 20px;">
    <a href="#web-app" style="padding: 15px 30px; background: #0066CC; color: white; border-radius: 8px; text-decoration: none; font-weight: bold;">🌐 Web App</a>
    <a href="#android-app" style="padding: 15px 30px; background: #3DDC84; color: white; border-radius: 8px; text-decoration: none; font-weight: bold;">📱 Android App</a>
  </div>
</div>

---

## 🌟 Features

### **13 Complete Categories**
- Morning (الصباح) - 30 adhkar
- Evening (المساء) - 37 adhkar  
- Before Sleep (قبل النوم) - 26 adhkar
- And 10 more essential categories...

### **Total: 115 Adhkar**
Authentically sourced from the most trusted Islamic references.

### 🎯 Daily Tracker
Track your progress with intuitive touch-based counting and persistent state management.

### ✨ Custom Adhkar
Add your personal reminders to the app.

### 🌗 Theme Support
Day, Dark, and System themes for any time of day.

---

## 🌐 Web App

A modern Next.js application built with:
- **Next.js 15** with React Server Components
- **TypeScript** for type safety
- **Tailwind CSS** for responsive design
- **Arabic-first RTL** layout
- **Amiri Font** for beautiful Arabic typography

### Tech Stack
```json
{
  "framework": "Next.js 15",
  "language": "TypeScript",
  "styling": "Tailwind CSS",
  "state-management": "React Context + Zustand",
  "persistence": "LocalStorage"
}
```

### Live Demo
[View Web App](#) *(coming soon)*

---

## 📱 Android App

A **native Jetpack Compose** application built with:

### Architecture
- **Kotlin** native development
- **Jetpack Compose** for modern UI
- **MVVM Architecture** with clean separation
- **Room Database** for offline persistence

### Features
- **100% Native Performance** - No WebView, no wrappers
- **Beautiful Typography** - Amiri font optimized for Arabic script
- **Forced RTL Layout** - True Right-to-Left support
- **Touch-based Counting** - Intuitive one-tap tracking
- **Offline First** - All data stored locally

### Installation
Download the release APK from GitHub Releases or install directly:
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

---

## 📊 Project Structure

```
RAFIQ/
├── android/                 # Native Android App (Jetpack Compose)
│   ├── app/src/main/
│   │   ├── java/com/rafiq/adhkar/
│   │   │   ├── data/       # Room DB, Models
│   │   │   ├── ui/         # Jetpack Compose screens
│   │   │   ├── util/       # Haptics, Sound
│   │   │   └── navigation/ # Nav graph
│   │   └── res/            # Resources
│   └── build.gradle.kts
│
├── src/                     # Web App (Next.js)
│   ├── app/                 # Next.js App Router pages
│   ├── components/          # React components
│   ├── data/                # Adhkar content
│   └── styles/              # Tailwind config
│
├── public/                  # Static assets
│   ├── manifest.json        # PWA manifest
│   └── icons/               # App icons
│
└── docs/                    # Documentation (GitHub Pages)
```

---

## 🛠 Development

### Web App
```bash
npm run dev          # Start development server
npm run build        # Build for production
npm run start        # Start production server
```

### Android App
```bash
# Install dependencies
./gradlew dependencies

# Build release APK
./gradlew assembleRelease

# Debug apk
./gradlew assembleDebug
```

---

## 📝 Content Source

All 115 adhkar are sourced from authentic Islamic references including:
- Sahih Bukhari
- Sahih Muslim  
- Sunan Abi Dawud
- Al-Tirmidhi

References are included for each dhikr where available.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is open source and available under the MIT License.

---

## 👤 Author

**Rafiq Development Team**  
Built with ❤️ for Muslim communities worldwide

---

<div style="text-align: center; margin-top: 60px;">
  <h3>Made with 💚 for Dhikr</h3>
  <p>رفيق - رفيقك في الذكر</p>
</div>
