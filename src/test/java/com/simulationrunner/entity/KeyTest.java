package com.simulationrunner.entity;

import com.simulationrunner.ColorPalette;
import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void testConstructorWithValidCoordinates() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        assertEquals(5, key.getGridX());
        assertEquals(7, key.getGridY());
        assertFalse(key.isCollected());
    }

    @Test
    void testConstructorWithNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> new Key(new GridPosition(-1, 5), Color.GOLD));
    }

    @Test
    void testConstructorWithNegativeY() {
        assertThrows(IllegalArgumentException.class, () -> new Key(new GridPosition(5, -1), Color.GOLD));
    }

    @Test
    void testConstructorWithBothNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Key(new GridPosition(-1, -1), Color.GOLD));
    }

    @Test
    void testConstructorWithZeroCoordinates() {
        Key key = new Key(new GridPosition(0, 0), Color.GOLD);
        assertEquals(0, key.getGridX());
        assertEquals(0, key.getGridY());
    }

    @Test
    void testCollectChangesState() {
        Key key = new Key(new GridPosition(3, 4), Color.GOLD);
        assertFalse(key.isCollected());

        key.collect();
        assertTrue(key.isCollected());
    }

    @Test
    void testCollectIsIdempotent() {
        Key key = new Key(new GridPosition(3, 4), Color.GOLD);

        key.collect();
        assertTrue(key.isCollected());

        key.collect(); // Collect again
        assertTrue(key.isCollected());
    }

    @Test
    void testCreateRandomKeysWithNullConfig() {
        Player player = new Player(new GridPosition(5, 5));
        assertThrows(NullPointerException.class, () -> Key.createRandomKeys(null, player, 1));
    }

    @Test
    void testCreateRandomKeysWithNullPlayer() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(NullPointerException.class, () -> Key.createRandomKeys(config, null, 1));
    }

    @Test
    void testCreateRandomKeysWithNegativeCount() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(5, 5));
        assertThrows(IllegalArgumentException.class, () -> Key.createRandomKeys(config, player, -1));
    }

    @Test
    void testCreateRandomKeysWithZeroCount() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(new GridPosition(5, 5));
        List<Key> keys = Key.createRandomKeys(config, player, 0);
        assertNotNull(keys);
        assertEquals(0, keys.size());
    }

    @Test
    void testCreateRandomKeysWithSingleKey() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));
        List<Key> keys = Key.createRandomKeys(config, player, 1);

        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertNotNull(keys.get(0));
        assertFalse(keys.get(0).isCollected());
    }

    @Test
    void testCreateRandomKeysWithMultipleKeys() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));
        List<Key> keys = Key.createRandomKeys(config, player, 5);

        assertNotNull(keys);
        assertEquals(5, keys.size());

        for (Key key : keys) {
            assertNotNull(key);
            assertFalse(key.isCollected());
        }
    }

    @RepeatedTest(50)
    void testCreateRandomKeysWithinBounds() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));
        List<Key> keys = Key.createRandomKeys(config, player, 3);

        for (Key key : keys) {
            assertTrue(key.getGridX() >= 0 && key.getGridX() < config.getGridWidth(),
                    "Key X coordinate should be within grid bounds");
            assertTrue(key.getGridY() >= 0 && key.getGridY() < config.getGridHeight(),
                    "Key Y coordinate should be within grid bounds");
        }
    }

    @RepeatedTest(50)
    void testCreateRandomKeysMinimumDistanceFromPlayer() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));
        List<Key> keys = Key.createRandomKeys(config, player, 3);

        for (Key key : keys) {
            int distance = Math.abs(key.getGridX() - player.getGridX()) +
                          Math.abs(key.getGridY() - player.getGridY());
            assertTrue(distance >= 5,
                    "Key should spawn at least 5 tiles from player (Manhattan distance). " +
                    "Player: (" + player.getGridX() + "," + player.getGridY() + "), " +
                    "Key: (" + key.getGridX() + "," + key.getGridY() + "), " +
                    "Distance: " + distance);
        }
    }

    @Test
    void testCreateRandomKeysOnSmallGrid() {
        // 5x5 grid where 5-tile distance is impossible from center
        GridConfig config = new GridConfig(5, 5, 50);
        Player player = new Player(new GridPosition(2, 2));

        // Should not throw exception even though constraint cannot be satisfied
        assertDoesNotThrow(() -> {
            List<Key> keys = Key.createRandomKeys(config, player, 1);
            assertNotNull(keys);
            assertEquals(1, keys.size());
        });
    }

    @Test
    void testCreateRandomKeysOnVerySmallGrid() {
        // 3x3 grid where 5-tile distance is definitely impossible
        GridConfig config = new GridConfig(3, 3, 50);
        Player player = new Player(new GridPosition(1, 1));

        List<Key> keys = Key.createRandomKeys(config, player, 2);
        assertNotNull(keys);
        assertEquals(2, keys.size());

        // Keys should still be within bounds
        for (Key key : keys) {
            assertTrue(key.getGridX() >= 0 && key.getGridX() < 3);
            assertTrue(key.getGridY() >= 0 && key.getGridY() < 3);
        }
    }

    @Test
    void testCreateRandomKeysOnLargeGrid() {
        GridConfig config = new GridConfig(50, 50, 50);
        Player player = new Player(new GridPosition(25, 25));
        List<Key> keys = Key.createRandomKeys(config, player, 10);

        assertNotNull(keys);
        assertEquals(10, keys.size());

        // All keys should satisfy distance constraint on large grid
        for (Key key : keys) {
            int distance = Math.abs(key.getGridX() - player.getGridX()) +
                          Math.abs(key.getGridY() - player.getGridY());
            assertTrue(distance >= 5);
        }
    }

    @Test
    void testEquals() {
        Key key1 = new Key(new GridPosition(5, 7), Color.GOLD);
        Key key2 = new Key(new GridPosition(5, 7), Color.GOLD);
        Key key3 = new Key(new GridPosition(5, 8), Color.GOLD);

        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
    }

    @Test
    void testEqualsSameObject() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        assertEquals(key, key);
    }

    @Test
    void testEqualsWithNull() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        assertNotEquals(key, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        assertNotEquals(key, "not a key");
    }

    @Test
    void testHashCode() {
        Key key1 = new Key(new GridPosition(5, 7), Color.GOLD);
        Key key2 = new Key(new GridPosition(5, 7), Color.GOLD);

        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void testToString() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        String str = key.toString();

        assertNotNull(str);
        assertTrue(str.contains("5"));
        assertTrue(str.contains("7"));
        assertTrue(str.contains("color"));
        assertTrue(str.contains("collected"));
        assertTrue(str.contains("false"));
    }

    @Test
    void testToStringAfterCollect() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        key.collect();
        String str = key.toString();

        assertNotNull(str);
        assertTrue(str.contains("5"));
        assertTrue(str.contains("7"));
        assertTrue(str.contains("color"));
        assertTrue(str.contains("collected"));
        assertTrue(str.contains("true"));
    }

    @Test
    void testRenderWithNullGraphicsContext() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);
        GridConfig config = new GridConfig(10, 10, 50);

        assertThrows(NullPointerException.class, () -> key.render(null, config));
    }

    @Test
    void testRenderWithNullConfig() {
        Key key = new Key(new GridPosition(5, 7), Color.GOLD);

        assertThrows(NullPointerException.class, () -> key.render(null, null));
    }

    @Test
    void testConstructorWithColor() {
        Key key = new Key(new GridPosition(5, 7), Color.RED);
        assertEquals(5, key.getGridX());
        assertEquals(7, key.getGridY());
        assertEquals(Color.RED, key.getColor());
        assertFalse(key.isCollected());
    }

    @Test
    void testConstructorWithNullColor() {
        assertThrows(NullPointerException.class, () -> new Key(new GridPosition(5, 7), null));
    }

    @Test
    void testGetColor() {
        Key redKey = new Key(new GridPosition(0, 0), Color.RED);
        Key blueKey = new Key(new GridPosition(1, 1), Color.BLUE);

        assertEquals(Color.RED, redKey.getColor());
        assertEquals(Color.BLUE, blueKey.getColor());
    }

    @Test
    void testCreateRandomKeysAssignsUniqueColors() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(new GridPosition(10, 10));
        List<Key> keys = Key.createRandomKeys(config, player, 5);

        assertEquals(5, keys.size());

        // First 5 keys should have first 5 palette colors
        assertEquals(ColorPalette.getKeyColor(0), keys.get(0).getColor());
        assertEquals(ColorPalette.getKeyColor(1), keys.get(1).getColor());
        assertEquals(ColorPalette.getKeyColor(2), keys.get(2).getColor());
        assertEquals(ColorPalette.getKeyColor(3), keys.get(3).getColor());
        assertEquals(ColorPalette.getKeyColor(4), keys.get(4).getColor());
    }

    @Test
    void testCreateRandomKeysColorsCycleAfterPaletteSize() {
        GridConfig config = new GridConfig(50, 50, 50);
        Player player = new Player(new GridPosition(25, 25));
        int paletteSize = ColorPalette.getPaletteSize();

        // Create more keys than palette size
        List<Key> keys = Key.createRandomKeys(config, player, paletteSize + 3);

        assertEquals(paletteSize + 3, keys.size());

        // Colors should cycle
        assertEquals(keys.get(0).getColor(), keys.get(paletteSize).getColor());
        assertEquals(keys.get(1).getColor(), keys.get(paletteSize + 1).getColor());
        assertEquals(keys.get(2).getColor(), keys.get(paletteSize + 2).getColor());
    }
}
