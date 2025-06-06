# Fighter-Jet-3D

**Fighter-Jet-3D** is a 3D fighter jet dogfight game written in Scala Native. The game simulates a fast-paced aerial battle between a player jet and an AI-controlled bot inside a bounded cubic arena. The project demonstrates low-level 3D graphics programming, basic physics and flight mechanics, and real-time user interaction using SDL2 and OpenGL, all built natively for high performance.

---


https://github.com/user-attachments/assets/50e7d993-5de6-4b33-bb0c-46ced60efbd4


---

## Table of Contents

- [Features](#features)
- [Technical Architecture](#technical-architecture)
  - [Project Structure](#project-structure)
  - [Core Components](#core-components)
  - [Graphics and Rendering](#graphics-and-rendering)
  - [Physics and Game Logic](#physics-and-game-logic)
  - [Controls](#controls)
- [Installation](#installation)
- [How to Play (Beginner Guide)](#how-to-play-beginner-guide)
  - [Basic Controls](#basic-controls)
  - [Flight Tactics](#flight-tactics)
  - [Winning & Losing](#winning--losing)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Native 3D graphics** with OpenGL via Scala Native bindings.
- **Flight simulation**: Realistic jet movement with yaw, pitch, roll, afterburner, and airbrake.
- **Shooting mechanics**: Fire bullets with cooldown, both player and AI can shoot.
- **Chase camera**: 3rd person camera follows your jet dynamically.
- **Simple AI**: Bot jet chases and attacks the player with semi-randomized maneuvers.
- **Collision detection**: Bullets and jets can collide, resulting in win/lose/draw.
- **Minimal UI**: Basic dialog boxes for start/end, and debug messages.

---

## Technical Architecture

### Project Structure

- `build.sbt` - Project configuration, dependencies, and native build settings.
- `com.example.game/` - All core game code.
  - `GameWindow.scala` - Main entry point, handles game loop and events.
  - `FighterJet.scala` - Player jet flight model and input.
  - `BotJet3D.scala` - AI bot logic.
  - `Bullet3D.scala`, `BulletOwner.scala` - Bullet logic and ownership.
  - `Vec3.scala` - 3D vector math utilities.
  - `JetRenderer3D.scala`, `BulletRenderer.scala`, `CubeRenderer.scala` - OpenGL rendering routines.
  - `GL.scala`, `GLConsts.scala`, `GL3D.scala`, `GLU.scala` - OpenGL/GLU Scala Native bindings and helpers.
  - `DialogUI.scala`, `TextRenderer.scala` - Minimal UI rendering.
- **Dependencies:** SDL2, OpenGL, GLU (linked natively).

### Core Components

- **Game Loop:** Runs at ~60 FPS, handles input, updates game state, and triggers rendering.
- **Player Jet:** Simulated with pitch, yaw, roll, thrust, afterburner, and airbrake. Physics is simplified but feels "jet-like."
- **Bot Jet:** AI chooses a direction biased toward the player, with randomized wandering and boundaries.
- **Bullets:** 3D projectiles, fast, with owner tracking and out-of-bounds kill logic.
- **Camera:** Follows player's jet from behind and above, always looking at your jet.

### Graphics and Rendering

- **OpenGL in Scala Native:** All rendering is done via low-level OpenGL calls. No engine or framework.
- **3D Models:** Jets are rendered as simple triangles and quads to form a stylized jet shape.
- **Environment:** A cube (only the bottom face is visible as the "ground"), all play happens within [-1,1] in each axis.
- **UI:** Simple 2D overlays for dialogs and buttons; text rendering is stubbed.

### Physics and Game Logic

- **Movement:** Both jets use physics-based updates each frame, with clamped boundaries.
- **Collisions:** Sphere-based for bullets (radius 0.04) and jets (radius ~0.07-0.11).
- **AI:** Bot picks a new direction every 0.8-1.5 seconds, tends to face the player, can shoot when aligned.

### Controls

| Action                | Key(s)               |
|-----------------------|----------------------|
| Pitch Down (Nose Down)| W / Up Arrow         |
| Pitch Up (Nose Up)    | S / Down Arrow       |
| Roll Left             | A / Left Arrow       |
| Roll Right            | D / Right Arrow      |
| Yaw Left              | Q                    |
| Yaw Right             | E                    |
| Throttle Up           | Shift / Z / +        |
| Throttle Down         | Ctrl / X / -         |
| Toggle Afterburner    | F                    |
| Toggle Airbrake       | B                    |
| Fire Gun              | Space                |
| Exit Game             | Esc                  |

---

## Installation

### Prerequisites

- **Scala Native 0.4+** and **Scala 2.13.x**
- **SDL2**, **OpenGL**, **GLU** libraries installed on your system
- SBT (Scala Build Tool)

### Build & Run

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd Fighter-Jet-3D
   ```
2. **Install SDL2, OpenGL, and GLU**
   - **Linux (Ubuntu/Debian):**
     ```bash
     sudo apt-get install libsdl2-dev libglu1-mesa-dev libgl1-mesa-dev
     ```
   - **MacOS (with Homebrew):**
     ```bash
     brew install sdl2
     ```
   - **Windows:** Use MSYS2 or install prebuilt binaries and ensure they're in PATH.

3. **Build the native binary**
   ```bash
   sbt nativeLink
   ```

4. **Run the game**
   ```bash
   ./target/scala-2.13/fighter-jet-3d-out
   ```

---

## How to Play (Beginner Guide)

### Basic Controls

- **Take Off:** You start mid-air. Use `W`/`Up` to pitch down and accelerate, `S`/`Down` to pitch up. Use `A`/`Left` and `D`/`Right` to roll your jet for banking turns.
- **Steering:** Combine `Q`/`E` (yaw) with roll and pitch for sharp maneuvers.
- **Throttle:** Use `Shift`, `Z`, or `+` to accelerate. Use `Ctrl`, `X`, or `-` to slow down.
- **Afterburner:** Press `F` to toggle afterburner for a speed boost (but harder to control).
- **Airbrake:** Press `B` to toggle airbrake for quick slow-downs.
- **Shoot:** Press and hold `Space` to fire bullets. There's a short cooldown between shots.

### Flight Tactics

- **Stay Moving:** If you slow down too much, you'll be an easy target. Use afterburner to dodge bot fire.
- **Dogfight:** Try to get behind the bot. Use roll plus yaw and pitch to circle around quickly.
- **Shoot Carefully:** Bullets travel fast, but you need to be facing the bot directly. Predict its path and fire as it crosses your sights.
- **Defensive Flying:** If the bot is on your tail, bank hard and use airbrake + roll/yaw to evade.
- **Boundaries:** The game world is a cube; if you hit the edge, your jet will bounce. Use this to your advantage or to recover from attacks.

### Winning & Losing

- **Win:** Hit the bot jet with your bullet. 
- **Lose:** Get hit by the bot's bullet or collide with the bot.
- **Draw:** If you and the bot collide.

---

## Contributing

Contributions are welcome! Please open issues for bugs or feature requests, and submit pull requests for improvements. See `build.sbt` for dependency management.

---

## License

This project is licensed under the MIT License.
