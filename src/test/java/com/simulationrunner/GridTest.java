package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Key;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void testGridConstructorWithValidConfig() {
        GridConfig config = new GridConfig(10, 10, 50);
        Grid grid = new Grid(config, 1);

        assertNotNull(grid.getPlayer());
        assertNotNull(grid.getDoor());
        assertEquals(1, grid.getKeys().size());
        assertFalse(grid.getWalls().isEmpty());
    }

    @Test
    void testGridConstructorWithZeroKeys() {
        GridConfig config = new GridConfig(10, 10, 50);
        Grid grid = new Grid(config, 0);

        assertNotNull(grid.getPlayer());
        assertNull(grid.getDoor());
        assertTrue(grid.getKeys().isEmpty());
        assertTrue(grid.getWalls().isEmpty());
    }

    @Test
    void testGridConstructorWithNullConfig() {
        assertThrows(IllegalArgumentException.class, () -> new Grid(null, 1));
    }

    @Test
    void testGridConstructorWithNegativeKeyCount() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(IllegalArgumentException.class, () -> new Grid(config, -1));
    }

    @RepeatedTest(100)
    void testKeysSpawnOnPlayerSide() {
        GridConfig config = new GridConfig(10, 10, 50);
        Grid grid = new Grid(config, 1);

        int playerX = grid.getPlayer().getGridX();
        int doorX = grid.getDoor().getGridX();

        // Verify the key spawns on the same side as the player
        for (Key key : grid.getKeys()) {
            int keyX = key.getGridX();

            if (playerX < doorX) {
                // Player is on the left side, key should be on the left
                assertTrue(keyX < doorX,
                          "Key should spawn on left side (x < " + doorX + ") but was at x=" + keyX);
            } else if (playerX > doorX) {
                // Player is on the right side, key should be on the right
                assertTrue(keyX > doorX,
                          "Key should spawn on right side (x > " + doorX + ") but was at x=" + keyX);
            } else {
                // Player is at the same x as door (rare case)
                // Key should be on the left side as per default behavior
                assertTrue(keyX <= doorX,
                          "Key should spawn on left side when player is at door x");
            }
        }
    }

    @RepeatedTest(100)
    void testDoorHasRoomBehind() {
        GridConfig config = new GridConfig(10, 10, 50);
        Grid grid = new Grid(config, 1);

        int doorX = grid.getDoor().getGridX();

        // Door should have at least 1 cell on each side
        assertTrue(doorX >= 1, "Door should have at least 1 cell on left (x >= 1)");
        assertTrue(doorX <= config.getGridWidth() - 2,
                  "Door should have at least 1 cell on right (x <= " + (config.getGridWidth() - 2) + ")");
    }
}
