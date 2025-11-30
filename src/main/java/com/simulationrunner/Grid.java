package com.simulationrunner;

import com.simulationrunner.config.GridConfig;

public class Grid {
    private final GridConfig config;

    public Grid(GridConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("GridConfig cannot be null");
        }
        this.config = config;
    }

    public GridConfig getConfig() {
        return config;
    }
}
