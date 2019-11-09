package com.dakare.rubik.model;

import com.dakare.rubik.rotate.RotateDirection;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public class CubeItem {

  private static final int TOP_INDEX = 0;
  private static final int BOTTOM_INDEX = 1;
  private static final int RIGHT_INDEX = 2;
  private static final int LEFT_INDEX = 3;
  private static final int FRONT_INDEX = 4;
  private static final int BACK_INDEX = 5;

  @Getter
  @Setter
  private int index;
  private final ColorWrapper[] colors = new ColorWrapper[6];

  public CubeItem(int index) {
    this.index = index;
    Arrays.fill(colors, ColorWrapper.BLACK);
  }

  public CubeItem(CubeItem from) {
    index = from.index;
    System.arraycopy(from.colors, 0, colors, 0, from.colors.length);
  }

  public ColorWrapper getTop() {
    return colors[TOP_INDEX];
  }

  public CubeItem setTop(ColorWrapper top) {
    colors[TOP_INDEX] = top;
    return this;
  }

  public ColorWrapper getBottom() {
    return colors[BOTTOM_INDEX];
  }

  public CubeItem setBottom(ColorWrapper bottom) {
    colors[BOTTOM_INDEX] = bottom;
    return this;
  }

  public ColorWrapper getLeft() {
    return colors[LEFT_INDEX];
  }

  public CubeItem setLeft(ColorWrapper left) {
    colors[LEFT_INDEX] = left;
    return this;
  }

  public ColorWrapper getRight() {
    return colors[RIGHT_INDEX];
  }

  public CubeItem setRight(ColorWrapper right) {
    colors[RIGHT_INDEX] = right;
    return this;
  }

  public ColorWrapper getFront() {
    return colors[FRONT_INDEX];
  }

  public CubeItem setFront(ColorWrapper front) {
    colors[FRONT_INDEX] = front;
    return this;
  }

  public ColorWrapper getBack() {
    return colors[BACK_INDEX];
  }

  public CubeItem setBack(ColorWrapper back) {
    colors[BACK_INDEX] = back;
    return this;
  }

  public int getX() {
    return index % RubikCube.SIZE;
  }

  public int getY() {
    return index / RubikCube.SIZE % RubikCube.SIZE;
  }

  public int getZ() {
    return index / RubikCube.SIZE / RubikCube.SIZE;
  }

  public void rotate(RotateDirection rotateDirection) {
    switch (rotateDirection) {
      case FRONT:
        rotate(TOP_INDEX, RIGHT_INDEX, BOTTOM_INDEX, LEFT_INDEX);
        return;
      case COUNTER_CLOCKWISE_FRONT:
        rotate(TOP_INDEX, LEFT_INDEX, BOTTOM_INDEX, RIGHT_INDEX);
        return;
      default:
        throw new UnsupportedOperationException("Rotation direction is not supported: " + rotateDirection);
    }
  }

  private void rotate(int first, int second, int third, int fourth) {
    ColorWrapper firstValue = colors[first];
    colors[first] = colors[fourth];
    colors[fourth] = colors[third];
    colors[third] = colors[second];
    colors[second] = firstValue;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "#" + index
        + "{top:" + colors[TOP_INDEX]
        + ",right:" + colors[RIGHT_INDEX]
        + ",bottom:" + colors[BOTTOM_INDEX]
        + ",left:" + colors[LEFT_INDEX]
        + ",front:" + colors[FRONT_INDEX]
        + ",back:" + colors[BACK_INDEX] + "}";
  }
}
