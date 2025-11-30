package com.simulationrunner;

/**
 * Represents a position on the grid using x,y coordinates.
 * This is an immutable value type that provides convenient methods
 * for position comparison and distance calculations.
 */
public record GridPosition(int x, int y) {

    /**
     * Creates a new grid position.
     *
     * @param x the X coordinate on the grid
     * @param y the Y coordinate on the grid
     * @throws IllegalArgumentException if coordinates are negative
     */
    public GridPosition {
        if (x < 0) {
            throw new IllegalArgumentException("x must be non-negative");
        }
        if (y < 0) {
            throw new IllegalArgumentException("y must be non-negative");
        }
    }

    /**
     * Calculates the Manhattan distance between this position and another.
     * Manhattan distance is the sum of absolute differences in coordinates,
     * representing the minimum number of moves in a grid-based game.
     *
     * @param other the other position
     * @return the Manhattan distance between the two positions
     * @throws NullPointerException if other is null
     */
    public int manhattanDistance(GridPosition other) {
        if (other == null) {
            throw new NullPointerException("other position cannot be null");
        }
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    /**
     * Creates a new position by adding the given deltas to this position.
     *
     * @param deltaX the change in X coordinate
     * @param deltaY the change in Y coordinate
     * @return a new GridPosition with the updated coordinates
     * @throws IllegalArgumentException if the resulting coordinates are negative
     */
    public GridPosition add(int deltaX, int deltaY) {
        return new GridPosition(this.x + deltaX, this.y + deltaY);
    }

    /**
     * Checks if this position is the same as another position.
     *
     * @param other the other position
     * @return true if both positions have the same x and y coordinates
     */
    public boolean isSameAs(GridPosition other) {
        if (other == null) {
            return false;
        }
        return this.x == other.x && this.y == other.y;
    }
}
