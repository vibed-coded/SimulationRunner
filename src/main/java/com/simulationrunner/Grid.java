package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Player;

public class Grid {
    private final GridConfig config;
    private final Player player;

    public Grid(GridConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("GridConfig cannot be null");
        }
        this.config = config;
        this.player = Player.createRandom(config);
    }

    public GridConfig getConfig() {
        return config;
    }

    public Player getPlayer() {
        return player;
    }
}
