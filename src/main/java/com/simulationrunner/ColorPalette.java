package com.simulationrunner;

import javafx.scene.paint.Color;

/**
 * Provides color palette for game entities.
 * Currently supports rainbow palette for keys with cyclic assignment.
 */
public final class ColorPalette {
    private ColorPalette() {
        throw new AssertionError("Cannot instantiate ColorPalette class");
    }

    private static final Color[] RAINBOW_PALETTE = {
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.PURPLE,
        Color.MAGENTA
    };

    /**
     * Gets a color from the rainbow palette by index.
     * If index exceeds palette size, cycles back using modulo.
     *
     * @param index the color index
     * @return the color at the given index (with cycling)
     * @throws IllegalArgumentException if index is negative
     */
    public static Color getKeyColor(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index must be non-negative");
        }
        return RAINBOW_PALETTE[index % RAINBOW_PALETTE.length];
    }

    /**
     * Gets the total number of colors in the rainbow palette.
     *
     * @return the palette size
     */
    public static int getPaletteSize() {
        return RAINBOW_PALETTE.length;
    }
}
