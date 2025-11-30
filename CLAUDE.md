# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SimulationRunner - A 2D grid-based game where a player entity must find a key and open a door. The game supports multiple levels with increasing complexity. Built with JavaFX 25.0.1 and Java 25.

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
- **App** (`com.simulationrunner.App`): Main JavaFX application entry point. Creates grid, canvas, and renders the grid using JavaFX GraphicsContext.
- **Grid** (`com.simulationrunner.Grid`): Represents the game grid, wraps GridConfig with validation.
- **GridConfig** (`com.simulationrunner.config.GridConfig`): Immutable configuration for grid dimensions (gridWidth, gridHeight) and cellSize. Provides computed pixel dimensions via `getPixelWidth()` and `getPixelHeight()`.
- **Constants** (`com.simulationrunner.Constants`): Defines default values (DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT, DEFAULT_CELL_SIZE).

## Design Decisions

1. **Immutable Configuration**: GridConfig is immutable to prevent accidental modification during gameplay. All fields are final and set via constructor.
2. **Top-Left Origin**: Standard screen coordinate system with (0,0) at top-left corner, matching JavaFX canvas coordinates.
3. **Separation of Concerns**: Grid wraps GridConfig and will eventually contain game state logic, while GridConfig remains pure configuration.
4. **Constructor Validation**: GridConfig validates all dimensions are positive in constructor, throwing IllegalArgumentException for invalid values.
