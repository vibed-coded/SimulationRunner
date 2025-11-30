package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
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
     * @param position the position on the grid
     * @throws NullPointerException if position is null
     */
    public Player(GridPosition position) {
        super(position);
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
        return new Player(new GridPosition(x, y));
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

        try {
            GridPosition newPosition = position.add(deltaX, deltaY);

            // Only update position if new position is within bounds
            if (newPosition.x() >= 0 && newPosition.x() < config.getGridWidth() &&
                newPosition.y() >= 0 && newPosition.y() < config.getGridHeight()) {
                this.position = newPosition;
            }
        } catch (IllegalArgumentException e) {
            // New position would have negative coordinates, don't move
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

        try {
            GridPosition newPosition = position.add(deltaX, deltaY);

            // Check if new position is within bounds
            if (newPosition.x() < 0 || newPosition.x() >= config.getGridWidth() ||
                newPosition.y() < 0 || newPosition.y() >= config.getGridHeight()) {
                return; // Out of bounds, don't move
            }

            // Check if there's a door at the new position
            if (door != null && door.getPosition().isSameAs(newPosition)) {
                // Door blocks movement unless player has matching key
                if (!door.canPass(this)) {
                    return; // Can't pass through door, don't move
                }
            }

            // Move is valid
            this.position = newPosition;
        } catch (IllegalArgumentException e) {
            // New position would have negative coordinates, don't move
        }
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
        double centerX = (position.x() * cellSize) + (cellSize / 2.0);
        double centerY = (position.y() * cellSize) + (cellSize / 2.0);

        // Calculate circle diameter and radius
        double diameter = cellSize * CIRCLE_SIZE_RATIO;
        double radius = diameter / 2.0;

        // Draw filled blue circle centered in the cell
        gc.setFill(Color.BLUE);
        gc.fillOval(centerX - radius, centerY - radius, diameter, diameter);
    }

}
