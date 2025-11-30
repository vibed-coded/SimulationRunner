package com.simulationrunner.ui;

import com.simulationrunner.config.GridConfig;
import com.simulationrunner.entity.Key;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HUDTest {

    @Test
    void testGetFooterHeight() {
        assertEquals(10, HUD.getFooterHeight());
    }

    @Test
    void testRenderWithNullGraphicsContextThrowsException() {
        HUD hud = new HUD();
        GridConfig config = new GridConfig(10, 10, 50);
        List<Key> keys = new ArrayList<>();

        assertThrows(NullPointerException.class,
            () -> hud.render(null, config, keys));
    }

    @Test
    void testRenderWithNullGridConfigThrowsException() {
        HUD hud = new HUD();
        Canvas canvas = new Canvas(500, 510);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<Key> keys = new ArrayList<>();

        assertThrows(NullPointerException.class,
            () -> hud.render(gc, null, keys));
    }

    @Test
    void testRenderWithNullKeysListThrowsException() {
        HUD hud = new HUD();
        Canvas canvas = new Canvas(500, 510);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GridConfig config = new GridConfig(10, 10, 50);

        assertThrows(NullPointerException.class,
            () -> hud.render(gc, config, null));
    }

    @Test
    void testRenderWithZeroKeysCollected() {
        HUD hud = new HUD();
        Canvas canvas = new Canvas(500, 510);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GridConfig config = new GridConfig(10, 10, 50);

        List<Key> keys = new ArrayList<>();
        keys.add(new Key(0, 0));
        keys.add(new Key(1, 1));
        keys.add(new Key(2, 2));

        assertDoesNotThrow(() -> hud.render(gc, config, keys));
    }

    @Test
    void testRenderWithAllKeysCollected() {
        HUD hud = new HUD();
        Canvas canvas = new Canvas(500, 510);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GridConfig config = new GridConfig(10, 10, 50);

        List<Key> keys = new ArrayList<>();
        Key key1 = new Key(0, 0);
        Key key2 = new Key(1, 1);
        Key key3 = new Key(2, 2);
        key1.collect();
        key2.collect();
        key3.collect();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);

        assertDoesNotThrow(() -> hud.render(gc, config, keys));
    }

    @Test
    void testRenderWithPartialKeysCollected() {
        HUD hud = new HUD();
        Canvas canvas = new Canvas(500, 510);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GridConfig config = new GridConfig(10, 10, 50);

        List<Key> keys = new ArrayList<>();
        Key key1 = new Key(0, 0);
        Key key2 = new Key(1, 1);
        Key key3 = new Key(2, 2);
        key1.collect();
        key2.collect();
        keys.add(key1);
        keys.add(key2);
        keys.add(key3);

        assertDoesNotThrow(() -> hud.render(gc, config, keys));
    }
}
