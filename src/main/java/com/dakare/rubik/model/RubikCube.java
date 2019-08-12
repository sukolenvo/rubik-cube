package com.dakare.rubik.model;

import com.google.common.base.Preconditions;
import javafx.scene.paint.Color;

public class RubikCube {
    private Side front = new Side(Color.WHITE, Color.WHITE, Color.WHITE,
            Color.WHITE, Color.WHITE, Color.WHITE,
            Color.WHITE, Color.WHITE, Color.WHITE);
    private Side right = new Side(Color.GREEN, Color.GREEN, Color.GREEN,
            Color.GREEN, Color.GREEN, Color.GREEN,
            Color.GREEN, Color.GREEN, Color.GREEN);
    private Side top = new Side(Color.RED, Color.RED, Color.RED,
            Color.RED, Color.RED, Color.RED,
            Color.RED, Color.RED, Color.RED);
    private Side bottom = new Side(Color.ORANGE, Color.ORANGE, Color.ORANGE,
            Color.ORANGE, Color.ORANGE, Color.ORANGE,
            Color.ORANGE, Color.ORANGE, Color.ORANGE);
    private Side back = new Side(Color.YELLOW, Color.YELLOW, Color.YELLOW,
            Color.YELLOW, Color.YELLOW, Color.YELLOW,
            Color.YELLOW, Color.YELLOW, Color.YELLOW);
    private Side left = new Side(Color.BLUE, Color.BLUE, Color.BLUE,
            Color.BLUE, Color.BLUE, Color.BLUE,
            Color.BLUE, Color.BLUE, Color.BLUE);
    private CubeItem[] items = new CubeItem[27];

    {
        for (int i = 0; i < 27; i++) {
            items[i] = createCubeItem(getX(i), getY(i), getZ(i))
                    .index(i)
                    .build();
        }
    }

    public static int getX(int index) {
        return index % 3;
    }

    public static int getY(int index) {
        return index / 3 % 3;
    }

    public static int getZ(int index) {
        return index / 9;
    }

    /**
     * From top left front corner.
     */
    private CubeItem.CubeItemBuilder createCubeItem(int x, int y, int z) {
        Preconditions.checkArgument(x >= 0 && x < 3, "x is invalid %d", x);
        Preconditions.checkArgument(y >= 0 && y < 3, "y is invalid %d", y);
        Preconditions.checkArgument(z >= 0 && z < 3, "z is invalid %d", z);
        CubeItem.CubeItemBuilder builder = CubeItem.builder();
        if (x == 0) {
            builder.left(left.getColor(z, y));
        } else if (x == 2) {
            builder.right(right.getColor(z, y));
        }
        if (y == 0) {
            builder.top(top.getColor(x, 2 - z));
        } else if (y == 2) {
            builder.bottom(bottom.getColor(x, 2 - z));
        }
        if (z == 0) {
            builder.front(front.getColor(x, y));
        } else if (z == 2) {
            builder.back(back.getColor(2 - x, y));
        }
        return builder;
    }

    public CubeItem getCubeItem(int index) {
        return items[index];
    }

    public void swap(int from, int to) {
        CubeItem item = items[from];
        items[from] = items[to];
        items[to] = item;
    }
}
