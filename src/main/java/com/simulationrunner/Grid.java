package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Door;
import com.simulationrunner.entity.Key;
import com.simulationrunner.entity.Pad;
import com.simulationrunner.entity.Player;
import com.simulationrunner.entity.Wall;

import java.util.Collections;
import java.util.List;

public class Grid {
    private final GridConfig config;
    private final Player player;
    private final List<Key> keys;
    private final Door door;
    private final List<Wall> walls;
    private final Pad pad;

    public Grid(GridConfig config, int keyCount) {
        if (config == null) {
            throw new IllegalArgumentException("GridConfig cannot be null");
        }
        if (keyCount < 0) {
            throw new IllegalArgumentException("keyCount must be non-negative");
        }
        this.config = config;
        this.player = Player.createRandom(config);

        // Create door with same color as first key (if any keys exist)
        if (keyCount > 0) {
            // Create door first to determine which side to spawn keys on
            this.door = Door.createRandom(config, player, ColorPalette.getKeyColor(0));

            // Determine which side of the door the player is on
            int playerX = player.getGridX();
            int doorX = door.getGridX();

            // Calculate the x-coordinate range for key spawning (player's side)
            int minX, maxX;
            if (playerX < doorX) {
                // Player is on the left side, spawn keys on the left
                minX = 0;
                maxX = doorX - 1;
            } else if (playerX > doorX) {
                // Player is on the right side, spawn keys on the right
                minX = doorX + 1;
                maxX = config.getGridWidth() - 1;
            } else {
                // Player is at the same x as the door (rare case)
                // Default to left side
                minX = 0;
                maxX = Math.max(0, doorX - 1);
            }

            // Create keys on the player's side of the door
            this.keys = Key.createRandomKeys(config, player, keyCount, minX, maxX);

            // Create vertical wall through the door's x-coordinate with a gap at the door
            this.walls = Wall.createVerticalWallWithGap(config, door.getGridX(), door.getPosition());

            // Create pad on the opposite side from the player
            this.pad = Pad.createOpposite(config, player, door);
        } else {
            this.keys = Collections.emptyList();
            this.door = null;
            this.walls = Collections.emptyList();
            this.pad = null;
        }
    }

    public GridConfig getConfig() {
        return config;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Key> getKeys() {
        return Collections.unmodifiableList(keys);
    }

    public Door getDoor() {
        return door;
    }

    public List<Wall> getWalls() {
        return Collections.unmodifiableList(walls);
    }

    public Pad getPad() {
        return pad;
    }
}
