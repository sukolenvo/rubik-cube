package com.dakare.rubik.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class CubeView extends Group {

    public static final int SIZE = 10;

    public CubeView(Color color) {
        this(color, color, color, color, color, color);
    }

    public CubeView(Color top, Color bottom, Color left, Color right, Color front, Color back) {
        createTopEdge(top);
        createBottomEdge(bottom);
        createLeftEdge(left);
        createRightEdge(right);
        createFrontEdge(front);
        createBackEdge(back);
    }

    private void createBackEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(SIZE - 2, SIZE - 2, 1);
        box.setTranslateZ(SIZE / 2 - 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }

    private void createLeftEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(1, SIZE - 2, SIZE - 2);
        box.setTranslateX(-SIZE / 2 + 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }

    private void createBottomEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(SIZE - 2, 1, SIZE - 2);
        box.setTranslateY(SIZE / 2 - 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }

    private void createRightEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(1, SIZE - 2, SIZE - 2);
        box.setTranslateX(SIZE / 2 - 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }

    private void createFrontEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(SIZE - 2, SIZE - 2, 1);
        box.setTranslateZ(-SIZE / 2 + 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }

    private void createTopEdge(Color color) {
        PhongMaterial colorMaterial = new PhongMaterial();
        colorMaterial.setDiffuseColor(color);
        colorMaterial.setSpecularColor(color);
        Box box = new Box(SIZE - 2, 1, SIZE - 2);
        box.setTranslateY(-SIZE / 2 + 1);
        box.setMaterial(colorMaterial);
        getChildren().add(box);
    }
}
