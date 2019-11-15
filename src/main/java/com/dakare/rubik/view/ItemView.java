package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class ItemView extends Group {

  public static final int SIZE = 10;

  private final CubeItem cubeItem;
  private final Rotate rotateX;
  private final Rotate rotateY;
  private final Rotate rotateZ;

  public ItemView(CubeItem cubeItem) {
    this.cubeItem = cubeItem;
    createTopEdge(cubeItem.getTop().getColor());
    createBottomEdge(cubeItem.getBottom().getColor());
    createLeftEdge(cubeItem.getLeft().getColor());
    createRightEdge(cubeItem.getRight().getColor());
    createFrontEdge(cubeItem.getFront().getColor());
    createBackEdge(cubeItem.getBack().getColor());
    setTranslateX(getExpectedX());
    setTranslateY(getExpectedY());
    setTranslateZ(getExpectedZ());
    rotateX = new Rotate(0, Rotate.X_AXIS);
    getTransforms().add(rotateX);
    rotateY = new Rotate(0, Rotate.Y_AXIS);
    getTransforms().add(rotateY);
    rotateZ = new Rotate(0, Rotate.Z_AXIS);
    getTransforms().add(rotateZ);
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

  public Rotate getRotateAroundAxis(Point3D axis) {
    if (axis == Rotate.X_AXIS) {
      if ((int) rotateY.getAngle() / 90 % 2 == 1) {
        return rotateZ;
      }
      if ((int) rotateZ.getAngle() / 90 % 2 == 1) {
        return rotateY;
      }
      return rotateX;
    }
    if (axis == Rotate.Y_AXIS) {
      if ((int) rotateZ.getAngle() / 90 % 2 == 1) {
        return rotateX;
      }
      if ((int) rotateX.getAngle() / 90 % 2 == 1) {
        return rotateZ;
      }
      return rotateY;
    }
    if (axis == Rotate.Z_AXIS) {
      boolean yMoved = (int) rotateY.getAngle() / 90 % 2 == 1;
      boolean xMoved = (int) rotateX.getAngle() / 90 % 2 == 1;
      if (yMoved && !xMoved) {
        return rotateX;
      }
      if (xMoved && !yMoved) {
        return rotateY;
      }
      return rotateZ;
    }
    throw new UnsupportedOperationException("Can't get axis from " + axis);
  }
}
