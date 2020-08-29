package com.dakare.rubik.model;

import com.dakare.rubik.rotate.CubeItemMove;
import com.dakare.rubik.rotate.RotateDirection;
import com.google.common.base.Preconditions;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
public class RubikCube {

  public static final int SIZE = 3;

  public static final ColorWrapper INITIAL_COLOR_FRONT = ColorWrapper.WHITE;
  public static final ColorWrapper INITIAL_COLOR_RIGHT = ColorWrapper.GREEN;
  public static final ColorWrapper INITIAL_COLOR_TOP = ColorWrapper.RED;
  public static final ColorWrapper INITIAL_COLOR_BOTTOM = ColorWrapper.ORANGE;
  public static final ColorWrapper INITIAL_COLOR_BACK = ColorWrapper.YELLOW;
  public static final ColorWrapper INITIAL_COLOR_LEFT = ColorWrapper.BLUE;

  @Getter
  private final CubeItem[] items;

  /**
   * Cube is created in solved state.
   */
  public RubikCube() {
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

  public void mix() {
    EnhancedRandomBuilder.aNewEnhancedRandom()
        .objects(RotateDirection.class, 20)
        .forEach(this::rotate);
    log.info("Mixed cube state is {}", getCurrentState());
  }

  public List<CubeItem> getFrontItems() {
    return Arrays.stream(items)
        .filter(item -> item.getIndex() < SIZE * SIZE)
        .sorted(Comparator.comparingInt(CubeItem::getIndex))
        .collect(Collectors.toList());
  }

  public List<CubeItem> getBackItems() {
    return Arrays.stream(items)
        .filter(item -> item.getZ() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparing(Comparator.comparingInt(CubeItem::getX).reversed()))
        .collect(Collectors.toList());
  }

  public List<CubeItem> getTopItems() {
    return Arrays.stream(items)
        .filter(item -> item.getY() == 0)
        .sorted(Comparator.comparingInt(CubeItem::getZ).reversed()
            .thenComparingInt(CubeItem::getX))
        .collect(Collectors.toList());
  }

  public List<CubeItem> getBottomItems() {
    return Arrays.stream(items)
        .filter(item -> item.getY() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getZ)
            .thenComparing(Comparator.comparingInt(CubeItem::getX).reversed()))
        .collect(Collectors.toList());
  }

  public List<CubeItem> getLeftItems() {
    return Arrays.stream(items)
        .filter(item -> item.getX() == 0)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparing(Comparator.comparingInt(CubeItem::getZ).reversed()))
        .collect(Collectors.toList());
  }

  public List<CubeItem> getRightItems() {
    return Arrays.stream(items)
        .filter(item -> item.getX() == SIZE - 1)
        .sorted(Comparator.comparingInt(CubeItem::getY)
            .thenComparingInt(CubeItem::getZ))
        .collect(Collectors.toList());
  }

  public CubeItem findByColors(ColorWrapper... colors) {
    return Arrays.stream(items)
        .filter(cubeItem -> cubeItem.isOfColors(colors))
        .findAny()
        .orElseThrow(() -> new IllegalStateException("Requested invalid combination of colors: " + Arrays.asList(colors)));
  }

  public CubeItem findByPosition(int positionIndex) {
    return Arrays.stream(items)
        .filter(cubeItem -> cubeItem.getIndex() == positionIndex)
        .findAny()
        .orElseThrow(() -> new IllegalStateException("Invalid position: " + positionIndex));
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

  public String getCurrentState() {
    return getFrontItems().stream()
        .map(CubeItem::getFront)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining())
        + getTopItems().stream()
        .map(CubeItem::getTop)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining())
        + getRightItems().stream()
        .map(CubeItem::getRight)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining())
        + getBottomItems().stream()
        .map(CubeItem::getBottom)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining())
        + getLeftItems().stream()
        .map(CubeItem::getLeft)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining())
        + getBackItems().stream()
        .map(CubeItem::getBack)
        .map(ColorWrapper::getColorLetter)
        .collect(Collectors.joining());
  }

  public void restoreState(String state) {
    Iterator<String> colorLetterIterator = state.chars().mapToObj(letter -> (char) letter).map(Object::toString).iterator();
    getFrontItems().forEach(item -> item.setFront(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    getTopItems().forEach(item -> item.setTop(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    getRightItems().forEach(item -> item.setRight(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    getBottomItems().forEach(item -> item.setBottom(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    getLeftItems().forEach(item -> item.setLeft(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    getBackItems().forEach(item -> item.setBack(ColorWrapper.fromColorLetter(colorLetterIterator.next())));
    Preconditions.checkArgument(!colorLetterIterator.hasNext(), "state contains too many entries %s", state);
    Arrays.sort(this.items, Comparator.comparingInt(CubeItem::getExpectedPosition));
  }

  @Override
  public String toString() {
    return "Rubik cube (" + getCurrentState() + ")";
  }
}
