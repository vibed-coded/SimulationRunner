package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Key;
import com.simulationrunner.ui.HUD;
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
    private int width;
    private int height;
    private int cellSize;

    @Override
    public void start(Stage stage) {
        config = new GridConfig(10, 10, 50);
        grid = new Grid(config, 1); // Create grid with 1 key
        hud = new HUD();

        width = config.getPixelWidth();
        height = config.getPixelHeightWithHUD(HUD.getFooterHeight());
        cellSize = config.getCellSize();

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Initial render
        render();

        var scene = new Scene(new StackPane(canvas), width, height);

        // Add keyboard event handler for WASD controls
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> grid.getPlayer().move(0, -1, config, grid.getDoor());  // Up
                case A -> grid.getPlayer().move(-1, 0, config, grid.getDoor());  // Left
                case S -> grid.getPlayer().move(0, 1, config, grid.getDoor());   // Down
                case D -> grid.getPlayer().move(1, 0, config, grid.getDoor());   // Right
            }
            checkKeyPickup(); // Check for key collection after movement
            render(); // Redraw after movement
        });

        stage.setScene(scene);
        stage.setTitle("SimulationRunner");
        stage.show();
    }

    private void checkKeyPickup() {
        var player = grid.getPlayer();

        for (Key key : grid.getKeys()) {
            if (!key.isCollected() &&
                player.getGridX() == key.getGridX() &&
                player.getGridY() == key.getGridY()) {
                key.collect();
                player.addKey(key.getColor()); // Add key to player's inventory
            }
        }
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
    }

    public static void main(String[] args) {
        launch();
    }

}