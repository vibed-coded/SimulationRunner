# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SimulationRunner - A 2D grid-based game where a player entity must find a key and open a door. The game supports multiple levels with increasing complexity.

## Architecture

### Coordinate System
- Grid uses top-left origin (0,0)
- X-axis increases going right
- Y-axis increases going down
- Grid dimensions are configurable (default: 10x10)
- Cell size is configurable in pixels (default: 10px)

### Core Components
- **GridConfig** (`com.simulationrunner.config.GridConfig`): Manages grid dimensions and cell size configuration
- **Constants** (`com.simulationrunner.Constants`): Defines default values for grid configuration

## Development Commands

### Build and Test
```bash
./mvnw clean test        # Run all tests
./mvnw clean package     # Build the project
./mvnw javafx:run        # Run the JavaFX application
```

## Code Structure

```
src/
├── main/
│   └── java/
│       └── com/simulationrunner/
│           ├── App.java              # Main JavaFX application
│           ├── Constants.java        # Default configuration values
│           └── config/
│               └── GridConfig.java   # Grid configuration class
└── test/
    └── java/
        └── com/simulationrunner/
            └── config/
                └── GridConfigTest.java  # Grid configuration tests
```

## Design Decisions

1. **Configurable Grid**: Grid dimensions and cell size are configurable to support different level sizes and display resolutions.
2. **Top-Left Origin**: Standard screen coordinate system with (0,0) at top-left corner.
3. **Immutable Configuration**: GridConfig is immutable to prevent accidental modification during gameplay.
