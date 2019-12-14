package com.dakare.rubik.model;

import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.rotate.CubeItemMove;
import com.google.common.base.Preconditions;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class RubikCube {

  public static final int SIZE = 3;

  static final ColorWrapper INITIAL_COLOR_FRONT = ColorWrapper.WHITE;
  static final ColorWrapper INITIAL_COLOR_RIGHT = ColorWrapper.GREEN;
  static final ColorWrapper INITIAL_COLOR_TOP = ColorWrapper.RED;
  static final ColorWrapper INITIAL_COLOR_BOTTOM = ColorWrapper.ORANGE;
  static final ColorWrapper INITIAL_COLOR_BACK = ColorWrapper.YELLOW;
  static final ColorWrapper INITIAL_COLOR_LEFT = ColorWrapper.BLUE;

  @Getter
  private final CubeItem[] items;

  public static RubikCube createDefault() {
    return new RubikCube();
  }

  public static RubikCube createMixed() {
    RubikCube rubikCube = new RubikCube();
    EnhancedRandomBuilder.aNewEnhancedRandom()
        .objects(RotateDirection.class, 20)
        .forEach(rubikCube::rotate);
    return rubikCube;
  }

  private RubikCube() {
    items = new CubeItem[SIZE * SIZE * SIZE];
    for (int i = 0; i < items.length; i++) {
      CubeItem cubeItem = new CubeItem(i);
      if (cubeItem.getX() == 0) {
        cubeItem.setLeft(INITIAL_COLOR_LEFT);
      } else if (cubeItem.getX() == SIZE - 1) {
        cubeItem.setRight(INITIAL_COLOR_RIGHT);
      }
      if (cubeItem.getY() == 0) {
        cubeItem.setTop(INITIAL_COLOR_TOP);
      } else if (cubeItem.getY() == SIZE - 1) {
        cubeItem.setBottom(INITIAL_COLOR_BOTTOM);
      }
      if (cubeItem.getZ() == 0) {
        cubeItem.setFront(INITIAL_COLOR_FRONT);
      } else if (cubeItem.getZ() == SIZE - 1) {
        cubeItem.setBack(INITIAL_COLOR_BACK);
      }
      items[i] = cubeItem;
    }
  }

  List<CubeItem> getFrontItems() {
    return Arrays.stream(items)
        .filter(item -> item.getIndex() < SIZE * SIZE)
        .sorted(Comparator.comparingInt(CubeItem::getIndex))
        .collect(Collectors.toList());
  }

  List<CubeItem> getBackItems() {
    return Arrays.stream(items)
        .filter(item -> item.getZ() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparing(Comparator.comparingInt(CubeItem::getX).reversed()))
        .collect(Collectors.toList());
  }

  List<CubeItem> getTopItems() {
    return Arrays.stream(items)
        .filter(item -> item.getY() == 0)
        .sorted(Comparator.comparingInt(CubeItem::getZ).reversed()
            .thenComparingInt(CubeItem::getX))
        .collect(Collectors.toList());
  }

  List<CubeItem> getBottomItems() {
    return Arrays.stream(items)
        .filter(item -> item.getY() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getZ)
            .thenComparing(Comparator.comparingInt(CubeItem::getX).reversed()))
        .collect(Collectors.toList());
  }

  List<CubeItem> getLeftItems() {
    return Arrays.stream(items)
        .filter(item -> item.getX() == 0)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparing(Comparator.comparingInt(CubeItem::getZ).reversed()))
        .collect(Collectors.toList());
  }

  List<CubeItem> getRightItems() {
    return Arrays.stream(items)
        .filter(item -> item.getX() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparingInt(CubeItem::getZ))
        .collect(Collectors.toList());
  }

  public List<CubeItemMove> rotate(RotateDirection rotateDirection) {
    switch (rotateDirection) {
      case FRONT:
      case COUNTER_CLOCKWISE_FRONT:
        return rotate(getFrontItems(), rotateDirection);
      case RIGHT:
      case COUNTER_CLOCKWISE_RIGHT:
        return rotate(getRightItems(), rotateDirection);
      case TOP:
      case COUNTER_CLOCKWISE_TOP:
        return rotate(getTopItems(), rotateDirection);
      case BACK:
      case COUNTER_CLOCKWISE_BACK:
        return rotate(getBackItems(), rotateDirection);
      case LEFT:
      case COUNTER_CLOCKWISE_LEFT:
        return rotate(getLeftItems(), rotateDirection);
      case BOTTOM:
      case COUNTER_CLOCKWISE_BOTTOM:
        return rotate(getBottomItems(), rotateDirection);
      default:
        throw new UnsupportedOperationException(rotateDirection + " is not supported");
    }
  }

  private List<CubeItemMove> rotate(List<CubeItem> items, RotateDirection rotateDirection) {
    Preconditions.checkArgument(RubikCube.SIZE <= 3, "This method is simplified to support Rubik Cube 3x3"
        + " or less. To work with higher dimension cube please update this method to a generic version");
    List<CubeItem> ringFromSide = getRingFromSide(items);
    if (!rotateDirection.isClockwise()) {
      Collections.reverse(ringFromSide);
    }
    List<CubeItemMove> result = new ArrayList<>();
    for (int i = 0; i < RubikCube.SIZE - 1; i++) {
      int firstIndex = ringFromSide.get(i).getIndex();
      result.add(ringFromSide.get(i)
          .moveTo(ringFromSide.get(i + RubikCube.SIZE - 1).getIndex()));
      result.add(ringFromSide.get(i + RubikCube.SIZE - 1)
          .moveTo(ringFromSide.get(i + (RubikCube.SIZE - 1) * 2).getIndex()));
      result.add(ringFromSide.get(i + (RubikCube.SIZE - 1) * 2)
          .moveTo(ringFromSide.get(i + (RubikCube.SIZE - 1) * 3).getIndex()));
      result.add(ringFromSide.get(i + (RubikCube.SIZE - 1) * 3)
          .moveTo(firstIndex));
    }
    ringFromSide.forEach(cubeItem -> cubeItem.rotate(rotateDirection));
    return result;
  }

  List<CubeItem> getRingFromSide(List<CubeItem> items) {
    List<CubeItem> result = new ArrayList<>();
    for (int i = 0; i < RubikCube.SIZE; i++) { //top line
      result.add(items.get(i));
    }
    for (int j = RubikCube.SIZE * 2 - 1; j < RubikCube.SIZE * RubikCube.SIZE; j += RubikCube.SIZE) { //right column
      result.add(items.get(j));
    }

    for (int i = RubikCube.SIZE * RubikCube.SIZE - 2; i >= RubikCube.SIZE * (RubikCube.SIZE - 1); i--) { //bottom line
      result.add(items.get(i));
    }
    for (int j = RubikCube.SIZE * (RubikCube.SIZE - 2); j > 0; j -= RubikCube.SIZE) { // left line
      result.add(items.get(j));
    }
    return result;
  }
}
