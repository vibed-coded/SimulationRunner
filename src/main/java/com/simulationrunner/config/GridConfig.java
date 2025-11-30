package com.simulationrunner.config;

public class GridConfig {
    private final int gridWidth;
    private final int gridHeight;
    private final int cellSize;

    public GridConfig() {
        this(10, 10, 10);
    }

    public GridConfig(int gridWidth, int gridHeight, int cellSize) {
        if (gridWidth <= 0) {
            throw new IllegalArgumentException("Grid width must be greater than 0");
        }
        if (gridHeight <= 0) {
            throw new IllegalArgumentException("Grid height must be greater than 0");
        }
        if (cellSize <= 0) {
            throw new IllegalArgumentException("Cell size must be greater than 0");
        }

        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getPixelWidth() {
        return gridWidth * cellSize;
    }

    public int getPixelHeight() {
        return gridHeight * cellSize;
    }

    @Override
    public String toString() {
        return String.format("GridConfig[width=%d, height=%d, cellSize=%d]",
            gridWidth, gridHeight, cellSize);
    }
}
