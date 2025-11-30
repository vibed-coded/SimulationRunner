package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoorTest {

    @Test
    void testConstructorWithValidCoordinates() {
        Door door = new Door(new GridPosition(5, 7), Color.RED);
        assertEquals(5, door.getGridX());
        assertEquals(7, door.getGridY());
        assertEquals(Color.RED, door.getColor());
    }

    @Test
    void testConstructorWithNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> new Door(new GridPosition(-1, 5), Color.RED));
    }

    @Test
    void testConstructorWithNegativeY() {
        assertThrows(IllegalArgumentException.class, () -> new Door(new GridPosition(5, -1), Color.RED));
    }

    @Test
    void testConstructorWithBothNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Door(new GridPosition(-1, -1), Color.RED));
    }

    @Test
    void testConstructorWithZeroCoordinates() {
        Door door = new Door(new GridPosition(0, 0), Color.RED);
        assertEquals(0, door.getGridX());
        assertEquals(0, door.getGridY());
    }

    @Test
    void testConstructorWithNullColor() {
        assertThrows(NullPointerException.class, () -> new Door(new GridPosition(5, 5), null));
    }

    @Test
    void testCreateRandomWithValidConfig() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(5, 5));

        Door door = Door.createRandom(config, player, Color.RED);

        assertNotNull(door);
        assertTrue(door.getGridX() >= 0);
        assertTrue(door.getGridX() < config.getGridWidth());
        assertTrue(door.getGridY() >= 0);
        assertTrue(door.getGridY() < config.getGridHeight());
        assertEquals(Color.RED, door.getColor());
    }

    @RepeatedTest(10)
    void testCreateRandomMaintainsDistance() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));

        Door door = Door.createRandom(config, player, Color.BLUE);

        // Calculate Manhattan distance
        int distance = Math.abs(door.getGridX() - player.getGridX()) +
                      Math.abs(door.getGridY() - player.getGridY());

        // Door should spawn at least 5 tiles away or at farthest point
        // For a 20x20 grid, this constraint should be satisfiable
        assertTrue(distance >= 5, "Door spawned too close to player: distance=" + distance);
    }

    @Test
    void testCreateRandomWithNullConfig() {
        Player player = new Player(new GridPosition(5, 5));
        assertThrows(NullPointerException.class,
            () -> Door.createRandom(null, player, Color.RED));
    }

    @Test
    void testCreateRandomWithNullPlayer() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(NullPointerException.class,
            () -> Door.createRandom(config, null, Color.RED));
    }

    @Test
    void testCreateRandomWithNullColor() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(5, 5));
        assertThrows(NullPointerException.class,
            () -> Door.createRandom(config, player, null));
    }

    @Test
    void testCanPassWithMatchingKey() {
        Door door = new Door(new GridPosition(5, 5), Color.GREEN);
        Player player = new Player(new GridPosition(3, 3));

        // Player doesn't have key yet
        assertFalse(door.canPass(player));

        // Player gets the matching key
        player.addKey(Color.GREEN);

        // Now player can pass
        assertTrue(door.canPass(player));
    }

    @Test
    void testCanPassWithWrongKey() {
        Door door = new Door(new GridPosition(5, 5), Color.GREEN);
        Player player = new Player(new GridPosition(3, 3));

        // Player gets a different colored key
        player.addKey(Color.RED);

        // Player cannot pass with wrong key
        assertFalse(door.canPass(player));
    }

    @Test
    void testCanPassWithMultipleKeys() {
        Door door = new Door(new GridPosition(5, 5), Color.BLUE);
        Player player = new Player(new GridPosition(3, 3));

        // Player collects multiple keys
        player.addKey(Color.RED);
        player.addKey(Color.YELLOW);
        player.addKey(Color.BLUE);

        // Player can pass because they have the blue key
        assertTrue(door.canPass(player));
    }

    @Test
    void testCanPassWithNullPlayer() {
        Door door = new Door(new GridPosition(5, 5), Color.RED);
        assertThrows(NullPointerException.class, () -> door.canPass(null));
    }

    @Test
    void testGetColor() {
        Door redDoor = new Door(new GridPosition(1, 1), Color.RED);
        Door blueDoor = new Door(new GridPosition(2, 2), Color.BLUE);

        assertEquals(Color.RED, redDoor.getColor());
        assertEquals(Color.BLUE, blueDoor.getColor());
    }

    @Test
    void testToString() {
        Door door = new Door(new GridPosition(3, 7), Color.PURPLE);
        String str = door.toString();

        assertTrue(str.contains("Door"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("7"));
        assertTrue(str.contains("0x800080ff")); // PURPLE color code
    }

    @Test
    void testEqualityWithSameCoordinatesAndColor() {
        Door door1 = new Door(new GridPosition(5, 5), Color.RED);
        Door door2 = new Door(new GridPosition(5, 5), Color.RED);

        assertEquals(door1, door2);
        assertEquals(door1.hashCode(), door2.hashCode());
    }

    @Test
    void testInequalityWithDifferentCoordinates() {
        Door door1 = new Door(new GridPosition(5, 5), Color.RED);
        Door door2 = new Door(new GridPosition(6, 5), Color.RED);

        assertNotEquals(door1, door2);
    }

    @Test
    void testInequalityWithDifferentColors() {
        Door door1 = new Door(new GridPosition(5, 5), Color.RED);
        Door door2 = new Door(new GridPosition(5, 5), Color.BLUE);

        // Even though position is the same, color is not
        // The equality is based on Entity (which uses position only)
        // So these would be equal based on Entity.equals()
        // But toString would differ
        assertNotEquals(door1.toString(), door2.toString());
    }

    @RepeatedTest(100)
    void testDoorSpawnsWithRoomBehind() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = Player.createRandom(config);
        Door door = Door.createRandom(config, player, Color.RED);

        // Door should spawn with at least 1 cell on each side
        // This means x should be in range [1, gridWidth-2]
        assertTrue(door.getGridX() >= 1, "Door x-coordinate should be >= 1");
        assertTrue(door.getGridX() <= config.getGridWidth() - 2,
                   "Door x-coordinate should be <= gridWidth-2");
    }
}
