package com.dakare.rubik.model;

import javafx.scene.paint.Color;

import java.util.Random;

public class ColorsHelper {
    private static Color[] colors = {Color.GREEN, Color.YELLOW, Color.RED, Color.WHITE, Color.ORANGE, Color.BLUE};

    public static Color random() {
        return colors[new Random().nextInt(6)];
    }
}
