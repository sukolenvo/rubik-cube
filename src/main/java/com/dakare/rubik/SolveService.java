package com.dakare.rubik;

import com.dakare.rubik.model.ColorWrapper;
import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.view.AnimationPlayService;
import com.google.common.base.Preconditions;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SolveService {

  @Autowired
  private AnimationPlayService animationPlayService;

  @Autowired
  private RubikCube rubikCube;

  @EventListener
  public void onKeyPressed(KeyCode keyCode) {
    if (keyCode == KeyCode.P) {
      new Thread(this::solve).start();
    }
  }

  void solve() {
    Preconditions.checkArgument(RubikCube.SIZE == 3, "This method supports only 3x3 Rubik cube");
    createCross();
    completeFirstSide();
    completeLevelTwo();
    completeBack();
  }

  private void createCross() {
    log.debug("Create cross started");
    putCrossCubeItemInPlace(RotateDirection.LEFT, ColorWrapper.BLUE, 0, 1);
    putCrossCubeItemInPlace(RotateDirection.TOP, ColorWrapper.RED, 1, 0);
    putCrossCubeItemInPlace(RotateDirection.RIGHT, ColorWrapper.GREEN, 2, 1);
    putCrossCubeItemInPlace(RotateDirection.BOTTOM, ColorWrapper.ORANGE, 1, 2);
    rotateCrossCubeItemIfNeeded(rubikCube.getItems()[1]);
    rotateCrossCubeItemIfNeeded(rubikCube.getItems()[3]);
    rotateCrossCubeItemIfNeeded(rubikCube.getItems()[5]);
    rotateCrossCubeItemIfNeeded(rubikCube.getItems()[7]);
  }

  void putCrossCubeItemInPlace(RotateDirection side, ColorWrapper sideColor, int wantedX, int wantedY) {
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.WHITE, sideColor);// green red blue orange
    log.debug("Put cross item to correct position {}", cubeItem);
    if (cubeItem.isInPlaceIgnoreRotation()) {
      log.info("Cube item {} already on correct position", cubeItem);
      return;
    }
    if (cubeItem.getZ() == 0) {
      if (cubeItem.getX() == 1) {
        if (cubeItem.getY() == 0) {
          animationPlayService.executeAction(RotateDirection.TOP);
          animationPlayService.executeAction(RotateDirection.TOP);
        } else {
          animationPlayService.executeAction(RotateDirection.BOTTOM);
          animationPlayService.executeAction(RotateDirection.BOTTOM);
        }
      } else {
        if (cubeItem.getX() == 0) {
          animationPlayService.executeAction(RotateDirection.LEFT);
          animationPlayService.executeAction(RotateDirection.LEFT);
        } else {
          animationPlayService.executeAction(RotateDirection.RIGHT);
          animationPlayService.executeAction(RotateDirection.RIGHT);
        }
      }
    } else if (cubeItem.getZ() == 1) {
      if (cubeItem.getIndex() == 9) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.LEFT);
      } else if (cubeItem.getIndex() == 11) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
      } else if (cubeItem.getIndex() == 15) {
        animationPlayService.executeAction(RotateDirection.LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
      } else if (cubeItem.getIndex() == 17) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BOTTOM);
      } else {
        throw new IllegalStateException("Unexpected cube index (should be corner): " + cubeItem);
      }
    }
    for (int i = 0; cubeItem.getX() != wantedX || cubeItem.getY() != wantedY; i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    animationPlayService.executeAction(side);
    animationPlayService.executeAction(side);
    Preconditions.checkArgument(cubeItem.isInPlaceIgnoreRotation(),
        "expecting %s to be it place ignoring rotation, but it is not", cubeItem);
  }

  void rotateCrossCubeItemIfNeeded(CubeItem item) {
    log.debug("Rotating cross item {}", item);
    if (item.isInPlace()) {
      log.info("Item {} already in place for cross", item);
      return;
    }
    int originalX = item.getX();
    int originalY = item.getY();
    while (item.getX() != 0 || item.getY() != 1) { // move to left
      animationPlayService.executeAction(RotateDirection.FRONT);
    }
    // rotate
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    animationPlayService.executeAction(RotateDirection.TOP);
    animationPlayService.executeAction(RotateDirection.BACK);
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
    animationPlayService.executeAction(RotateDirection.LEFT);
    animationPlayService.executeAction(RotateDirection.LEFT);
    for (int i = 0; item.getX() != originalX || item.getY() != originalY; i++) { // return item back it the right place
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + item);
      }
      animationPlayService.executeAction(RotateDirection.FRONT);
    }
    Preconditions.checkArgument(item.isInPlace(),
        "expecting cube item %s to be in place, but it was not", item);
  }

  private void completeFirstSide() {
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.BLUE, ColorWrapper.RED), 0, 0);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.GREEN, ColorWrapper.RED), 2, 0);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.BLUE, ColorWrapper.ORANGE), 0, 2);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.GREEN, ColorWrapper.ORANGE), 2, 2);
  }

  void completeFirstSideCorner(CubeItem cubeItem, int wantedX, int wantedY) {
    log.debug("Processing first side corner {}", cubeItem);
    if (cubeItem.isInPlace()) {
      log.info("Cube item {} already in place", cubeItem);
      return;
    }
    if (cubeItem.getZ() == 0) {
      if (cubeItem.getIndex() == 0) {
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
      } else if (cubeItem.getIndex() == 2) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
      } else if (cubeItem.getIndex() == 6) {
        animationPlayService.executeAction(RotateDirection.BOTTOM);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
      } else if (cubeItem.getIndex() == 8) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.BOTTOM);
      } else {
        throw new IllegalStateException("expecting cube itemto be in corner, but it was not: " + cubeItem);
      }
    }
    for (int i = 0; cubeItem.getX() != wantedX || cubeItem.getY() != wantedY; i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    if (cubeItem.getIndex() == 18) {
      if (cubeItem.getTop() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
      } else if (cubeItem.getLeft() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.LEFT);
      } else {
        Preconditions.checkArgument(cubeItem.getBack() == ColorWrapper.WHITE,
            "expecting one of the sides of %s to be white", cubeItem);
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
      }
    } else if (cubeItem.getIndex() == 20) {
      if (cubeItem.getTop() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
      } else if (cubeItem.getRight() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
      } else {
        Preconditions.checkArgument(cubeItem.getBack() == ColorWrapper.WHITE,
            "expecting one of the sides of %s to be white", cubeItem);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
      }
    } else if (cubeItem.getIndex() == 24) {
      if (cubeItem.getLeft() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.LEFT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
      } else if (cubeItem.getBottom() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.BOTTOM);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
      } else {
        Preconditions.checkArgument(cubeItem.getBack() == ColorWrapper.WHITE,
            "expecting one of the sides of %s to be white", cubeItem);
        animationPlayService.executeAction(RotateDirection.LEFT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.LEFT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
      }
    } else if (cubeItem.getIndex() == 26) {
      if (cubeItem.getRight() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
      } else if (cubeItem.getBottom() == ColorWrapper.WHITE) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BOTTOM);
      } else {
        Preconditions.checkArgument(cubeItem.getBack() == ColorWrapper.WHITE,
            "expecting one of the sides of %s to be white", cubeItem);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
      }
    } else {
      throw new IllegalStateException("expecting cube itemto be in corner, but it was not: " + cubeItem);
    }
  }

  void completeLevelTwo() {
    log.debug("Start level two");
    completeLevelTwoItem(rubikCube.findByColors(ColorWrapper.GREEN, ColorWrapper.RED),
        RotateDirection.COUNTER_CLOCKWISE_TOP, RotateDirection.RIGHT);
    Preconditions.checkArgument(rubikCube.getFrontItems().stream().allMatch(CubeItem::isInPlace),
        "expecting front item to be in place, but got %s", rubikCube.getFrontItems());
    completeLevelTwoItem(rubikCube.findByColors(ColorWrapper.BLUE, ColorWrapper.RED),
        RotateDirection.TOP, RotateDirection.COUNTER_CLOCKWISE_LEFT);
    Preconditions.checkArgument(rubikCube.getFrontItems().stream().allMatch(CubeItem::isInPlace),
        "expecting front item to be in place, but got %s", rubikCube.getFrontItems());
    completeLevelTwoItem(rubikCube.findByColors(ColorWrapper.BLUE, ColorWrapper.ORANGE),
        RotateDirection.LEFT, RotateDirection.BOTTOM);
    Preconditions.checkArgument(rubikCube.getFrontItems().stream().allMatch(CubeItem::isInPlace),
        "expecting front item to be in place, but got %s", rubikCube.getFrontItems());
    completeLevelTwoItem(rubikCube.findByColors(ColorWrapper.GREEN, ColorWrapper.ORANGE),
        RotateDirection.COUNTER_CLOCKWISE_RIGHT, RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
    Preconditions.checkArgument(rubikCube.getFrontItems().stream().allMatch(CubeItem::isInPlace),
        "expecting front item to be in place, but got %s", rubikCube.getFrontItems());
  }

  void completeLevelTwoItem(CubeItem cubeItem, RotateDirection directionA, RotateDirection directionB) {
    if (cubeItem.isInPlace()) {
      log.info("level 2 item {} already in place", cubeItem);
      return;
    }
    log.debug("Processing level 2 item {}", cubeItem);
    if (cubeItem.getZ() == 1) {
      if (cubeItem.getIndex() == 9) {
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.LEFT);
      } else if (cubeItem.getIndex() == 11) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
      } else if (cubeItem.getIndex() == 15) {
        animationPlayService.executeAction(RotateDirection.LEFT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.BOTTOM);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
      } else if (cubeItem.getIndex() == 17) {
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.BOTTOM);
      } else {
        throw new IllegalStateException("Unexpected position of the item " + cubeItem);
      }
    }
    Preconditions.checkArgument(cubeItem.getZ() == 2, "expecting Z to be 2 for %s", cubeItem);
    for (int i = 0; isOppositeToColor(cubeItem); i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    RotateDirection firstDirection;
    RotateDirection secondDirection;
    if (rubikCube.findByColors(cubeItem.getBack()).getSideColor(directionA) != ColorWrapper.BLACK) {
      firstDirection = directionA;
      secondDirection = directionB;
    } else {
      Preconditions.checkArgument(rubikCube.findByColors(cubeItem.getBack()).getSideColor(directionB) != ColorWrapper.BLACK,
          "expecting center cube to have non black color in one of directions %s or %s, but got %s,",
          directionA, directionB, rubikCube.findByColors(cubeItem.getBack()));
      firstDirection = directionB;
      secondDirection = directionA;
    }
    animationPlayService.executeAction(firstDirection);
    for (int i = 0; cubeItem.getSideColor(secondDirection) == ColorWrapper.BLACK; i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    animationPlayService.executeAction(firstDirection.getOppositeDirection());
    for (int i = 0; cubeItem.getSideColor(firstDirection) == ColorWrapper.BLACK; i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    animationPlayService.executeAction(secondDirection);
    for (int i = 0; cubeItem.getSideColor(secondDirection) == ColorWrapper.BLACK; i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while processing " + cubeItem);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    animationPlayService.executeAction(secondDirection.getOppositeDirection());
    Preconditions.checkArgument(cubeItem.isInPlace(), "expecting to be in place %s", cubeItem);
  }

  private boolean isOppositeToColor(CubeItem cubeItem) {
    CubeItem centerCubeItem = rubikCube.findByColors(cubeItem.getBack());
    return centerCubeItem.getX() != 1 && Math.abs(cubeItem.getX() - centerCubeItem.getX()) != 2
        || centerCubeItem.getY() != 1 && Math.abs(cubeItem.getY() - centerCubeItem.getY()) != 2
        || centerCubeItem.getZ() != 1 && Math.abs(cubeItem.getZ() - centerCubeItem.getZ()) != 2;
  }

  void completeBack() {
    moveCorners(0);
    rotateCorners();
  }

  void moveCorners(int recursionLevel) {
    if (recursionLevel > 2) {
      throw new IllegalStateException("Failed to move corners");
    }
    CubeItem topLeft = rubikCube.getItems()[18];
    CubeItem topRight = rubikCube.getItems()[20];
    CubeItem bottomLeft = rubikCube.getItems()[24];
    CubeItem bottomRight = rubikCube.getItems()[26];
    log.info("Moving corners: {} {} {} {}", topLeft, topRight, bottomLeft, bottomRight);
    for (int i = 0; !topLeft.isInPlaceIgnoreRotation(); i++) {
      if (i >= 5) {
        throw new IllegalStateException("Endless loop detected while moving top left back corner");
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
    int needToMove = 0;
    if (!topRight.isInPlaceIgnoreRotation()) {
      log.info("Top right is not in place");
      needToMove++;
    }
    if (!bottomLeft.isInPlaceIgnoreRotation()) {
      log.info("Bottom left is not in place");
      needToMove++;
    }
    if (!bottomRight.isInPlaceIgnoreRotation()) {
      log.info("Bottom right is not in place");
      needToMove++;
    }
    if (needToMove == 0) {
      log.info("Back corners are in place");
      return;
    }
    if (needToMove == 3) {
      log.info("Moving 3 corners: {} {} {}", topRight, bottomLeft, bottomRight);
      animationPlayService.executeAction(RotateDirection.RIGHT);
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
      animationPlayService.executeAction(RotateDirection.BACK);
      animationPlayService.executeAction(RotateDirection.TOP);
      animationPlayService.executeAction(RotateDirection.BACK);
      moveCorners(recursionLevel + 1);
    } else if (needToMove == 2) {
      if (bottomRight.isInPlaceIgnoreRotation()) {
        log.info("Swap top right with bottom left");
        animationPlayService.executeAction(RotateDirection.BACK);
        animationPlayService.executeAction(RotateDirection.TOP);
        swapBackRightCorners();
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
      } else if (!topRight.isInPlaceIgnoreRotation()) {
        log.info("Swap right corners");
        swapBackRightCorners();
      } else {
        log.info("Swap bottom corners");
        animationPlayService.executeAction(RotateDirection.BACK);
        swapBackRightCorners();
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
      }
    } else {
      throw new IllegalStateException("Invalid number of corners to swap " + needToMove);
    }
  }

  private void swapBackRightCorners() {
    animationPlayService.executeAction(RotateDirection.RIGHT);
    animationPlayService.executeAction(RotateDirection.BACK);
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
    animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_BACK);
    animationPlayService.executeAction(RotateDirection.TOP);
  }

  private void rotateCorners() {
    for (int i = 0; i < 4; i++) {
      CubeItem topRight = rubikCube.getBackItems().get(0);
      for (int j = 0; topRight.getBack() != ColorWrapper.YELLOW; j++) {
        if (j >= 3) {
          throw new IllegalStateException("Endless loop detected while rotating" + topRight);
        }
        log.info("Rotating top right {}", topRight);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_FRONT);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.FRONT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
        animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_FRONT);
        animationPlayService.executeAction(RotateDirection.RIGHT);
        animationPlayService.executeAction(RotateDirection.FRONT);
      }
      animationPlayService.executeAction(RotateDirection.BACK);
    }
  }
}
