package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Represents a player entity on the grid.
 * The player spawns at a random grid location and is rendered as a blue circle.
 *
 * @param gridX the X coordinate on the grid (0 to gridWidth-1)
 * @param gridY the Y coordinate on the grid (0 to gridHeight-1)
 */
public record Player(int gridX, int gridY) {
    private static final double CIRCLE_SIZE_RATIO = 0.6;
    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    /**
     * Creates a new player at a random position on the grid.
     *
     * @param config the grid configuration
     * @return a new Player instance at a random position
     * @throws NullPointerException if config is null
     */
    public static Player createRandom(GridConfig config) {
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int x = RANDOM.nextInt(config.getGridWidth());
        int y = RANDOM.nextInt(config.getGridHeight());
        return new Player(x, y);
    }

    /**
     * Compact constructor with validation.
     */
    public Player {
        if (gridX < 0) {
            throw new IllegalArgumentException("gridX must be non-negative");
        }
        if (gridY < 0) {
            throw new IllegalArgumentException("gridY must be non-negative");
        }
    }

    /**
     * Renders the player on the canvas as a blue circle centered in its grid cell.
     *
     * @param gc the graphics context to draw on
     * @param config the grid configuration for pixel calculations
     * @throws NullPointerException if gc or config is null
     */
    public void render(GraphicsContext gc, GridConfig config) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int cellSize = config.getCellSize();

        // Calculate pixel position of cell center
        double centerX = (gridX * cellSize) + (cellSize / 2.0);
        double centerY = (gridY * cellSize) + (cellSize / 2.0);

        // Calculate circle diameter and radius
        double diameter = cellSize * CIRCLE_SIZE_RATIO;
        double radius = diameter / 2.0;

        // Draw filled blue circle centered in the cell
        gc.setFill(Color.BLUE);
        gc.fillOval(centerX - radius, centerY - radius, diameter, diameter);
    }
}
