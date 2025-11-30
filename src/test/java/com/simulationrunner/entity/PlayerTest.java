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

        assertTrue(player.getGridX() >= 0, "Player X coordinate should be non-negative");
        assertTrue(player.getGridX() < config.getGridWidth(), "Player X coordinate should be within grid width");
        assertTrue(player.getGridY() >= 0, "Player Y coordinate should be non-negative");
        assertTrue(player.getGridY() < config.getGridHeight(), "Player Y coordinate should be within grid height");
    }

    @Test
    void testCreateRandomWithDifferentGridSizes() {
        GridConfig smallGrid = new GridConfig(5, 5, 20);
        Player player1 = Player.createRandom(smallGrid);

        assertTrue(player1.getGridX() >= 0 && player1.getGridX() < 5);
        assertTrue(player1.getGridY() >= 0 && player1.getGridY() < 5);

        GridConfig largeGrid = new GridConfig(20, 15, 10);
        Player player2 = Player.createRandom(largeGrid);

        assertTrue(player2.getGridX() >= 0 && player2.getGridX() < 20);
        assertTrue(player2.getGridY() >= 0 && player2.getGridY() < 15);
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

        assertEquals(5, player.getGridX());
        assertEquals(7, player.getGridY());
    }

    @Test
    void testPlayerConstructorWithZeroCoordinates() {
        Player player = new Player(0, 0);

        assertEquals(0, player.getGridX());
        assertEquals(0, player.getGridY());
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
        assertTrue(player.getGridX() >= 0 && player.getGridX() < 10);
        assertTrue(player.getGridY() >= 0 && player.getGridY() < 10);
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

    @Test
    void testMoveWithinBounds() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(5, 5);

        player.move(1, 0, config);  // Move right
        assertEquals(6, player.getGridX());
        assertEquals(5, player.getGridY());

        player.move(0, 1, config);  // Move down
        assertEquals(6, player.getGridX());
        assertEquals(6, player.getGridY());

        player.move(-1, 0, config); // Move left
        assertEquals(5, player.getGridX());
        assertEquals(6, player.getGridY());

        player.move(0, -1, config); // Move up
        assertEquals(5, player.getGridX());
        assertEquals(5, player.getGridY());
    }

    @Test
    void testMoveBeyondLeftBoundary() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(0, 5);

        player.move(-1, 0, config); // Try to move left beyond boundary
        assertEquals(0, player.getGridX(), "Player should stay at left boundary");
        assertEquals(5, player.getGridY());
    }

    @Test
    void testMoveBeyondRightBoundary() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(9, 5);

        player.move(1, 0, config); // Try to move right beyond boundary
        assertEquals(9, player.getGridX(), "Player should stay at right boundary");
        assertEquals(5, player.getGridY());
    }

    @Test
    void testMoveBeyondTopBoundary() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(5, 0);

        player.move(0, -1, config); // Try to move up beyond boundary
        assertEquals(5, player.getGridX());
        assertEquals(0, player.getGridY(), "Player should stay at top boundary");
    }

    @Test
    void testMoveBeyondBottomBoundary() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(5, 9);

        player.move(0, 1, config); // Try to move down beyond boundary
        assertEquals(5, player.getGridX());
        assertEquals(9, player.getGridY(), "Player should stay at bottom boundary");
    }

    @Test
    void testMoveWithNullConfigThrowsException() {
        Player player = new Player(5, 5);

        assertThrows(NullPointerException.class, () -> {
            player.move(1, 0, null);
        });
    }

    @Test
    void testMoveMultipleSteps() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(0, 0);

        // Move to bottom-right corner
        for (int i = 0; i < 9; i++) {
            player.move(1, 0, config);
        }
        for (int i = 0; i < 9; i++) {
            player.move(0, 1, config);
        }

        assertEquals(9, player.getGridX());
        assertEquals(9, player.getGridY());

        // Try to move beyond
        player.move(1, 1, config);
        assertEquals(9, player.getGridX(), "Should still be at corner");
        assertEquals(9, player.getGridY(), "Should still be at corner");
    }
}
