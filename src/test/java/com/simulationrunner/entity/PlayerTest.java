package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testCreateRandomSpawnsWithinBounds() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = Player.createRandom(config);

        assertTrue(player.gridX() >= 0, "Player X coordinate should be non-negative");
        assertTrue(player.gridX() < config.getGridWidth(), "Player X coordinate should be within grid width");
        assertTrue(player.gridY() >= 0, "Player Y coordinate should be non-negative");
        assertTrue(player.gridY() < config.getGridHeight(), "Player Y coordinate should be within grid height");
    }

    @Test
    void testCreateRandomWithDifferentGridSizes() {
        GridConfig smallGrid = new GridConfig(5, 5, 20);
        Player player1 = Player.createRandom(smallGrid);

        assertTrue(player1.gridX() >= 0 && player1.gridX() < 5);
        assertTrue(player1.gridY() >= 0 && player1.gridY() < 5);

        GridConfig largeGrid = new GridConfig(20, 15, 10);
        Player player2 = Player.createRandom(largeGrid);

        assertTrue(player2.gridX() >= 0 && player2.gridX() < 20);
        assertTrue(player2.gridY() >= 0 && player2.gridY() < 15);
    }

    @Test
    void testCreateRandomWithNullConfigThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            Player.createRandom(null);
        });
    }

    @Test
    void testPlayerConstructorWithValidCoordinates() {
        Player player = new Player(5, 7);

        assertEquals(5, player.gridX());
        assertEquals(7, player.gridY());
    }

    @Test
    void testPlayerConstructorWithZeroCoordinates() {
        Player player = new Player(0, 0);

        assertEquals(0, player.gridX());
        assertEquals(0, player.gridY());
    }

    @Test
    void testPlayerConstructorWithNegativeXThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(-1, 5);
        });
    }

    @Test
    void testPlayerConstructorWithNegativeYThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(5, -1);
        });
    }

    @RepeatedTest(100)
    void testCreateRandomDistributionCoversGrid() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = Player.createRandom(config);

        // Verify player is always within bounds across many iterations
        assertTrue(player.gridX() >= 0 && player.gridX() < 10);
        assertTrue(player.gridY() >= 0 && player.gridY() < 10);
    }

    @Test
    void testPlayerRecordEquality() {
        Player player1 = new Player(3, 4);
        Player player2 = new Player(3, 4);
        Player player3 = new Player(5, 6);

        assertEquals(player1, player2, "Players with same coordinates should be equal");
        assertNotEquals(player1, player3, "Players with different coordinates should not be equal");
    }

    @Test
    void testPlayerRecordHashCode() {
        Player player1 = new Player(3, 4);
        Player player2 = new Player(3, 4);

        assertEquals(player1.hashCode(), player2.hashCode(),
            "Players with same coordinates should have same hash code");
    }

    @Test
    void testPlayerToString() {
        Player player = new Player(7, 9);
        String str = player.toString();

        assertTrue(str.contains("7"), "toString should contain X coordinate");
        assertTrue(str.contains("9"), "toString should contain Y coordinate");
    }
}
