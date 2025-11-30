package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;

import java.util.Objects;

/**
 * Abstract base class for all entities in the game.
 * Entities have a position on the grid and can be rendered.
 */
public abstract class Entity {
    protected GridPosition position;

    /**
     * Creates a new entity at the specified grid position.
     *
     * @param position the position on the grid
     * @throws NullPointerException if position is null
     */
    protected Entity(GridPosition position) {
        this.position = Objects.requireNonNull(position, "Position cannot be null");
    }

    /**
     * Gets the position on the grid.
     *
     * @return the grid position
     */
    public GridPosition getPosition() {
        return position;
    }

    /**
     * Gets the X coordinate on the grid.
     *
     * @return the grid X coordinate
     */
    public int getGridX() {
        return position.x();
    }

    /**
     * Gets the Y coordinate on the grid.
     *
     * @return the grid Y coordinate
     */
    public int getGridY() {
        return position.y();
    }

    /**
     * Renders this entity on the canvas.
     *
     * @param gc the graphics context to draw on
     * @param config the grid configuration for pixel calculations
     */
    public abstract void render(GraphicsContext gc, GridConfig config);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Entity entity = (Entity) obj;
        return position.equals(entity.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[position=" + position + "]";
    }
}
