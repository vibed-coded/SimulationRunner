# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SimulationRunner - A 2D grid-based game where a player entity can move around a grid using keyboard controls. Currently implements core movement mechanics with plans to add key collection, door unlocking, and multiple levels. Built with JavaFX 25.0.1 and Java 25.

**Current Implementation Status:**
- Player movement with WASD controls
- Grid rendering with configurable dimensions
- Boundary detection and collision prevention
- **Planned Features:** Key entity, Door entity, level system, win/lose conditions

## Development Commands

### Prerequisites
- Java 25 (use SDKMAN: `sdk install java 25-open`)
- Uses Maven wrapper (no separate Maven installation needed)

### Build and Test
```bash
./mvnw clean test        # Run all tests
./mvnw clean package     # Build the project
./mvnw javafx:run        # Run the JavaFX application
```

### Running Single Tests
```bash
./mvnw test -Dtest=GridConfigTest              # Run specific test class
./mvnw test -Dtest=GridConfigTest#testMethod   # Run specific test method
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
- **App** (`com.simulationrunner.App`): Main JavaFX application entry point. Creates grid (currently hardcoded as 10x10 with 50px cells), canvas, and handles keyboard input. Renders grid using JavaFX GraphicsContext with manual render triggers (not a continuous game loop). Handles WASD keyboard events and delegates to player movement.
- **Grid** (`com.simulationrunner.Grid`): Represents the game grid, wraps GridConfig with validation. Creates and manages the Player entity via `Player.createRandom()`.
- **GridConfig** (`com.simulationrunner.config.GridConfig`): Immutable configuration class for grid dimensions (gridWidth, gridHeight) and cellSize. Provides computed pixel dimensions via `getPixelWidth()` and `getPixelHeight()`. Default constructor creates 10x10 grid with 10px cells.
- **Player** (`com.simulationrunner.entity.Player`): Mutable class representing the player entity with gridX and gridY position. Spawns at random grid position via factory method. Implements `move(deltaX, deltaY, GridConfig)` with boundary checking. Renders as blue circle at 60% cell size (CIRCLE_SIZE_RATIO = 0.6). Implements equals(), hashCode(), toString() manually.
- **Constants** (`com.simulationrunner.Constants`): Utility class defining default values (DEFAULT_GRID_WIDTH=10, DEFAULT_GRID_HEIGHT=10, DEFAULT_CELL_SIZE=10). **Note:** Currently unused in App, which uses hardcoded GridConfig instead.
- **SystemInfo** (`com.simulationrunner.SystemInfo`): Utility class for retrieving Java and JavaFX runtime versions via `javaVersion()` and `javafxVersion()` methods.

### Entity System
- **Player was converted from a record to a mutable class** to support movement (commit 0138739)
- Records are immutable and incompatible with changing position state; mutable class pattern used instead
- Each entity is responsible for its own rendering via a `render(GraphicsContext, GridConfig)` method
- Player spawns randomly using `Player.createRandom(GridConfig)` factory method
- Entities are positioned using grid coordinates, then converted to pixel coordinates for rendering (gridX * cellSize)
- Movement logic is encapsulated within the entity (Player.move() includes boundary checking)

### Input System
- **Keyboard Controls:** WASD keys for movement (W=up, A=left, S=down, D=right)
- Event-driven architecture using JavaFX `scene.setOnKeyPressed()` handler in App
- Input handling flow: KeyEvent → switch on KeyCode → player.move(deltaX, deltaY, config) → render()
- Manual render trigger after each keypress (no continuous game loop)
- Direct coupling between App and Player (no input manager or command pattern)

### Rendering System
- **Manual render triggers** - not a continuous game loop; render() called explicitly after state changes
- Two-phase rendering process:
  1. Clear canvas and draw grid lines
  2. Delegate entity rendering via entity.render(GraphicsContext, GridConfig)
- Entity self-rendering pattern: each entity converts its own grid coordinates to pixels
- Player renders as filled blue circle centered in grid cell at 60% cell size (CIRCLE_SIZE_RATIO constant)

### Testing
- **Test Framework:** JUnit 5.11.4 with comprehensive unit test coverage
- **GridConfigTest** (`src/test/java/.../config/GridConfigTest.java`): Tests constructors, pixel calculations, validation, toString()
- **PlayerTest** (`src/test/java/.../entity/PlayerTest.java`): 204 lines of tests covering:
  - Random spawn distribution verification using `@RepeatedTest(100)` to ensure statistical randomness
  - Constructor validation (positive coordinates, within bounds)
  - Movement in all four directions with boundary checking
  - Null safety for all methods (move, render)
  - Equality, hashCode, toString contracts
- **Test Coverage Gaps:** No tests for App, Grid, or SystemInfo classes
- **No Integration Tests:** All tests are unit tests; no end-to-end testing of input → movement → rendering flow

## Design Decisions

1. **Mutable Entity, Immutable Configuration**: Player position mutates to support movement, but GridConfig is immutable to prevent accidental modification during gameplay.
2. **Top-Left Origin**: Standard screen coordinate system with (0,0) at top-left corner, matching JavaFX canvas coordinates.
3. **Separation of Concerns**: Grid wraps GridConfig and contains game state (player reference), while GridConfig remains pure configuration.
4. **Constructor Validation**: GridConfig and Player validate all parameters in constructors, throwing IllegalArgumentException for invalid values (fail-fast).
5. **Record to Class Conversion**: Player was originally a Java record but converted to mutable class because records are immutable and cannot support changing state needed for movement.
6. **Entity Self-Rendering**: Each entity implements its own render() method rather than centralized rendering logic, allowing entity-specific rendering behavior.
7. **Manual Render Triggers**: Render is called explicitly after state changes rather than continuous game loop, suitable for turn-based/event-driven game.
- Do not generate code during planning that is not the point. The LLM is just wasting tokens since it is duplicate generation.