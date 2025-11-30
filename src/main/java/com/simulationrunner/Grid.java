package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Door;
import com.simulationrunner.entity.Key;
import com.simulationrunner.entity.Player;

import java.util.Collections;
import java.util.List;

public class Grid {
    private final GridConfig config;
    private final Player player;
    private final List<Key> keys;
    private final Door door;

    public Grid(GridConfig config, int keyCount) {
        if (config == null) {
            throw new IllegalArgumentException("GridConfig cannot be null");
        }
        if (keyCount < 0) {
            throw new IllegalArgumentException("keyCount must be non-negative");
        }
        this.config = config;
        this.player = Player.createRandom(config);
        this.keys = Key.createRandomKeys(config, player, keyCount);

        // Create door with same color as first key (if any keys exist)
        if (!keys.isEmpty()) {
            this.door = Door.createRandom(config, player, keys.get(0).getColor());
        } else {
            this.door = null;
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
}
