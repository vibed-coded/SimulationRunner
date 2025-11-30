package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.random.RandomGenerator;

/**
 * Represents a player entity on the grid.
 * The player spawns at a random grid location and is rendered as a blue circle.
 * The player maintains an inventory of collected key colors.
 */
public class Player extends Entity {
    private static final double CIRCLE_SIZE_RATIO = 0.6;
    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final Set<Color> inventory;

    /**
     * Creates a new player at the specified grid position.
     *
     * @param gridX the X coordinate on the grid (0 to gridWidth-1)
     * @param gridY the Y coordinate on the grid (0 to gridHeight-1)
     * @throws IllegalArgumentException if coordinates are negative
     */
    public Player(int gridX, int gridY) {
        super(gridX, gridY);
        this.inventory = new HashSet<>();
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

    /**
     * Moves the player by the specified delta if the new position is within bounds
     * and not blocked by a door the player cannot pass through.
     *
     * @param deltaX the change in X coordinate
     * @param deltaY the change in Y coordinate
     * @param config the grid configuration for boundary checking
     * @param door the door to check for collision (null if no door)
     * @throws NullPointerException if config is null
     */
    public void move(int deltaX, int deltaY, GridConfig config, Door door) {
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int newX = gridX + deltaX;
        int newY = gridY + deltaY;

        // Check if new position is within bounds
        if (newX < 0 || newX >= config.getGridWidth() ||
            newY < 0 || newY >= config.getGridHeight()) {
            return; // Out of bounds, don't move
        }

        // Check if there's a door at the new position
        if (door != null && door.getGridX() == newX && door.getGridY() == newY) {
            // Door blocks movement unless player has matching key
            if (!door.canPass(this)) {
                return; // Can't pass through door, don't move
            }
        }

        // Move is valid
        this.gridX = newX;
        this.gridY = newY;
    }

    /**
     * Adds a key color to the player's inventory.
     *
     * @param keyColor the color of the key to add
     * @throws NullPointerException if keyColor is null
     */
    public void addKey(Color keyColor) {
        Objects.requireNonNull(keyColor, "Key color cannot be null");
        inventory.add(keyColor);
    }

    /**
     * Checks if the player has a key of the specified color.
     *
     * @param keyColor the color to check for
     * @return true if the player has a key of this color, false otherwise
     * @throws NullPointerException if keyColor is null
     */
    public boolean hasKey(Color keyColor) {
        Objects.requireNonNull(keyColor, "Key color cannot be null");
        return inventory.contains(keyColor);
    }

    /**
     * Gets an unmodifiable view of the player's key inventory.
     *
     * @return the set of key colors the player has collected
     */
    public Set<Color> getInventory() {
        return Collections.unmodifiableSet(inventory);
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
