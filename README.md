# NoteFlow - Android Note Taking App

A simple and elegant Android note-taking application built with modern Android development practices.

[![Build & Release](https://github.com/wsdjeg/noteflow-app/actions/workflows/release.yml/badge.svg)](https://github.com/wsdjeg/noteflow-app/actions/workflows/release.yml)

## ✨ Features

- ✨ Create, edit, and delete notes
- 📝 Title and content support
- 📱 Material Design 3 UI
- 💾 Local persistence with Room database
- 🎨 Clean and intuitive interface
- 🕒 Auto-save timestamps

## 📥 Download

| Version | Status | Download |
|---------|--------|----------|
| **Stable** | ![Release](https://img.shields.io/github/v/release/wsdjeg/noteflow-app?include_prereleases) | [Latest Release](https://github.com/wsdjeg/noteflow-app/releases/latest) |
| **Dev** | ![PreRelease](https://img.shields.io/badge/status-prerelease-orange) | [PreRelease APK](https://github.com/wsdjeg/noteflow-app/releases/prerelease) |

> **Note**: PreRelease is automatically built on every push to master branch. It may contain unstable features.

## 🛠 Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| Architecture | MVVM |
| Database | Room 2.6.1 |
| UI | Material Design 1.11.0 |
| Async | Kotlin Coroutines 1.7.3 |
| Build | Gradle 8.2 |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

## 📁 Project Structure

```
app/src/main/java/com/noteflow/app/
├── adapter/           # RecyclerView adapters
│   └── NoteAdapter.kt
├── data/              # Database layer
│   ├── NoteDao.kt
│   └── NoteDatabase.kt
├── model/             # Data models
│   └── Note.kt
├── repository/        # Data repository
│   └── NoteRepository.kt
├── ui/                # Activities
│   ├── MainActivity.kt
│   └── NoteDetailActivity.kt
└── viewmodel/         # ViewModels
    └── NoteViewModel.kt
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1+
- JDK 17
- Android SDK 34

### Build & Run

```bash
# Clone the repository
git clone https://github.com/wsdjeg/noteflow-app.git
cd noteflow-app

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug
```

## 🔄 CI/CD Pipeline

This project uses GitHub Actions for automated builds:

| Trigger | Action |
|---------|--------|
| **Pull Request** | Build verification |
| **Push to master** | Auto PreRelease APK |
| **Tag (v\*)** | Official Release |

### Release Workflow

```
master push → Build APK → Create/Update prerelease tag
tag v*      → Build APK → Create Release with CHANGELOG
```

## 📋 Roadmap

- [ ] Note search functionality
- [ ] Categories/Tags
- [ ] Markdown support
- [ ] Export notes
- [ ] Cloud sync
- [ ] Dark mode
- [ ] Note reminders

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'feat: add amazing feature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Commit Convention

```
feat:     New feature
fix:      Bug fix
docs:     Documentation
style:    Code style
refactor: Refactoring
test:     Tests
chore:    Build/tools
```

## 📄 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- [Android Jetpack](https://developer.android.com/jetpack)
- [Material Design](https://material.io/)
- [Kotlin](https://kotlinlang.org/)
