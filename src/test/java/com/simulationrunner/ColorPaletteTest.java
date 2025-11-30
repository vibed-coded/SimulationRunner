package com.simulationrunner;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorPaletteTest {

    @Test
    void testGetKeyColorWithValidIndex() {
        Color color0 = ColorPalette.getKeyColor(0);
        Color color1 = ColorPalette.getKeyColor(1);

        assertNotNull(color0);
        assertNotNull(color1);
        assertNotEquals(color0, color1);
    }

    @Test
    void testGetKeyColorWithNegativeIndex() {
        assertThrows(IllegalArgumentException.class, () -> ColorPalette.getKeyColor(-1));
    }

    @Test
    void testGetKeyColorCycles() {
        int paletteSize = ColorPalette.getPaletteSize();

        // Color at index 0 should equal color at index paletteSize
        assertEquals(ColorPalette.getKeyColor(0), ColorPalette.getKeyColor(paletteSize));
        assertEquals(ColorPalette.getKeyColor(1), ColorPalette.getKeyColor(paletteSize + 1));
        assertEquals(ColorPalette.getKeyColor(2), ColorPalette.getKeyColor(paletteSize + 2));
    }

    @Test
    void testGetKeyColorLargeIndex() {
        int paletteSize = ColorPalette.getPaletteSize();

        // Test with very large index
        Color color = ColorPalette.getKeyColor(100);
        assertNotNull(color);

        // Should be same as (100 % paletteSize)
        assertEquals(ColorPalette.getKeyColor(100 % paletteSize), color);
    }

    @Test
    void testGetPaletteSizeIsPositive() {
        int size = ColorPalette.getPaletteSize();
        assertTrue(size > 0, "Palette size should be positive");
        assertTrue(size >= 7, "Palette should have at least 7 rainbow colors");
    }

    @Test
    void testPaletteHasUniqueColors() {
        int paletteSize = ColorPalette.getPaletteSize();

        // Get all colors in palette
        Color[] colors = new Color[paletteSize];
        for (int i = 0; i < paletteSize; i++) {
            colors[i] = ColorPalette.getKeyColor(i);
        }

        // Check all colors are unique
        for (int i = 0; i < paletteSize; i++) {
            for (int j = i + 1; j < paletteSize; j++) {
                assertNotEquals(colors[i], colors[j],
                    "Colors at indices " + i + " and " + j + " should be different");
            }
        }
    }

    @Test
    void testConstructorThrowsException() {
        // Verify utility class cannot be instantiated
        java.lang.reflect.InvocationTargetException exception =
            assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
                java.lang.reflect.Constructor<ColorPalette> constructor =
                    ColorPalette.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            });

        // Verify the cause is AssertionError
        assertTrue(exception.getCause() instanceof AssertionError,
            "Constructor should throw AssertionError");
    }
}
