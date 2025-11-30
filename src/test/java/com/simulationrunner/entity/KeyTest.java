package com.simulationrunner.entity;

import com.simulationrunner.config.GridConfig;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    @Test
    void testConstructorWithValidCoordinates() {
        Key key = new Key(5, 7);
        assertEquals(5, key.getGridX());
        assertEquals(7, key.getGridY());
        assertFalse(key.isCollected());
    }

    @Test
    void testConstructorWithNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> new Key(-1, 5));
    }

    @Test
    void testConstructorWithNegativeY() {
        assertThrows(IllegalArgumentException.class, () -> new Key(5, -1));
    }

    @Test
    void testConstructorWithBothNegative() {
        assertThrows(IllegalArgumentException.class, () -> new Key(-1, -1));
    }

    @Test
    void testConstructorWithZeroCoordinates() {
        Key key = new Key(0, 0);
        assertEquals(0, key.getGridX());
        assertEquals(0, key.getGridY());
    }

    @Test
    void testCollectChangesState() {
        Key key = new Key(3, 4);
        assertFalse(key.isCollected());

        key.collect();
        assertTrue(key.isCollected());
    }

    @Test
    void testCollectIsIdempotent() {
        Key key = new Key(3, 4);

        key.collect();
        assertTrue(key.isCollected());

        key.collect(); // Collect again
        assertTrue(key.isCollected());
    }

    @Test
    void testCreateRandomKeysWithNullConfig() {
        Player player = new Player(5, 5);
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
        Player player = new Player(5, 5);
        assertThrows(IllegalArgumentException.class, () -> Key.createRandomKeys(config, player, -1));
    }

    @Test
    void testCreateRandomKeysWithZeroCount() {
        GridConfig config = new GridConfig(10, 10, 50);
        Player player = new Player(5, 5);
        List<Key> keys = Key.createRandomKeys(config, player, 0);
        assertNotNull(keys);
        assertEquals(0, keys.size());
    }

    @Test
    void testCreateRandomKeysWithSingleKey() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(10, 10);
        List<Key> keys = Key.createRandomKeys(config, player, 1);

        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertNotNull(keys.get(0));
        assertFalse(keys.get(0).isCollected());
    }

    @Test
    void testCreateRandomKeysWithMultipleKeys() {
        GridConfig config = new GridConfig(20, 20, 50);
        Player player = new Player(10, 10);
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
        Player player = new Player(10, 10);
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
        Player player = new Player(10, 10);
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
        Player player = new Player(2, 2);

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
        Player player = new Player(1, 1);

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
        Player player = new Player(25, 25);
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
        Key key1 = new Key(5, 7);
        Key key2 = new Key(5, 7);
        Key key3 = new Key(5, 8);

        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
    }

    @Test
    void testEqualsSameObject() {
        Key key = new Key(5, 7);
        assertEquals(key, key);
    }

    @Test
    void testEqualsWithNull() {
        Key key = new Key(5, 7);
        assertNotEquals(key, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Key key = new Key(5, 7);
        assertNotEquals(key, "not a key");
    }

    @Test
    void testHashCode() {
        Key key1 = new Key(5, 7);
        Key key2 = new Key(5, 7);

        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    void testToString() {
        Key key = new Key(5, 7);
        String str = key.toString();

        assertNotNull(str);
        assertTrue(str.contains("5"));
        assertTrue(str.contains("7"));
        assertTrue(str.contains("collected"));
        assertTrue(str.contains("false"));
    }

    @Test
    void testToStringAfterCollect() {
        Key key = new Key(5, 7);
        key.collect();
        String str = key.toString();

        assertNotNull(str);
        assertTrue(str.contains("5"));
        assertTrue(str.contains("7"));
        assertTrue(str.contains("collected"));
        assertTrue(str.contains("true"));
    }

    @Test
    void testRenderWithNullGraphicsContext() {
        Key key = new Key(5, 7);
        GridConfig config = new GridConfig(10, 10, 50);

        assertThrows(NullPointerException.class, () -> key.render(null, config));
    }

    @Test
    void testRenderWithNullConfig() {
        Key key = new Key(5, 7);

        assertThrows(NullPointerException.class, () -> key.render(null, null));
    }
}
