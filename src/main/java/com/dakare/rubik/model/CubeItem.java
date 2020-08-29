package com.dakare.rubik.model;

import com.dakare.rubik.rotate.CubeItemMove;
import com.dakare.rubik.rotate.RotateDirection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

  public CubeItemMove moveTo(int index) {
    int from = this.index;
    this.index = index;
    return new CubeItemMove(from, index);
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

  public ColorWrapper getSideColor(RotateDirection side) {
    switch (side) {
      case FRONT:
      case COUNTER_CLOCKWISE_FRONT:
        return getFront();
      case TOP:
      case COUNTER_CLOCKWISE_TOP:
        return getTop();
      case RIGHT:
      case COUNTER_CLOCKWISE_RIGHT:
        return getRight();
      case BACK:
      case COUNTER_CLOCKWISE_BACK:
        return getBack();
      case BOTTOM:
      case COUNTER_CLOCKWISE_BOTTOM:
        return getBottom();
      case LEFT:
      case COUNTER_CLOCKWISE_LEFT:
        return getLeft();
      default:
        throw new IllegalStateException("Unexpected side " + side);
    }
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
      case COUNTER_CLOCKWISE_BACK:
        rotate(TOP_INDEX, RIGHT_INDEX, BOTTOM_INDEX, LEFT_INDEX);
        return;
      case BACK:
      case COUNTER_CLOCKWISE_FRONT:
        rotate(TOP_INDEX, LEFT_INDEX, BOTTOM_INDEX, RIGHT_INDEX);
        return;
      case RIGHT:
      case COUNTER_CLOCKWISE_LEFT:
        rotate(TOP_INDEX, BACK_INDEX, BOTTOM_INDEX, FRONT_INDEX);
        return;
      case LEFT:
      case COUNTER_CLOCKWISE_RIGHT:
        rotate(TOP_INDEX, FRONT_INDEX, BOTTOM_INDEX, BACK_INDEX);
        return;
      case TOP:
      case BOTTOM:
        rotate(FRONT_INDEX, LEFT_INDEX, BACK_INDEX, RIGHT_INDEX);
        return;
      case COUNTER_CLOCKWISE_BOTTOM:
      case COUNTER_CLOCKWISE_TOP:
        rotate(FRONT_INDEX, RIGHT_INDEX, BACK_INDEX, LEFT_INDEX);
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

  public boolean isOfColors(ColorWrapper... colors) {
    return isOfColors(Arrays.asList(colors));
  }

  public boolean isOfColors(List<ColorWrapper> expectedColors) {
    List<ColorWrapper> effectiveColors = Arrays.stream(this.colors)
        .filter(colorWrapper -> colorWrapper != ColorWrapper.BLACK)
        .collect(Collectors.toList());
    return effectiveColors.size() == expectedColors.size()
        && effectiveColors.containsAll(expectedColors);
  }

  public boolean isInPlace() {
    if (colors[TOP_INDEX] != ColorWrapper.BLACK && colors[TOP_INDEX] != RubikCube.INITIAL_COLOR_TOP) {
      return false;
    }
    if (colors[FRONT_INDEX] != ColorWrapper.BLACK && colors[FRONT_INDEX] != RubikCube.INITIAL_COLOR_FRONT) {
      return false;
    }
    if (colors[BOTTOM_INDEX] != ColorWrapper.BLACK && colors[BOTTOM_INDEX] != RubikCube.INITIAL_COLOR_BOTTOM) {
      return false;
    }
    if (colors[BACK_INDEX] != ColorWrapper.BLACK && colors[BACK_INDEX] != RubikCube.INITIAL_COLOR_BACK) {
      return false;
    }
    if (colors[LEFT_INDEX] != ColorWrapper.BLACK && colors[LEFT_INDEX] != RubikCube.INITIAL_COLOR_LEFT) {
      return false;
    }
    if (colors[RIGHT_INDEX] != ColorWrapper.BLACK && colors[RIGHT_INDEX] != RubikCube.INITIAL_COLOR_RIGHT) {
      return false;
    }
    return true;
  }

  public int getExpectedPosition() {
    int x = 1;
    int y = 1;
    int z = 1;
    for (ColorWrapper color : colors) {
      switch (color) {
        case WHITE -> z = 0;
        case GREEN -> x = 2;
        case RED -> y = 0;
        case ORANGE -> y = 2;
        case YELLOW -> z = 2;
        case BLUE -> x = 0;
      }
    }
    return z * RubikCube.SIZE * RubikCube.SIZE + y * RubikCube.SIZE + x;
  }

  public boolean isInPlaceIgnoreRotation() {
    List<ColorWrapper> expectedColors = new ArrayList<>();
    if (getX() == 0) {
      expectedColors.add(RubikCube.INITIAL_COLOR_LEFT);
    } else if (getX() == 2) {
      expectedColors.add(RubikCube.INITIAL_COLOR_RIGHT);
    }
    if (getY() == 0) {
      expectedColors.add(RubikCube.INITIAL_COLOR_TOP);
    } else if (getY() == 2) {
      expectedColors.add(RubikCube.INITIAL_COLOR_BOTTOM);
    }
    if (getZ() == 0) {
      expectedColors.add(RubikCube.INITIAL_COLOR_FRONT);
    } else if (getZ() == 2) {
      expectedColors.add(RubikCube.INITIAL_COLOR_BACK);
    }
    return isOfColors(expectedColors);
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
