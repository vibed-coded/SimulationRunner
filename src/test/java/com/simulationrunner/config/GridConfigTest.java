package com.simulationrunner.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridConfigTest {

    @Test
    void testDefaultConstructor() {
        GridConfig config = new GridConfig();
        assertEquals(10, config.getGridWidth());
        assertEquals(10, config.getGridHeight());
        assertEquals(10, config.getCellSize());
    }

    @Test
    void testCustomConfiguration() {
        GridConfig config = new GridConfig(20, 15, 5);
        assertEquals(20, config.getGridWidth());
        assertEquals(15, config.getGridHeight());
        assertEquals(5, config.getCellSize());
    }

    @Test
    void testPixelDimensions() {
        GridConfig config = new GridConfig(10, 10, 10);
        assertEquals(100, config.getPixelWidth());
        assertEquals(100, config.getPixelHeight());

        GridConfig config2 = new GridConfig(8, 6, 15);
        assertEquals(120, config2.getPixelWidth());
        assertEquals(90, config2.getPixelHeight());
    }

    @Test
    void testInvalidGridWidth() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(0, 10, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(-5, 10, 10);
        });
    }

    @Test
    void testInvalidGridHeight() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(10, 0, 10);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(10, -5, 10);
        });
    }

    @Test
    void testInvalidCellSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(10, 10, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new GridConfig(10, 10, -10);
        });
    }

    @Test
    void testToString() {
        GridConfig config = new GridConfig(10, 10, 10);
        String result = config.toString();
        assertTrue(result.contains("width=10"));
        assertTrue(result.contains("height=10"));
        assertTrue(result.contains("cellSize=10"));
    }

    @Test
    void testGetPixelHeightWithHUD() {
        GridConfig config = new GridConfig(10, 10, 50);
        int heightWithHUD = config.getPixelHeightWithHUD(10);
        assertEquals(510, heightWithHUD);
    }

    @Test
    void testGetPixelHeightWithHUDDoesNotModifyOriginalHeight() {
        GridConfig config = new GridConfig(10, 10, 50);
        int originalHeight = config.getPixelHeight();
        config.getPixelHeightWithHUD(10);
        assertEquals(originalHeight, config.getPixelHeight());
    }
}
