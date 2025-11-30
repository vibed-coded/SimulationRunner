package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

/**
 * Abstract base class for all entities in the game.
 * Entities have a position on the grid and can be rendered.
 */
public abstract class Entity {
    protected int gridX;
    protected int gridY;

    /**
     * Creates a new entity at the specified grid position.
     *
     * @param gridX the X coordinate on the grid
     * @param gridY the Y coordinate on the grid
     * @throws IllegalArgumentException if coordinates are negative
     */
    protected Entity(int gridX, int gridY) {
        if (gridX < 0) {
            throw new IllegalArgumentException("gridX must be non-negative");
        }
        if (gridY < 0) {
            throw new IllegalArgumentException("gridY must be non-negative");
        }
        this.gridX = gridX;
        this.gridY = gridY;
    }

    /**
     * Gets the X coordinate on the grid.
     *
     * @return the grid X coordinate
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * Gets the Y coordinate on the grid.
     *
     * @return the grid Y coordinate
     */
    public int getGridY() {
        return gridY;
    }

    /**
     * Renders this entity on the canvas.
     *
     * @param gc the graphics context to draw on
     * @param config the grid configuration for pixel calculations
     */
    public abstract void render(GraphicsContext gc, GridConfig config);

    /**
     * Calculates the Manhattan distance between two grid positions.
     * Manhattan distance is the sum of absolute differences in coordinates,
     * representing the minimum number of moves in a grid-based game.
     *
     * @param x1 first X coordinate
     * @param y1 first Y coordinate
     * @param x2 second X coordinate
     * @param y2 second Y coordinate
     * @return the Manhattan distance between the two positions
     */
    protected static int manhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity entity = (Entity) obj;
        return gridX == entity.gridX && gridY == entity.gridY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gridX, gridY);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[gridX=" + gridX + ", gridY=" + gridY + "]";
    }
}
