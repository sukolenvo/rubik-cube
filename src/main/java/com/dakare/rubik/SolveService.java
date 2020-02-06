package com.dakare.rubik;

import com.dakare.rubik.model.ColorWrapper;
import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.view.AnimationPlayService;
import com.google.common.base.Preconditions;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

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
  }

  private void createCross() {
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
    if (!cubeItem.isInPlaceIgnoreRotation()) {
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
  }

  void rotateCrossCubeItemIfNeeded(CubeItem item) {
    if (!item.isInPlace()) {
      int originalX = item.getX();
      int originalY = item.getY();
      while(item.getX() != 0 || item.getY() != 1) { // move to left
        animationPlayService.executeAction(RotateDirection.FRONT);
      }
      // rotate
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_LEFT);
      animationPlayService.executeAction(RotateDirection.TOP);
      animationPlayService.executeAction(RotateDirection.BACK);
      animationPlayService.executeAction(RotateDirection.COUNTER_CLOCKWISE_TOP);
      animationPlayService.executeAction(RotateDirection.LEFT);
      animationPlayService.executeAction(RotateDirection.LEFT);
      for(int i = 0; item.getX() != originalX || item.getY() != originalY; i++) { // return item back it the right place
        if (i >= 5) {
          throw new IllegalStateException("Endless loop detected while processing " + item);
        }
        animationPlayService.executeAction(RotateDirection.FRONT);
      }
      Preconditions.checkArgument(item.isInPlace(),
          "expecting cube item % to be in place, but it was not", item);
    }
  }

  private void completeFirstSide() {
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.BLUE, ColorWrapper.RED), 0, 0);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.GREEN, ColorWrapper.RED), 2, 0);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.BLUE, ColorWrapper.ORANGE), 0, 2);
    completeFirstSideCorner(rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.GREEN, ColorWrapper.ORANGE), 2, 2);
  }

  void completeFirstSideCorner(CubeItem cubeItem, int wantedX, int wantedY) {
    if (!cubeItem.isInPlace()) {
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
  }
}
