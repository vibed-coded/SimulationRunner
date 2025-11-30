package com.simulationrunner.entity;

import com.simulationrunner.GridPosition;
import com.simulationrunner.config.GridConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WallTest {

    @Test
    void testConstructorWithValidPosition() {
        Wall wall = new Wall(new GridPosition(5, 7));
        assertEquals(5, wall.getGridX());
        assertEquals(7, wall.getGridY());
    }

    @Test
    void testConstructorWithNullPositionThrowsException() {
        assertThrows(NullPointerException.class, () -> new Wall(null));
    }

    @Test
    void testConstructorWithZeroCoordinates() {
        Wall wall = new Wall(new GridPosition(0, 0));
        assertEquals(0, wall.getGridX());
        assertEquals(0, wall.getGridY());
    }

    @Test
    void testCreateVerticalWallWithValidConfig() {
        GridConfig config = new GridConfig(10, 10, 50);
        List<Wall> walls = Wall.createVerticalWall(config, 5);

        assertNotNull(walls);
        assertEquals(10, walls.size(), "Vertical wall should have one segment per row");

        // Verify all walls are at x=5
        for (int i = 0; i < walls.size(); i++) {
            assertEquals(5, walls.get(i).getGridX(), "All wall segments should be at x=5");
            assertEquals(i, walls.get(i).getGridY(), "Wall segments should span from y=0 to y=9");
        }
    }

    @Test
    void testCreateVerticalWallAtLeftEdge() {
        GridConfig config = new GridConfig(10, 10, 50);
        List<Wall> walls = Wall.createVerticalWall(config, 0);

        assertNotNull(walls);
        assertEquals(10, walls.size());

        for (Wall wall : walls) {
            assertEquals(0, wall.getGridX());
        }
    }

    @Test
    void testCreateVerticalWallAtRightEdge() {
        GridConfig config = new GridConfig(10, 10, 50);
        List<Wall> walls = Wall.createVerticalWall(config, 9);

        assertNotNull(walls);
        assertEquals(10, walls.size());

        for (Wall wall : walls) {
            assertEquals(9, wall.getGridX());
        }
    }

    @Test
    void testCreateVerticalWallWithDifferentGridHeights() {
        GridConfig config = new GridConfig(10, 20, 50);
        List<Wall> walls = Wall.createVerticalWall(config, 5);

        assertEquals(20, walls.size(), "Wall should match grid height");

        for (int i = 0; i < 20; i++) {
            assertEquals(5, walls.get(i).getGridX());
            assertEquals(i, walls.get(i).getGridY());
        }
    }

    @Test
    void testCreateVerticalWallWithNullConfigThrowsException() {
        assertThrows(NullPointerException.class, () -> Wall.createVerticalWall(null, 5));
    }

    @Test
    void testCreateVerticalWallWithNegativeXThrowsException() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(IllegalArgumentException.class, () -> Wall.createVerticalWall(config, -1));
    }

    @Test
    void testCreateVerticalWallWithXOutOfBoundsThrowsException() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(IllegalArgumentException.class, () -> Wall.createVerticalWall(config, 10));
    }

    @Test
    void testEquality() {
        Wall wall1 = new Wall(new GridPosition(5, 7));
        Wall wall2 = new Wall(new GridPosition(5, 7));
        Wall wall3 = new Wall(new GridPosition(6, 7));

        assertEquals(wall1, wall2, "Walls at same position should be equal");
        assertNotEquals(wall1, wall3, "Walls at different positions should not be equal");
    }

    @Test
    void testHashCode() {
        Wall wall1 = new Wall(new GridPosition(5, 7));
        Wall wall2 = new Wall(new GridPosition(5, 7));

        assertEquals(wall1.hashCode(), wall2.hashCode(),
            "Walls at same position should have same hash code");
    }

    @Test
    void testToString() {
        Wall wall = new Wall(new GridPosition(3, 7));
        String str = wall.toString();

        assertNotNull(str);
        assertTrue(str.contains("Wall"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("7"));
    }

    @Test
    void testRenderWithNullGraphicsContextThrowsException() {
        Wall wall = new Wall(new GridPosition(5, 7));
        GridConfig config = new GridConfig(10, 10, 50);

        assertThrows(NullPointerException.class, () -> wall.render(null, config));
    }

    @Test
    void testRenderWithNullConfigThrowsException() {
        Wall wall = new Wall(new GridPosition(5, 7));

        assertThrows(NullPointerException.class, () -> wall.render(null, null));
    }

    @Test
    void testCreateVerticalWallWithGap() {
        GridConfig config = new GridConfig(10, 10, 50);
        GridPosition gapPosition = new GridPosition(5, 3);
        List<Wall> walls = Wall.createVerticalWallWithGap(config, 5, gapPosition);

        assertNotNull(walls);
        assertEquals(9, walls.size(), "Wall should have 9 segments (10 total - 1 gap)");

        // Verify all walls are at x=5 and none at the gap position
        for (Wall wall : walls) {
            assertEquals(5, wall.getGridX());
            assertNotEquals(gapPosition, wall.getPosition(), "No wall should be at gap position");
        }

        // Verify the gap position is actually missing
        boolean hasGapPosition = walls.stream()
            .anyMatch(wall -> wall.getPosition().isSameAs(gapPosition));
        assertFalse(hasGapPosition, "Gap position should not have a wall");
    }

    @Test
    void testCreateVerticalWallWithGapAtTop() {
        GridConfig config = new GridConfig(10, 10, 50);
        GridPosition gapPosition = new GridPosition(5, 0);
        List<Wall> walls = Wall.createVerticalWallWithGap(config, 5, gapPosition);

        assertEquals(9, walls.size());
        assertTrue(walls.stream().noneMatch(w -> w.getGridY() == 0));
    }

    @Test
    void testCreateVerticalWallWithGapAtBottom() {
        GridConfig config = new GridConfig(10, 10, 50);
        GridPosition gapPosition = new GridPosition(5, 9);
        List<Wall> walls = Wall.createVerticalWallWithGap(config, 5, gapPosition);

        assertEquals(9, walls.size());
        assertTrue(walls.stream().noneMatch(w -> w.getGridY() == 9));
    }

    @Test
    void testCreateVerticalWallWithGapNullConfigThrowsException() {
        GridPosition gapPosition = new GridPosition(5, 5);
        assertThrows(NullPointerException.class,
            () -> Wall.createVerticalWallWithGap(null, 5, gapPosition));
    }

    @Test
    void testCreateVerticalWallWithGapNullGapPositionThrowsException() {
        GridConfig config = new GridConfig(10, 10, 50);
        assertThrows(NullPointerException.class,
            () -> Wall.createVerticalWallWithGap(config, 5, null));
    }

    @Test
    void testCreateVerticalWallWithGapInvalidXThrowsException() {
        GridConfig config = new GridConfig(10, 10, 50);
        GridPosition gapPosition = new GridPosition(5, 5);
        assertThrows(IllegalArgumentException.class,
            () -> Wall.createVerticalWallWithGap(config, -1, gapPosition));
    }
}
