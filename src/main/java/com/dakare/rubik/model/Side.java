package com.dakare.rubik.model;

import javafx.scene.paint.Color;

public class Side {
    private Color[] colors = new Color[9];

    public Side(Color leftTop, Color top, Color rightTop,
                Color left, Color middle, Color right,
                Color leftBottom, Color bottom, Color rightBottom) {
        colors[0] = leftTop;
        colors[1] = top;
        colors[2] = rightTop;
        colors[3] = left;
        colors[4] = middle;
        colors[5] = right;
        colors[6] = leftBottom;
        colors[7] = bottom;
        colors[8] = rightBottom;
    }

    public Color getColor(int x, int y) {
        return colors[x + y * 3];
    }
}
