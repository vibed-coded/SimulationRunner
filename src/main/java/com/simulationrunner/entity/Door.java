package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.random.RandomGenerator;

/**
 * Represents a door entity on the grid.
 * Doors block player movement unless the player has a key of matching color.
 */
public class Door extends Entity {
    private static final double DOOR_WIDTH_RATIO = 0.5;
    private static final double DOOR_HEIGHT_RATIO = 1.0; // Full cell height
    private static final RandomGenerator RANDOM = RandomGenerator.getDefault();

    private final Color color;

    /**
     * Creates a new door at the specified grid position with the specified color.
     *
     * @param position the position on the grid
     * @param color the color of this door (must match key color to pass)
     * @throws NullPointerException if position or color is null
     */
    public Door(GridPosition position, Color color) {
        super(position);
        Objects.requireNonNull(color, "Color cannot be null");
        this.color = color;
    }

    /**
     * Creates a door at a random position on the grid.
     * The door will spawn at least 5 tiles away from the player.
     *
     * @param config the grid configuration
     * @param player the player entity to maintain distance from
     * @param color the color of the door
     * @return a randomly positioned door
     * @throws NullPointerException if config, player, or color is null
     */
    public static Door createRandom(GridConfig config, Player player, Color color) {
        Objects.requireNonNull(config, "GridConfig cannot be null");
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(color, "Color cannot be null");

        int MIN_SPAWN_DISTANCE = 5;
        int MAX_SPAWN_ATTEMPTS = 1000;

        // Try to find a position at least MIN_SPAWN_DISTANCE away from player
        for (int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
            int x = RANDOM.nextInt(config.getGridWidth());
            int y = RANDOM.nextInt(config.getGridHeight());
            GridPosition candidatePosition = new GridPosition(x, y);

            int distance = candidatePosition.manhattanDistance(player.getPosition());

            if (distance >= MIN_SPAWN_DISTANCE) {
                return new Door(candidatePosition, color);
            }
        }

        // Fallback: if we couldn't find a valid position, place at farthest point from player
        return createFarthest(config, player, color);
    }

    /**
     * Creates a door at the farthest possible position from the player.
     * Used as fallback for small grids where minimum distance cannot be satisfied.
     */
    private static Door createFarthest(GridConfig config, Player player, Color color) {
        int maxDistance = -1;
        GridPosition bestPosition = new GridPosition(0, 0);

        // Sample grid positions to find the farthest one
        for (int x = 0; x < config.getGridWidth(); x++) {
            for (int y = 0; y < config.getGridHeight(); y++) {
                GridPosition candidatePosition = new GridPosition(x, y);
                int distance = candidatePosition.manhattanDistance(player.getPosition());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bestPosition = candidatePosition;
                }
            }
        }

        return new Door(bestPosition, color);
    }

    /**
     * Gets the color of this door.
     *
     * @return the door's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Checks if the player can pass through this door.
     * The player needs a key of matching color.
     *
     * @param player the player attempting to pass through
     * @return true if the player has a matching key, false otherwise
     * @throws NullPointerException if player is null
     */
    public boolean canPass(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        return player.hasKey(color);
    }

    @Override
    public void render(GraphicsContext gc, GridConfig config) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int cellSize = config.getCellSize();

        // Calculate pixel position of cell center
        double centerX = (position.x() * cellSize) + (cellSize / 2.0);
        double centerY = (position.y() * cellSize) + (cellSize / 2.0);

        // Calculate door dimensions
        double doorWidth = cellSize * DOOR_WIDTH_RATIO;
        double doorHeight = cellSize * DOOR_HEIGHT_RATIO;

        // Draw filled rectangle centered in the cell (vertical door)
        gc.setFill(color);
        gc.fillRect(centerX - doorWidth / 2, centerY - doorHeight / 2, doorWidth, doorHeight);

        // Draw a darker border to make it look more like a door
        gc.setStroke(color.darker());
        gc.setLineWidth(2);
        gc.strokeRect(centerX - doorWidth / 2, centerY - doorHeight / 2, doorWidth, doorHeight);
    }

    @Override
    public String toString() {
        return "Door[position=" + position + ", color=" + color + "]";
    }
}
