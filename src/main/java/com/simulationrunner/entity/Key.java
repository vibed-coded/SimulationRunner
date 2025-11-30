package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Represents a key entity on the grid.
 * Keys spawn at least 5 tiles away from the player and can be collected.
 */
public class Key extends Entity {
    private static final double RECTANGLE_SIZE_RATIO = 0.3;
    private static final int MIN_SPAWN_DISTANCE = 5;
    private static final int MAX_SPAWN_ATTEMPTS = 1000;
    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private boolean collected;

    /**
     * Creates a new key at the specified grid position.
     *
     * @param gridX the X coordinate on the grid
     * @param gridY the Y coordinate on the grid
     * @throws IllegalArgumentException if coordinates are negative
     */
    public Key(int gridX, int gridY) {
        super(gridX, gridY);
        this.collected = false;
    }

    /**
     * Creates a list of keys at random positions on the grid.
     * Each key will spawn at least 5 tiles away from the player (Manhattan distance).
     *
     * @param config the grid configuration
     * @param player the player entity to maintain distance from
     * @param count the number of keys to create
     * @return a list of randomly positioned keys
     * @throws NullPointerException if config or player is null
     * @throws IllegalArgumentException if count is negative
     */
    public static List<Key> createRandomKeys(GridConfig config, Player player, int count) {
        Objects.requireNonNull(config, "GridConfig cannot be null");
        Objects.requireNonNull(player, "Player cannot be null");
        if (count < 0) {
            throw new IllegalArgumentException("count must be non-negative");
        }

        List<Key> keys = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            Key key = createSingleKey(config, player);
            keys.add(key);
        }

        return keys;
    }

    /**
     * Creates a single key at a random position, maintaining minimum distance from player.
     */
    private static Key createSingleKey(GridConfig config, Player player) {
        // Try to find a position at least MIN_SPAWN_DISTANCE away from player
        for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
            int x = RANDOM.nextInt(config.getGridWidth());
            int y = RANDOM.nextInt(config.getGridHeight());

            int distance = manhattanDistance(x, y, player.getGridX(), player.getGridY());

            if (distance >= MIN_SPAWN_DISTANCE) {
                return new Key(x, y);
            }
        }

        // Fallback: if we couldn't find a valid position, place at farthest point from player
        return createFarthestKey(config, player);
    }

    /**
     * Creates a key at the farthest possible position from the player.
     * Used as fallback for small grids where minimum distance cannot be satisfied.
     */
    private static Key createFarthestKey(GridConfig config, Player player) {
        int maxDistance = -1;
        int bestX = 0;
        int bestY = 0;

        // Sample grid positions to find the farthest one
        for (int x = 0; x < config.getGridWidth(); x++) {
            for (int y = 0; y < config.getGridHeight(); y++) {
                int distance = manhattanDistance(x, y, player.getGridX(), player.getGridY());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestX = x;
                    bestY = y;
                }
            }
        }

        return new Key(bestX, bestY);
    }

    /**
     * Marks this key as collected.
     */
    public void collect() {
        this.collected = true;
    }

    /**
     * Checks if this key has been collected.
     *
     * @return true if the key has been collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }

    @Override
    public void render(GraphicsContext gc, GridConfig config) {
        if (collected) {
            return; // Don't render collected keys
        }

        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int cellSize = config.getCellSize();

        // Calculate pixel position of cell center
        double centerX = (gridX * cellSize) + (cellSize / 2.0);
        double centerY = (gridY * cellSize) + (cellSize / 2.0);

        // Calculate rectangle size
        double rectSize = cellSize * RECTANGLE_SIZE_RATIO;

        // Draw filled gold rectangle centered in the cell
        gc.setFill(Color.GOLD);
        gc.fillRect(centerX - rectSize / 2, centerY - rectSize / 2, rectSize, rectSize);
    }

    @Override
    public String toString() {
        return "Key[gridX=" + gridX + ", gridY=" + gridY + ", collected=" + collected + "]";
    }
}
