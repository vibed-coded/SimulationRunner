package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Key;
import com.simulationrunner.ui.HUD;
import com.simulationrunner.ui.WinBanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    private Grid grid;
    private GridConfig config;
    private GraphicsContext gc;
    private HUD hud;
    private WinBanner winBanner;
    private int width;
    private int height;
    private int cellSize;
    private boolean hasWon;

    @Override
    public void start(Stage stage) {
        config = new GridConfig(10, 10, 50);
        grid = new Grid(config, 1); // Create grid with 1 key
        hud = new HUD();
        winBanner = new WinBanner();
        hasWon = false;

        width = config.getPixelWidth();
        height = config.getPixelHeightWithHUD(HUD.getFooterHeight());
        cellSize = config.getCellSize();

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Initial render
        render();

        var scene = new Scene(new StackPane(canvas), width, height);

        // Add keyboard event handler for WASD controls and SPACE for new level
        scene.setOnKeyPressed(event -> {
            if (hasWon) {
                // If player has won, only respond to SPACE key for new level
                if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                    regenerateLevel();
                    render();
                }
            } else {
                // Normal gameplay controls
                switch (event.getCode()) {
                    case W -> grid.getPlayer().move(0, -1, config, grid.getDoor(), grid.getWalls());  // Up
                    case A -> grid.getPlayer().move(-1, 0, config, grid.getDoor(), grid.getWalls());  // Left
                    case S -> grid.getPlayer().move(0, 1, config, grid.getDoor(), grid.getWalls());   // Down
                    case D -> grid.getPlayer().move(1, 0, config, grid.getDoor(), grid.getWalls());   // Right
                }
                checkKeyPickup(); // Check for key collection after movement
                checkWinCondition(); // Check if player reached the pad
                render(); // Redraw after movement
            }
        });

        stage.setScene(scene);
        stage.setTitle("SimulationRunner");
        stage.show();
    }

    private void checkKeyPickup() {
        var player = grid.getPlayer();

        for (Key key : grid.getKeys()) {
            if (!key.isCollected() &&
                player.getPosition().isSameAs(key.getPosition())) {
                key.collect();
                player.addKey(key.getColor()); // Add key to player's inventory
            }
        }
    }

    private void checkWinCondition() {
        if (grid.getPad() != null &&
            grid.getPlayer().getPosition().isSameAs(grid.getPad().getPosition())) {
            hasWon = true;
        }
    }

    private void regenerateLevel() {
        grid = new Grid(config, 1); // Create new grid with 1 key
        hasWon = false;
    }

    private void render() {
        int gridPixelHeight = config.getPixelHeight();

        // Clear canvas (including HUD area)
        gc.clearRect(0, 0, width, height);

        // Draw grid lines (only in grid area, not footer)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (int x = 0; x <= width; x += cellSize) {
            gc.strokeLine(x, 0, x, gridPixelHeight);
        }

        for (int y = 0; y <= gridPixelHeight; y += cellSize) {
            gc.strokeLine(0, y, width, y);
        }

        // Render all walls first (behind other entities)
        for (var wall : grid.getWalls()) {
            wall.render(gc, config);
        }

        // Render the pad (if it exists)
        if (grid.getPad() != null) {
            grid.getPad().render(gc, config);
        }

        // Render all keys
        for (Key key : grid.getKeys()) {
            key.render(gc, config);
        }

        // Render the door (if it exists)
        if (grid.getDoor() != null) {
            grid.getDoor().render(gc, config);
        }

        // Render the player
        grid.getPlayer().render(gc, config);

        // Render HUD
        hud.render(gc, config, grid.getKeys());

        // Render win banner if player has won
        if (hasWon) {
            winBanner.render(gc, config);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}