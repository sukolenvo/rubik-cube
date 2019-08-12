package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class CubeView extends Group {

    public static final int SIZE = 10;

    public CubeView(CubeItem cubeItem) {
        createTopEdge(cubeItem.getTop());
        createBottomEdge(cubeItem.getBottom());
        createLeftEdge(cubeItem.getLeft());
        createRightEdge(cubeItem.getRight());
        createFrontEdge(cubeItem.getFront());
        createBackEdge(cubeItem.getBack());
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
