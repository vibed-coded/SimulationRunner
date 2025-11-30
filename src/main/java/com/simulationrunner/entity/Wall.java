package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a wall segment on the grid.
 * Walls are impassable barriers that block player movement.
 */
public class Wall extends Entity {
    private static final Color WALL_COLOR = Color.DARKGRAY;
    private static final double WALL_SIZE_RATIO = 1.0; // Full cell size

    /**
     * Creates a new wall segment at the specified grid position.
     *
     * @param position the position on the grid
     * @throws NullPointerException if position is null
     */
    public Wall(GridPosition position) {
        super(position);
    }

    /**
     * Creates a vertical wall that extends through the entire height of the grid
     * at the specified x-coordinate.
     *
     * @param config the grid configuration
     * @param x the x-coordinate where the wall should be placed
     * @return a list of Wall segments forming a vertical wall
     * @throws NullPointerException if config is null
     * @throws IllegalArgumentException if x is out of bounds
     */
    public static List<Wall> createVerticalWall(GridConfig config, int x) {
        Objects.requireNonNull(config, "GridConfig cannot be null");
        if (x < 0 || x >= config.getGridWidth()) {
            throw new IllegalArgumentException("x must be within grid bounds");
        }

        List<Wall> walls = new ArrayList<>();
        for (int y = 0; y < config.getGridHeight(); y++) {
            walls.add(new Wall(new GridPosition(x, y)));
        }
        return walls;
    }

    /**
     * Creates a vertical wall that extends through the entire height of the grid
     * at the specified x-coordinate, excluding a gap at the specified position.
     *
     * @param config the grid configuration
     * @param x the x-coordinate where the wall should be placed
     * @param gapPosition the position where a gap should be left (typically for a door)
     * @return a list of Wall segments forming a vertical wall with a gap
     * @throws NullPointerException if config or gapPosition is null
     * @throws IllegalArgumentException if x is out of bounds
     */
    public static List<Wall> createVerticalWallWithGap(GridConfig config, int x, GridPosition gapPosition) {
        Objects.requireNonNull(config, "GridConfig cannot be null");
        Objects.requireNonNull(gapPosition, "Gap position cannot be null");
        if (x < 0 || x >= config.getGridWidth()) {
            throw new IllegalArgumentException("x must be within grid bounds");
        }

        List<Wall> walls = new ArrayList<>();
        for (int y = 0; y < config.getGridHeight(); y++) {
            GridPosition wallPosition = new GridPosition(x, y);
            // Skip the gap position (where the door is)
            if (!wallPosition.isSameAs(gapPosition)) {
                walls.add(new Wall(wallPosition));
            }
        }
        return walls;
    }

    @Override
    public void render(GraphicsContext gc, GridConfig config) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int cellSize = config.getCellSize();

        // Calculate pixel position (top-left corner of cell)
        double x = position.x() * cellSize;
        double y = position.y() * cellSize;

        // Draw filled rectangle covering the entire cell
        gc.setFill(WALL_COLOR);
        gc.fillRect(x, y, cellSize * WALL_SIZE_RATIO, cellSize * WALL_SIZE_RATIO);

        // Draw a lighter border for depth effect
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, cellSize * WALL_SIZE_RATIO, cellSize * WALL_SIZE_RATIO);
    }

    @Override
    public String toString() {
        return "Wall[position=" + position + "]";
    }
}
