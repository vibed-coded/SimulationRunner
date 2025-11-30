package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Represents a player entity on the grid.
 * The player spawns at a random grid location and is rendered as a blue circle.
 */
public class Player extends Entity {
    private static final double CIRCLE_SIZE_RATIO = 0.6;
    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    /**
     * Creates a new player at the specified grid position.
     *
     * @param gridX the X coordinate on the grid (0 to gridWidth-1)
     * @param gridY the Y coordinate on the grid (0 to gridHeight-1)
     * @throws IllegalArgumentException if coordinates are negative
     */
    public Player(int gridX, int gridY) {
        super(gridX, gridY);
    }

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
     * Moves the player by the specified delta if the new position is within bounds.
     *
     * @param deltaX the change in X coordinate
     * @param deltaY the change in Y coordinate
     * @param config the grid configuration for boundary checking
     * @throws NullPointerException if config is null
     */
    public void move(int deltaX, int deltaY, GridConfig config) {
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int newX = gridX + deltaX;
        int newY = gridY + deltaY;

        // Only update position if new position is within bounds
        if (newX >= 0 && newX < config.getGridWidth() &&
            newY >= 0 && newY < config.getGridHeight()) {
            this.gridX = newX;
            this.gridY = newY;
        }
    }

    @Override
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
