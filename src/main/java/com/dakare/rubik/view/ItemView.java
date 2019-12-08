package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.rotate.RotateDirection;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class ItemView extends Group {

  public static final int SIZE = 10;

  private final CubeItem cubeItem;
  private Box bottom;
  private Box left;
  private Box right;
  private Box front;
  private Box top;

  public ItemView(CubeItem cubeItem) {
    this.cubeItem = cubeItem;
    createTopEdge();
    createBottomEdge();
    createLeftEdge();
    createRightEdge();
    createFrontEdge();
    createBackEdge();
    setTranslateX(getExpectedX());
    setTranslateY(getExpectedY());
    setTranslateZ(getExpectedZ());
    applyColors();
  }

  private void createBackEdge() {
    Box box = new Box(SIZE - 2, SIZE - 2, 1);
    box.setTranslateZ(SIZE / 2 - 1);
    getChildren().add(box);
  }

  private void createLeftEdge() {
    left = new Box(1, SIZE - 2, SIZE - 2);
    left.setTranslateX(-SIZE / 2 + 1);
    getChildren().add(left);
  }

  private void createBottomEdge() {
    bottom = new Box(SIZE - 2, 1, SIZE - 2);
    bottom.setTranslateY(SIZE / 2 - 1);
    getChildren().add(bottom);
  }

  private void createRightEdge() {
    right = new Box(1, SIZE - 2, SIZE - 2);
    right.setTranslateX(SIZE / 2 - 1);
    getChildren().add(right);
  }

  private void createFrontEdge() {
    front = new Box(SIZE - 2, SIZE - 2, 1);
    front.setTranslateZ(-SIZE / 2 + 1);
    getChildren().add(front);
  }

  private void createTopEdge() {
    top = new Box(SIZE - 2, 1, SIZE - 2);
    top.setTranslateY(-SIZE / 2 + 1);
    getChildren().add(top);
  }

  public int getExpectedX() {
    return SIZE * (cubeItem.getX() - 1);
  }

  public int getExpectedY() {
    return SIZE * (cubeItem.getY() - 1);
  }

  public int getExpectedZ() {
    return SIZE * (cubeItem.getZ() - 1);
  }

  public boolean requiresMove() {
    return Math.abs(getTranslateX() - getExpectedX()) > 1
        || Math.abs(getTranslateY() - getExpectedY()) > 1
        || Math.abs(getTranslateZ() - getExpectedZ()) > 1;
  }

  public void applyColors() {
    top.setMaterial(createMaterial(cubeItem.getTop().getColor()));
    bottom.setMaterial(createMaterial(cubeItem.getBottom().getColor()));
    left.setMaterial(createMaterial(cubeItem.getLeft().getColor()));
    right.setMaterial(createMaterial(cubeItem.getRight().getColor()));
    front.setMaterial(createMaterial(cubeItem.getFront().getColor()));
    bottom.setMaterial(createMaterial(cubeItem.getBottom().getColor()));
  }

  private PhongMaterial createMaterial(Color color) {
    PhongMaterial colorMaterial = new PhongMaterial();
    colorMaterial.setDiffuseColor(color);
    colorMaterial.setSpecularColor(color);
    return colorMaterial;
  }
}
