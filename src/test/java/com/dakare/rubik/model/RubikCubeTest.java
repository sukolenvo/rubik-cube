package com.dakare.rubik.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.dakare.rubik.rotate.RotateDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RubikCubeTest {

  private RubikCube rubikCube;

  @BeforeEach
  void before() {
    rubikCube = new RubikCube();
  }

  @Test
  void getFrontItems_initial() {
    assertThat(rubikCube.getFrontItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getFront)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_FRONT);
  }

  @Test
  void getBackItems_initial() {
    assertThat(rubikCube.getBackItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getBack)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_BACK);
  }

  @Test
  void getTopItems_initial() {
    assertThat(rubikCube.getTopItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getTop)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_TOP);
  }

  @Test
  void getBottomItems_initial() {
    assertThat(rubikCube.getBottomItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getBottom)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_BOTTOM);
  }

  @Test
  void getLeftItems_initial() {
    assertThat(rubikCube.getLeftItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getLeft)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_LEFT);
  }

  @Test
  void getRightItems_initial() {
    assertThat(rubikCube.getRightItems())
        .hasSize(RubikCube.SIZE * RubikCube.SIZE)
        .extracting(CubeItem::getRight)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_RIGHT);
  }

  @Test
  void rotate_front_initial() {
    rubikCube.rotate(RotateDirection.FRONT);
    assertThat(rubikCube.getFrontItems())
        .as("check front side after front rotation")
        .extracting(CubeItem::getFront)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_FRONT);
    assertThat(rubikCube.getBackItems())
        .as("check back side after front rotation")
        .extracting(CubeItem::getBack)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_BACK);
    assertThat(rubikCube.getTopItems())
        .as("check top side after front rotation. But Got: %s", rubikCube.getTopItems())
        .extracting(CubeItem::getTop)
        .containsExactly(RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP,
            RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT);
    assertThat(rubikCube.getRightItems())
        .as("check right side after front rotation. But Got: %s", rubikCube.getRightItems())
        .extracting(CubeItem::getRight)
        .containsExactly(RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT);
    assertThat(rubikCube.getBottomItems())
        .as("check bottom side after front rotation. But Got: %s", rubikCube.getBottomItems())
        .extracting(CubeItem::getBottom)
        .containsExactly(RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT,
            RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM);
    assertThat(rubikCube.getLeftItems())
        .as("check left side after front rotation. But Got: %s", rubikCube.getLeftItems())
        .extracting(CubeItem::getLeft)
        .containsExactly(RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_BOTTOM);
  }

  @Test
  void rotate_front_counterClockwise() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.FRONT);
    assertThat(rubikCube)
        .isEqualTo(new RubikCube());
  }

  @Test
  void rotate_right_initial() {
    rubikCube.rotate(RotateDirection.RIGHT);
    assertThat(rubikCube.getRightItems())
        .as("check right side after right rotation")
        .extracting(CubeItem::getRight)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_RIGHT);
    assertThat(rubikCube.getLeftItems())
        .as("check left side after right rotation")
        .extracting(CubeItem::getLeft)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_LEFT);
    assertThat(rubikCube.getFrontItems())
        .as("check front side after right rotation. But Got: %s", rubikCube.getFrontItems())
        .extracting(CubeItem::getFront)
        .containsExactly(RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_BOTTOM);
    assertThat(rubikCube.getTopItems())
        .as("check top side after right rotation. But Got: %s", rubikCube.getTopItems())
        .extracting(CubeItem::getTop)
        .containsExactly(RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_FRONT,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_FRONT,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_FRONT);
    assertThat(rubikCube.getBottomItems())
        .as("check bottom side after right rotation. But Got: %s", rubikCube.getBottomItems())
        .extracting(CubeItem::getBottom)
        .containsExactly(RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM,
            RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BOTTOM, RubikCube.INITIAL_COLOR_BOTTOM);
    assertThat(rubikCube.getBackItems())
        .as("check back side after right rotation. But Got: %s", rubikCube.getBackItems())
        .extracting(CubeItem::getBack)
        .containsExactly(RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK,
            RubikCube.INITIAL_COLOR_TOP, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK);
  }

  @Test
  void rotate_right_counterClockwise() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    rubikCube.rotate(RotateDirection.RIGHT);
    assertThat(rubikCube)
        .isEqualTo(new RubikCube());
  }

  @Test
  void rotate_top_initial() {
    rubikCube.rotate(RotateDirection.TOP);
    assertThat(rubikCube.getTopItems())
        .as("check top side after top rotation")
        .extracting(CubeItem::getTop)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_TOP);
    assertThat(rubikCube.getBottomItems())
        .as("check bottom side after top rotation")
        .extracting(CubeItem::getBottom)
        .allMatch(color -> color == RubikCube.INITIAL_COLOR_BOTTOM);
    assertThat(rubikCube.getFrontItems())
        .as("check front side after top rotation. But Got: %s", rubikCube.getFrontItems())
        .extracting(CubeItem::getFront)
        .containsExactly(RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT,
            RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT,
            RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT);
    assertThat(rubikCube.getRightItems())
        .as("check right side after top rotation. But Got: %s", rubikCube.getRightItems())
        .extracting(CubeItem::getRight)
        .containsExactly(RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK,
            RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT,
            RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT, RubikCube.INITIAL_COLOR_RIGHT);
    assertThat(rubikCube.getBackItems())
        .as("check back side after top rotation. But Got: %s", rubikCube.getBackItems())
        .extracting(CubeItem::getBack)
        .containsExactly(RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT,
            RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK,
            RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK, RubikCube.INITIAL_COLOR_BACK);
    assertThat(rubikCube.getLeftItems())
        .as("check left side after top rotation. But Got: %s", rubikCube.getLeftItems())
        .extracting(CubeItem::getLeft)
        .containsExactly(RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT, RubikCube.INITIAL_COLOR_FRONT,
            RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT,
            RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT, RubikCube.INITIAL_COLOR_LEFT);
  }

  @Test
  void rotate_top_counterClockwise() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_TOP);
    rubikCube.rotate(RotateDirection.TOP);
    assertThat(rubikCube)
        .isEqualTo(new RubikCube());
  }
}