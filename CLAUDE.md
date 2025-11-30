# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SimulationRunner - A 2D grid-based game where a player entity moves around a grid collecting keys. Built with JavaFX 25.0.1 and Java 25.

**Current Implementation Status:**
- Player movement with WASD controls
- Key collection system with rainbow-colored keys
- Player inventory system tracking collected key colors
- Door mechanic with color-based key requirement
- HUD footer showing collected keys with color coding
- Entity inheritance hierarchy (Player, Key, Door extend Entity)
- Grid rendering with configurable dimensions
- Boundary detection and collision prevention
- **Planned Features:** Level system, win/lose conditions

## Development Commands

### Prerequisites
- Java 25 (use SDKMAN: `sdk install java 25-open`)
- Uses Maven wrapper (no separate Maven installation needed)

### Build and Test
**Unix/Linux/macOS:**
```bash
./mvnw clean test        # Run all tests
./mvnw clean package     # Build the project
./mvnw javafx:run        # Run the JavaFX application
```

**Windows:**
```cmd
mvnw.cmd clean test      # Run all tests
mvnw.cmd clean package   # Build the project
mvnw.cmd javafx:run      # Run the JavaFX application
```

### Running Single Tests
**Unix/Linux/macOS:**
```bash
./mvnw test -Dtest=GridConfigTest              # Run specific test class
./mvnw test -Dtest=GridConfigTest#testMethod   # Run specific test method
```

**Windows:**
```cmd
mvnw.cmd test -Dtest=GridConfigTest              # Run specific test class
mvnw.cmd test -Dtest=GridConfigTest#testMethod   # Run specific test method
```

## Architecture

### Technology Stack
- **Java 25** with modern Java features
- **JavaFX 25.0.1** for rendering the grid-based UI
- **JUnit 5.11.4** for testing
- **Maven** as build system (via wrapper)

### Coordinate System
- Grid uses top-left origin (0,0)
- X-axis increases going right
- Y-axis increases going down
- Grid dimensions are configurable (default: 10x10 cells)
- Cell size is configurable in pixels (default: 10px)

### Core Components
- **App** (`com.simulationrunner.App`): Main JavaFX application entry point. Creates 10x10 grid with 50px cells and 1 key. Handles WASD keyboard input, delegates to player movement with door collision checking, checks for key collection after each move, and triggers manual render updates (not a continuous game loop).
- **Grid** (`com.simulationrunner.Grid`): Represents the game grid, wraps GridConfig with validation. Creates and manages Player via `Player.createRandom()`, Keys via `Key.createRandomKeys()`, and Door via `Door.createRandom()`. Constructor takes keyCount parameter. Door color matches first key's color (null if no keys).
- **GridConfig** (`com.simulationrunner.config.GridConfig`): Immutable configuration class for grid dimensions (gridWidth, gridHeight) and cellSize. Provides computed pixel dimensions via `getPixelWidth()`, `getPixelHeight()`, and `getPixelHeightWithHUD(hudHeight)`. Default constructor creates 10x10 grid with 10px cells.
- **Entity** (`com.simulationrunner.entity.Entity`): Abstract base class for all game entities. Defines protected gridX/gridY position fields, abstract `render(GraphicsContext, GridConfig)` method, and utility `manhattanDistance()` for spawn distance calculations. Implements equals(), hashCode(), toString() that subclasses inherit.
- **Player** (`com.simulationrunner.entity.Player`): Extends Entity. Spawns at random grid position via `createRandom()` factory method. Implements two `move()` overloads: one with basic boundary checking, and one with door collision checking. Maintains key inventory as Set<Color> with methods `addKey()`, `hasKey()`, and `getInventory()`. Renders as blue circle at 60% cell size (CIRCLE_SIZE_RATIO = 0.6).
- **Key** (`com.simulationrunner.entity.Key`): Extends Entity. Spawns at least 5 tiles away from player (Manhattan distance) via `createRandomKeys(config, player, count)`. Has `collected` boolean state, `collect()` method, and `isCollected()` getter. Each key has a unique color assigned from ColorPalette. Renders as colored rectangle at 30% cell size (RECTANGLE_SIZE_RATIO = 0.3). Does not render when collected.
- **Door** (`com.simulationrunner.entity.Door`): Extends Entity. Spawns at least 5 tiles away from player via `createRandom()` factory method with fallback to farthest position. Has immutable color matching required key. Implements `canPass(Player)` method checking if player has matching key color. Renders as colored vertical rectangle at 50% width × 80% height with darker border. Blocks player movement unless matching key is in inventory.
- **ColorPalette** (`com.simulationrunner.ColorPalette`): Utility class providing rainbow color palette for keys. Contains 8 predefined colors (red, orange, yellow, green, cyan, blue, purple, magenta). Uses cyclic assignment via modulo when key count exceeds palette size. Non-instantiable utility class with static methods `getKeyColor(index)` and `getPaletteSize()`.
- **HUD** (`com.simulationrunner.ui.HUD`): Heads-Up Display system. Renders 10px footer at bottom of canvas showing key collection status. Displays key icons in their assigned colors with brightness reduction for uncollected keys (40% brightness via UNCOLLECTED_BRIGHTNESS constant). Integrates with GridConfig via `getPixelHeightWithHUD()`.
- **Constants** (`com.simulationrunner.Constants`): Utility class defining default values (DEFAULT_GRID_WIDTH=10, DEFAULT_GRID_HEIGHT=10, DEFAULT_CELL_SIZE=10). **Note:** Currently unused in App, which uses hardcoded GridConfig instead.
- **SystemInfo** (`com.simulationrunner.SystemInfo`): Utility class for retrieving Java and JavaFX runtime versions via `javaVersion()` and `javafxVersion()` methods.

### Entity System
- **Abstract Entity base class** introduced (commit 79b65fb) to reduce code duplication between Player, Key, and Door
- Entity defines protected gridX/gridY fields, abstract render() method, and shared utility methods (manhattanDistance, equals, hashCode, toString)
- **Player was converted from a record to a mutable class** to support movement (commit 0138739) - records are immutable and incompatible with changing position state
- Each entity is responsible for its own rendering via a `render(GraphicsContext, GridConfig)` method
- Entities spawn randomly using factory methods (`Player.createRandom()`, `Key.createRandomKeys()`, `Door.createRandom()`)
- Keys and doors spawn with minimum distance constraint (5 tiles from player via Manhattan distance) to prevent trivial spawns
- Entities are positioned using grid coordinates, then converted to pixel coordinates for rendering (gridX * cellSize)
- Movement logic is encapsulated within Player.move() with boundary and door collision checking
- Player inventory system uses Set<Color> to track collected key colors, enabling door access control

### Input System
- **Keyboard Controls:** WASD keys for movement (W=up, A=left, S=down, D=right)
- Event-driven architecture using JavaFX `scene.setOnKeyPressed()` handler in App
- Input handling flow: KeyEvent → switch on KeyCode → player.move(deltaX, deltaY, config, door) → checkKeyPickup() → render()
- Key collection checked after each movement by comparing player and key grid positions
- Door collision handled within Player.move() via canPass() check
- Manual render trigger after each keypress (no continuous game loop)
- Direct coupling between App and Player (no input manager or command pattern)

### Rendering System
- **Manual render triggers** - not a continuous game loop; render() called explicitly after state changes
- Four-phase rendering process:
  1. Clear canvas and draw grid lines (only in grid area, not HUD footer)
  2. Delegate entity rendering via entity.render(GraphicsContext, GridConfig) for all keys
  3. Render door (if exists) via door.render(gc, config)
  4. Render player via player.render(gc, config), then HUD footer via hud.render(gc, config, keys)
- Entity self-rendering pattern: each entity converts its own grid coordinates to pixels
- Player renders as filled blue circle centered in grid cell at 60% cell size
- Keys render as filled colored rectangles at 30% cell size (invisible when collected), each with unique rainbow color
- Door renders as colored vertical rectangle (50% width × 80% height) with darker border for depth
- HUD renders 10px footer below grid with key collection status (collected keys bright, uncollected dimmed to 40%), preserving key colors

### Testing
- **Test Framework:** JUnit 5.11.4 with comprehensive unit test coverage for core components
- **GridConfigTest** (`src/test/java/.../config/GridConfigTest.java`): Tests constructors, pixel calculations (including HUD height), validation, toString()
- **PlayerTest** (`src/test/java/.../entity/PlayerTest.java`): Tests random spawn, constructor validation, movement with boundary checking, inventory operations (addKey, hasKey), null safety, equality contracts
- **KeyTest** (`src/test/java/.../entity/KeyTest.java`): Tests random spawn with distance constraints, collection state, rendering behavior when collected, color assignment
- **DoorTest** (`src/test/java/.../entity/DoorTest.java`): Tests random spawn with distance constraints, canPass() access control, color requirement, null safety
- **ColorPaletteTest** (`src/test/java/.../ColorPaletteTest.java`): Tests color retrieval, cyclic palette assignment, negative index validation, palette uniqueness, non-instantiability
- **HUDTest** (`src/test/java/.../ui/HUDTest.java`): Tests HUD rendering, key icon display, collected/uncollected visual states
- **Test Coverage Gaps:** No tests for App, Grid, or SystemInfo classes
- **No Integration Tests:** All tests are unit tests; no end-to-end testing of input → movement → collection → door interaction → rendering flow

## Design Decisions

1. **Abstract Entity Hierarchy**: Created abstract Entity base class to share common code (position, rendering interface, utility methods) between Player, Key, and Door. Reduces duplication and makes adding new entities easier.
2. **Mutable Entity, Immutable Configuration**: Entity position mutates to support movement and collection state, but GridConfig is immutable to prevent accidental modification during gameplay.
3. **Top-Left Origin**: Standard screen coordinate system with (0,0) at top-left corner, matching JavaFX canvas coordinates.
4. **Separation of Concerns**: Grid wraps GridConfig and contains game state (player, keys, door), while GridConfig remains pure configuration. HUD is separate UI component.
5. **Constructor Validation**: GridConfig and Entity validate all parameters in constructors, throwing IllegalArgumentException for invalid values (fail-fast).
6. **Record to Class Conversion**: Player was originally a Java record but converted to mutable class because records are immutable and cannot support changing state needed for movement and inventory.
7. **Entity Self-Rendering**: Each entity implements its own render() method rather than centralized rendering logic, allowing entity-specific rendering behavior (e.g., Key doesn't render when collected, Door has border effect).
8. **Manual Render Triggers**: Render is called explicitly after state changes rather than continuous game loop, suitable for turn-based/event-driven game.
9. **Spatial Spawn Constraints**: Keys and doors spawn with minimum Manhattan distance (5 tiles) from player to prevent trivial collection/access. Fallback to farthest position for small grids.
10. **HUD Integration**: Canvas height calculation includes HUD footer via `getPixelHeightWithHUD()`, keeping UI layout concerns in GridConfig rather than scattered across App.
11. **Rainbow Color Palette**: Keys and doors use predefined rainbow palette via ColorPalette utility class with cyclic assignment. Centralizes color logic and ensures visual consistency across game and HUD. Non-instantiable utility class pattern prevents accidental instantiation.
12. **Color-Based Access Control**: Door color matches first key's color, creating key-door pairing. Player inventory uses Set<Color> for O(1) lookup when checking door access via canPass().
13. **Movement Overloading**: Player has two move() overloads - one for basic boundary checking, one for door collision. App uses door-aware version. Provides flexibility for different movement contexts.

## Development Guidelines

- When planning code changes, focus on architectural decisions rather than implementation details. Avoid generating code during the planning phase.
- Respect the entity self-rendering pattern: each entity should manage its own render() implementation rather than centralizing rendering logic.
- When adding new entity types, extend the Entity abstract class and implement the render() method.