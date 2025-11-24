# Comic Viewer - shortcut challenge

A modern Android app for browsing xkcd comics, built with Kotlin and Jetpack Compose.

## Features Implemented (MVP)

 - **Browse Comics** - Navigate through comics with Previous, Next, Latest, and Random buttons 
 - **Comic Details** - View title, image, date, and alt-text for each comic
 - **Favorites** - Save favorite comics with offline access (persisted in Room database)
 - **Search by Number** - Jump directly to any comic by its number
 - **Offline Support** - All viewed comics cached locally, favorites available offline

## Features Deferred to v2

 - **Text Search** - Full-text search across comic titles (would use xkcd search API)
 - **Explanations** - Integration with explainxkcd.com for comic explanations
 - **Share Comics** - Share comic images and links
 - **Push Notifications** - Notify when new comics are published (requires backend)

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM with Clean Architecture
- **Dependency Injection:** Hilt
- **Networking:** Retrofit + Gson
- **Database:** Room
- **Async:** Kotlin Coroutines + Flow
- **Image Loading:** Coil
- **Minimum SDK:** 24 (Android 7.0)

## Architecture

<img src="https://github.com/sofiatroge/Shortcut-challenge/blob/images/Shortcut_challenge_diagram.png" style="display: block; margin-left: auto; margin-right: auto;">  

### Key Architecture Decisions

**MVVM Pattern:**
- Separates UI logic from business logic
- ViewModel survives configuration changes (screen rotation)
- Makes unit testing straightforward

**Repository Pattern:**
- Single source of truth for data
- Abstracts data sources (API vs Database)
- Implements caching strategy (network-first with local fallback)

**Clean Architecture:**
- Domain layer with pure Kotlin models
- Data layer handles API/DB concerns
- UI layer only knows about domain models

**Reactive Programming:**
- StateFlow for UI state management
- Flow for continuous data streams (favorites list)
- Automatic UI updates when data changes

## Project Structure

```
com.shortcut.myapplication/
├── data/
│   ├── local/              # Room database
│   │   ├── ComicDatabase.kt
│   │   ├── ComicDao.kt
│   │   └── ComicEntity.kt
│   ├── remote/             # Retrofit API
│   │   ├── XkcdApi.kt
│   │   └── ComicDto.kt
│   └── ComicRepository.kt  # Data coordination
├── domain/
│   └── Comic.kt            # Domain model
├── ui/
│   ├── comic/              # Main screen
│   ├── favorites/          # Favorites screen
│   └── theme/              # Material 3 theme
├── di/
│   └── AppModule.kt        # Hilt modules
└── MainActivity.kt
```

## Setup Instructions

1. **Clone the repository**
```bash
   git clone https://github.com/sofiatroge/Shortcut-challenge.git
```

2. **Open in Android Studio**
   - Android Studio Hedgehog or newer
   - Gradle will sync automatically

3. **Run the app**
   - Connect device or start emulator
   - Click Run (or Shift + F10)

4. **Requirements**
   - Android 7.0+ (API 24+)
   - Internet connection for first load

## How It Works

### Data Flow Example: Loading a Comic

1. User taps "Next" button
2. `ComicScreen` calls `viewModel.loadNextComic()`
3. `ComicViewModel` updates UI state to Loading
4. `ViewModel` calls `repository.getComic(num)`
5. `Repository` checks local database first
6. If not found, fetches from xkcd API
7. Caches result in Room database
8. Returns domain model to ViewModel
9. `ViewModel` updates StateFlow
10. Compose recomposes UI automatically

### Offline Support

- **Caching:** Every viewed comic is stored in Room
- **Favorites:** Marked as favorite in database, queryable offline
- **Strategy:** Local-first for favorites, network-first for browsing
