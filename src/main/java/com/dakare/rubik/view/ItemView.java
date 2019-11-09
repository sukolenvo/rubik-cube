package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class ItemView extends Group {

  public static final int SIZE = 10;

  public ItemView(CubeItem cubeItem) {
    createTopEdge(cubeItem.getTop().getColor());
    createBottomEdge(cubeItem.getBottom().getColor());
    createLeftEdge(cubeItem.getLeft().getColor());
    createRightEdge(cubeItem.getRight().getColor());
    createFrontEdge(cubeItem.getFront().getColor());
    createBackEdge(cubeItem.getBack().getColor());
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
