package com.simulationrunner.ui;

import com.simulationrunner.config.GridConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

/**
 * Win banner displayed when the player reaches the goal pad.
 * Shows a congratulatory message and instructions for starting a new level.
 */
public class WinBanner {
    private static final Color BANNER_BACKGROUND = Color.rgb(0, 128, 0, 0.9);
    private static final Color BANNER_BORDER = Color.DARKGREEN;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final int BANNER_HEIGHT = 100;
    private static final int BANNER_PADDING = 20;
    private static final int BORDER_WIDTH = 3;

    /**
     * Renders the win banner in the center of the canvas.
     *
     * @param gc the graphics context to draw on
     * @param config the grid configuration
     * @throws NullPointerException if gc or config is null
     */
    public void render(GraphicsContext gc, GridConfig config) {
        Objects.requireNonNull(gc, "GraphicsContext cannot be null");
        Objects.requireNonNull(config, "GridConfig cannot be null");

        int canvasWidth = config.getPixelWidth();
        int canvasHeight = config.getPixelHeightWithHUD(HUD.getFooterHeight());

        // Calculate banner position (centered on screen)
        double bannerWidth = canvasWidth * 0.8;
        double bannerX = (canvasWidth - bannerWidth) / 2;
        double bannerY = (canvasHeight - BANNER_HEIGHT) / 2;

        // Draw semi-transparent background
        gc.setFill(BANNER_BACKGROUND);
        gc.fillRect(bannerX, bannerY, bannerWidth, BANNER_HEIGHT);

        // Draw border
        gc.setStroke(BANNER_BORDER);
        gc.setLineWidth(BORDER_WIDTH);
        gc.strokeRect(bannerX, bannerY, bannerWidth, BANNER_HEIGHT);

        // Draw "YOU WIN!" text
        gc.setFill(TEXT_COLOR);
        gc.setFont(Font.font("Arial", 32));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("YOU WIN!", canvasWidth / 2.0, bannerY + 35);

        // Draw instruction text
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Press SPACE for a new level", canvasWidth / 2.0, bannerY + 65);
    }
}
