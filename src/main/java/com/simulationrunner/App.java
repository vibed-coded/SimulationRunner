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

    @Override
    public void start(Stage stage) {
        GridConfig config = new GridConfig(10, 10, 50);
        Grid grid = new Grid(config);

        int width = config.getPixelWidth();
        int height = config.getPixelHeight();
        int cellSize = config.getCellSize();

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

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

        var scene = new Scene(new StackPane(canvas), width, height);
        stage.setScene(scene);
        stage.setTitle("SimulationRunner");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}