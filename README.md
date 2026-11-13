# 🎲 Ludo King Replica (Experimental / Failed Attempt)

> [!WARNING]
> ### ⚠️ Project Status: Archived (Failed Asset Mimicry Experiment)
> This repository documents an experimental reverse-engineering and recreation attempt of the popular mobile game **Ludo King** built with **Android Jetpack Compose** and **HTML5 Canvas**.
>
> **Why this project is marked as failed:**
> The primary objective was to achieve a pixel-perfect, 1:1 mimicry of the authentic Ludo King in-game visuals and UI. However, this attempt failed to accurately mimic the exact official assets:
> - **Incomplete 2D Texture Atlases:** Assets extracted from the official Unity bundles (`UnityDataAssetPack.apk`) via `UnityPy` yielded separated, low-resolution texture atlases (`tex_642_512x512.png`), cropped sprite fragments, and static dice frames rather than the integrated high-fidelity game visuals.
> - **Lack of 3D Engine & Lighting:** The original Ludo King runs on Unity 3D with dynamic 3D dice physics, specular lighting, bounce animations, and custom shaders. Attempting to render the board and dice with 2D Canvas and Compose primitives resulted in a flat, unfaithful presentation that did not capture the genuine game feel.
> - **Asset Layout & Scaling Discrepancies:** Pawn proportions, path coordinates, and theme styling could not seamlessly align with the extracted raw board assets without distortion.

---

## 📖 Overview

Despite the visual fidelity shortfall, this repository contains a complete working logic prototype implemented across two environments:
1. **Android App (Kotlin + Jetpack Compose)**: Full local turn-based game engine, player state management, custom Canvas board renderer, and rigged dice system.
2. **Web Companion / Preview (HTML5 Canvas + Vanilla JS)**: Quick browser-based emulator in `preview/index.html` replicating game screens and board flows.
3. **Asset Extraction Pipeline (Python)**: Scripts under `scripts/` used to unpack the official APK/XAPK, scan Unity assets, and parse textures/audio clips.

---

## 🛠️ Features Implemented

- **Local Pass 'N Play Multiplayer:** Supports 2 to 4 players (Red, Green, Yellow, Blue).
- **Full Ludo Game Engine (`LudoEngine.kt`):**
  - Accurate path step tracking and coordinate mapping.
  - Safe zones / Star squares where tokens cannot be captured.
  - Token capture mechanics (sends opponent pawn back to yard).
  - Home stretch and victory detection.
- **Rigged Dice System (`RiggedDiceController.kt`):**
  - Configurable RNG biasing (e.g., Red player advantage or controlled roll probabilities).
- **Screens & UI (Jetpack Compose):**
  - `HomeScreen`: Mode selection (Pass 'N Play, Computer, etc.).
  - `SelectGameScreen`: Game format options.
  - `PlayerSetupScreen`: Selecting colors, player names, and token count.
  - `GameBoardScreen`: Interactive game canvas with dice roll triggers and winner celebrations.
- **Web Simulation (`preview/index.html`):**
  - Interactive HTML5 board with pawn animations and responsive phone frame preview.

---

## 📂 Repository Structure

```
LudoApp/
├── app/
│   ├── src/main/java/com/ludoking/game/
│   │   ├── engine/             # Ludo game logic, board math & rigged dice
│   │   │   ├── LudoBoardMath.kt
│   │   │   ├── LudoEngine.kt
│   │   │   └── RiggedDiceController.kt
│   │   ├── model/              # State models (GameState, Player, Token, etc.)
│   │   ├── ui/
│   │   │   ├── components/     # Canvas board renderer & dice components
│   │   │   ├── screens/        # Jetpack Compose UI screens
│   │   │   └── theme/          # App themes, typography, colors
│   │   ├── viewmodel/          # State management (LudoViewModel)
│   │   └── MainActivity.kt
│   └── src/main/res/           # Extracted drawable assets & manifests
├── preview/
│   ├── index.html              # Standalone web emulator / preview
│   └── official_assets/        # Extracted texture samples (board, dice, pawns)
├── scripts/                    # Python reverse-engineering & asset extraction tools
│   ├── download_ludo.py        # Fetches official XAPK from APKPure
│   ├── extract_assets.py       # Extracts Unity textures & audio via UnityPy
│   ├── extract_dice.py         # Parses dice frame sequences
│   ├── assemble_official.py    # Maps extracted textures into app drawables
│   ├── check_sprites.py        # Validates sprite dimensions and transparency
│   ├── find_core_assets.py     # Locates specific board & UI components
│   └── inspect_metadata.py     # Inspects asset metadata and headers
├── gradle/                     # Gradle wrapper & version catalog (libs.versions.toml)
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🔬 Asset Extraction Post-Mortem

The `scripts/` directory documents the reverse-engineering methodology attempted:

1. **Extraction Attempt:**
   - Downloaded official `LudoKing_Official.xapk` using `scripts/download_ludo.py`.
   - Used `UnityPy` in `scripts/extract_assets.py` to extract `Texture2D` and `Sprite` objects from `UnityDataAssetPack.apk`.
2. **Why Mimicry Failed:**
   - Raw Unity assets are stored as split texture blocks and UV-mapped atlases rather than ready-to-use full board images.
   - The original dice is rendered dynamically with 3D mesh rendering in Unity; 2D frame extractions produced inconsistent perspectives and lighting seams.
   - UI chrome and buttons rely on Unity 9-slice sprites that lost their slicing metadata when dumped as raw PNGs.

---

## 🚀 Building & Running

### Android Application
- **Requirements:** Android Studio Jellyfish / Ladybug or newer, JDK 17+, Android SDK 34+.
- Open the root folder in Android Studio.
- Sync Gradle and run on an Android device or emulator (API 24+).
- Command-line build:
  ```bash
  ./gradlew assembleDebug
  ```

### Web Preview
- Open `preview/index.html` directly in any modern web browser or serve with a local HTTP server:
  ```bash
  cd preview
  python -m http.server 8000
  ```
  Navigate to `http://localhost:8000`.

---

## 📄 License & Disclaimer

This project is created strictly for **educational and research purposes** exploring game engine logic, Compose Canvas rendering, and Unity asset structures. It is not affiliated with, endorsed by, or sponsored by Gametion Technologies or the creators of Ludo King.
