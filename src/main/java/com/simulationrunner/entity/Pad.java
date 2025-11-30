package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;

/**
 * Represents a goal pad on the grid.
 * When the player reaches this pad, they win the level.
 */
public class Pad extends Entity {
    private static final double PAD_SIZE_RATIO = 0.8;
    private static final Color PAD_COLOR = Color.GOLD;

    /**
     * Creates a new pad at the specified grid position.
     *
     * @param position the position on the grid
     * @throws NullPointerException if position is null
     */
    public Pad(GridPosition position) {
        super(position);
    }

    /**
     * Creates a pad on the opposite side of the door from the player.
     * If the player is on the left side (playerX < doorX), pad spawns on the right.
     * If the player is on the right side (playerX > doorX), pad spawns on the left.
     *
     * @param config the grid configuration
     * @param player the player entity
     * @param door the door entity
     * @return a pad positioned on the opposite side from the player
     * @throws NullPointerException if config, player, or door is null
     */
    public static Pad createOpposite(GridConfig config, Player player, Door door) {
        Objects.requireNonNull(config, "GridConfig cannot be null");
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(door, "Door cannot be null");

        int playerX = player.getGridX();
        int doorX = door.getGridX();

        // Determine which side to place the pad
        int padX;
        if (playerX < doorX) {
            // Player is on the left, pad goes on the right
            padX = config.getGridWidth() - 1;
        } else {
            // Player is on the right (or at door), pad goes on the left
            padX = 0;
        }

        // Place pad in the middle of the grid vertically
        int padY = config.getGridHeight() / 2;

        return new Pad(new GridPosition(padX, padY));
    }

    @Override
    public void render(GraphicsContext gc, GridConfig config) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int cellSize = config.getCellSize();

        // Calculate pixel position of cell center
        double centerX = (position.x() * cellSize) + (cellSize / 2.0);
        double centerY = (position.y() * cellSize) + (cellSize / 2.0);

        // Calculate pad size
        double padSize = cellSize * PAD_SIZE_RATIO;

        // Draw filled gold square centered in the cell
        gc.setFill(PAD_COLOR);
        gc.fillRect(centerX - padSize / 2, centerY - padSize / 2, padSize, padSize);

        // Draw a darker border for emphasis
        gc.setStroke(PAD_COLOR.darker());
        gc.setLineWidth(2);
        gc.strokeRect(centerX - padSize / 2, centerY - padSize / 2, padSize, padSize);
    }
}
