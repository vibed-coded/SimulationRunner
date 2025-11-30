package com.simulationrunner.ui;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Key;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

/**
 * Heads-Up Display for showing game status information.
 * Renders a footer at the bottom of the canvas showing collected keys.
 */
public class HUD {
    private static final int FOOTER_HEIGHT = 10;
    private static final int PADDING = 2;
    private static final int KEY_ICON_SIZE = 6;
    private static final int KEY_ICON_SPACING = 2;
    private static final Color FOOTER_BACKGROUND = Color.LIGHTGRAY;
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final double UNCOLLECTED_BRIGHTNESS = 0.4;

    /**
     * Renders the HUD footer showing key collection status.
     *
     * @param gc the graphics context to draw on
     * @param config the grid configuration
     * @param keys the list of keys to display
     * @throws NullPointerException if gc, config, or keys is null
     */
    public void render(GraphicsContext gc, GridConfig config, List<Key> keys) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");
        Objects.requireNonNull(keys, "Keys list cannot be null");

        int gridPixelHeight = config.getPixelHeight();
        int footerY = gridPixelHeight;
        int width = config.getPixelWidth();

        // Draw footer background
        gc.setFill(FOOTER_BACKGROUND);
        gc.fillRect(0, footerY, width, FOOTER_HEIGHT);

        // Draw top border
        gc.setStroke(BORDER_COLOR);
        gc.setLineWidth(1);
        gc.strokeLine(0, footerY, width, footerY);

        // Render key icons
        for (int i = 0; i < keys.size(); i++) {
            Key key = keys.get(i);
            double x = PADDING + (i * (KEY_ICON_SIZE + KEY_ICON_SPACING));
            double y = footerY + (FOOTER_HEIGHT - KEY_ICON_SIZE) / 2.0;  // Vertically centered

            Color keyColor = getKeyColor(key);
            Color iconColor = key.isCollected() ? keyColor : keyColor.deriveColor(0, 1, UNCOLLECTED_BRIGHTNESS, 1);
            gc.setFill(iconColor);
            gc.fillRect(x, y, KEY_ICON_SIZE, KEY_ICON_SIZE);
        }
    }

    /**
     * Gets the color for a key.
     *
     * @param key the key
     * @return the key's color
     */
    private Color getKeyColor(Key key) {
        return key.getColor();
    }

    /**
     * Gets the height of the HUD footer in pixels.
     *
     * @return the footer height
     */
    public static int getFooterHeight() {
        return FOOTER_HEIGHT;
    }
}
