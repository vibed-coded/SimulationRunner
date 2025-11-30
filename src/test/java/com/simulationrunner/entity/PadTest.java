package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PadTest {

    @Test
    void testConstructorWithValidCoordinates() {
        Pad pad = new Pad(new GridPosition(5, 7));
        assertEquals(5, pad.getGridX());
        assertEquals(7, pad.getGridY());
    }

    @Test
    void testConstructorWithNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> new Pad(new GridPosition(-1, 5)));
    }

    @Test
    void testConstructorWithNegativeY() {
        assertThrows(IllegalArgumentException.class, () -> new Pad(new GridPosition(5, -1)));
    }

    @Test
    void testConstructorWithZeroCoordinates() {
        Pad pad = new Pad(new GridPosition(0, 0));
        assertEquals(0, pad.getGridX());
        assertEquals(0, pad.getGridY());
    }

    @Test
    void testConstructorWithNullPosition() {
        assertThrows(NullPointerException.class, () -> new Pad(null));
    }

    @RepeatedTest(100)
    void testCreateOppositePlayerOnLeft() {
        GridConfig config = new GridConfig(10, 10, 50);

        // Place player on left side
        Player player = new Player(new GridPosition(2, 5));

        // Create door in middle
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        // Pad should spawn on right side (opposite from player)
        Pad pad = Pad.createOpposite(config, player, door);

        assertTrue(pad.getGridX() > door.getGridX(),
                  "Pad should be on right side when player is on left");
        assertEquals(config.getGridWidth() - 1, pad.getGridX(),
                    "Pad should be at rightmost edge");
    }

    @RepeatedTest(100)
    void testCreateOppositePlayerOnRight() {
        GridConfig config = new GridConfig(10, 10, 50);

        // Place player on right side
        Player player = new Player(new GridPosition(8, 5));

        // Create door in middle
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        // Pad should spawn on left side (opposite from player)
        Pad pad = Pad.createOpposite(config, player, door);

        assertTrue(pad.getGridX() < door.getGridX(),
                  "Pad should be on left side when player is on right");
        assertEquals(0, pad.getGridX(),
                    "Pad should be at leftmost edge");
    }

    @Test
    void testCreateOppositePlayerAtDoor() {
        GridConfig config = new GridConfig(10, 10, 50);

        // Place player at same x as door (edge case)
        Player player = new Player(new GridPosition(5, 3));
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        // Pad should spawn on left side (default behavior)
        Pad pad = Pad.createOpposite(config, player, door);

        assertEquals(0, pad.getGridX(),
                    "Pad should be at leftmost edge when player at door x");
    }

    @Test
    void testCreateOppositePadYPosition() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(2, 5));
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        Pad pad = Pad.createOpposite(config, player, door);

        assertEquals(config.getGridHeight() / 2, pad.getGridY(),
                    "Pad should be at vertical center of grid");
    }

    @Test
    void testCreateOppositeWithNullConfig() {
        Player player = new Player(new GridPosition(2, 5));
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        assertThrows(NullPointerException.class,
                    () -> Pad.createOpposite(null, player, door));
    }

    @Test
    void testCreateOppositeWithNullPlayer() {
        GridConfig config = new GridConfig(10, 10, 50);
        Door door = new Door(new GridPosition(5, 5), javafx.scene.paint.Color.RED);

        assertThrows(NullPointerException.class,
                    () -> Pad.createOpposite(config, null, door));
    }

    @Test
    void testCreateOppositeWithNullDoor() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(2, 5));

        assertThrows(NullPointerException.class,
                    () -> Pad.createOpposite(config, player, null));
    }

    @Test
    void testEqualityWithSameCoordinates() {
        Pad pad1 = new Pad(new GridPosition(5, 5));
        Pad pad2 = new Pad(new GridPosition(5, 5));

        assertEquals(pad1, pad2);
        assertEquals(pad1.hashCode(), pad2.hashCode());
    }

    @Test
    void testInequalityWithDifferentCoordinates() {
        Pad pad1 = new Pad(new GridPosition(5, 5));
        Pad pad2 = new Pad(new GridPosition(6, 5));

        assertNotEquals(pad1, pad2);
    }

    @Test
    void testToString() {
        Pad pad = new Pad(new GridPosition(3, 7));
        String str = pad.toString();

        assertTrue(str.contains("Pad"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("7"));
    }
}
