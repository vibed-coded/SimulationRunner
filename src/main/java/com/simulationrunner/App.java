package com.simulationrunner;

import com.simulationrunner.config.GridConfig;
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
    private int width;
    private int height;
    private int cellSize;

    @Override
    public void start(Stage stage) {
        config = new GridConfig(10, 10, 50);
        grid = new Grid(config);

        width = config.getPixelWidth();
        height = config.getPixelHeight();
        cellSize = config.getCellSize();

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Initial render
        render();

        var scene = new Scene(new StackPane(canvas), width, height);

        // Add keyboard event handler for WASD controls
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> grid.getPlayer().move(0, -1, config);  // Up
                case A -> grid.getPlayer().move(-1, 0, config);  // Left
                case S -> grid.getPlayer().move(0, 1, config);   // Down
                case D -> grid.getPlayer().move(1, 0, config);   // Right
            }
            render(); // Redraw after movement
        });

        stage.setScene(scene);
        stage.setTitle("SimulationRunner");
        stage.show();
    }

    private void render() {
        // Clear canvas
        gc.clearRect(0, 0, width, height);

        // Draw grid lines
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (int x = 0; x <= width; x += cellSize) {
            gc.strokeLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += cellSize) {
            gc.strokeLine(0, y, width, y);
        }

        // Render the player
        grid.getPlayer().render(gc, config);
    }

    public static void main(String[] args) {
        launch();
    }

}