package com.dakare.rubik.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.dakare.rubik.rotate.RotateDirection;
import org.junit.jupiter.api.Test;

class CubeItemTest {

  @Test
  void leftTopFrontCorner() {
    CubeItem cubeItem = new CubeItem(0);
    assertThat(cubeItem)
        .as("check getX()")
        .returns(0, CubeItem::getX)
        .as("check getY()")
        .returns(0, CubeItem::getY)
        .as("check getZ()")
        .returns(0, CubeItem::getZ);
  }

  @Test
  void rightTopFrontCorner() {
    CubeItem cubeItem = new CubeItem(RubikCube.SIZE - 1);
    assertThat(cubeItem)
        .as("check getX()")
        .returns(RubikCube.SIZE - 1, CubeItem::getX)
        .as("check getY()")
        .returns(0, CubeItem::getY)
        .as("check getZ()")
        .returns(0, CubeItem::getZ);
  }

  @Test
  void leftBottomFrontCorner() {
    CubeItem cubeItem = new CubeItem(RubikCube.SIZE * (RubikCube.SIZE - 1));
    assertThat(cubeItem)
        .as("check getX()")
        .returns(0, CubeItem::getX)
        .as("check getY()")
        .returns(RubikCube.SIZE - 1, CubeItem::getY)
        .as("check getZ()")
        .returns(0, CubeItem::getZ);
  }

  @Test
  void rightBottomFrontCorner() {
    CubeItem cubeItem = new CubeItem(RubikCube.SIZE * RubikCube.SIZE - 1);
    assertThat(cubeItem)
        .as("check getX()")
        .returns(RubikCube.SIZE - 1, CubeItem::getX)
        .as("check getY()")
        .returns(RubikCube.SIZE - 1, CubeItem::getY)
        .as("check getZ()")
        .returns(0, CubeItem::getZ);
  }

  @Test
  void leftTopBackCorner() {
    CubeItem cubeItem = new CubeItem(getBackSideIndex());
    assertThat(cubeItem)
        .as("check getX()")
        .returns(0, CubeItem::getX)
        .as("check getY()")
        .returns(0, CubeItem::getY)
        .as("check getZ()")
        .returns(RubikCube.SIZE - 1, CubeItem::getZ);
  }

  private int getBackSideIndex() {
    return RubikCube.SIZE * RubikCube.SIZE * (RubikCube.SIZE - 1);
  }

  @Test
  void rightTopBckCorner() {
    CubeItem cubeItem = new CubeItem(getBackSideIndex() + RubikCube.SIZE - 1);
    assertThat(cubeItem)
        .as("check getX()")
        .returns(RubikCube.SIZE - 1, CubeItem::getX)
        .as("check getY()")
        .returns(0, CubeItem::getY)
        .as("check getZ()")
        .returns(RubikCube.SIZE - 1, CubeItem::getZ);
  }

  @Test
  void leftBottomBackCorner() {
    CubeItem cubeItem = new CubeItem(getBackSideIndex() + RubikCube.SIZE * (RubikCube.SIZE - 1));
    assertThat(cubeItem)
        .as("check getX()")
        .returns(0, CubeItem::getX)
        .as("check getY()")
        .returns(RubikCube.SIZE - 1, CubeItem::getY)
        .as("check getZ()")
        .returns(RubikCube.SIZE - 1, CubeItem::getZ);
  }

  @Test
  void rightBottomBackCorner() {
    CubeItem cubeItem = new CubeItem(getBackSideIndex() + RubikCube.SIZE * RubikCube.SIZE - 1);
    assertThat(cubeItem)
        .as("check getX()")
        .returns(RubikCube.SIZE - 1, CubeItem::getX)
        .as("check getY()")
        .returns(RubikCube.SIZE - 1, CubeItem::getY)
        .as("check getZ()")
        .returns(RubikCube.SIZE - 1, CubeItem::getZ);
  }

  @Test
  void rotateFront() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.FRONT);
    assertThat(copy)
        .as("check top moved to right")
        .returns(original.getTop(), CubeItem::getRight)
        .as("check right moved to bottom")
        .returns(original.getRight(), CubeItem::getBottom)
        .as("check bottom moved to left")
        .returns(original.getBottom(), CubeItem::getLeft)
        .as("check left moved to top")
        .returns(original.getLeft(), CubeItem::getTop)
        .as("check front and back not changed")
        .returns(original.getFront(), CubeItem::getFront)
        .returns(original.getBack(), CubeItem::getBack);
  }

  @Test
  void rotateFrontCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    copy.rotate(RotateDirection.FRONT);
    assertThat(copy)
        .isEqualTo(original);
  }

  @Test
  void rotateTop() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.TOP);
    assertThat(copy)
        .as("check front moved to left")
        .returns(original.getFront(), CubeItem::getLeft)
        .as("check left moved to back")
        .returns(original.getLeft(), CubeItem::getBack)
        .as("check back moved to right")
        .returns(original.getBack(), CubeItem::getRight)
        .as("check right moved to front")
        .returns(original.getRight(), CubeItem::getFront)
        .as("check top and bottom not changed")
        .returns(original.getTop(), CubeItem::getTop)
        .returns(original.getBottom(), CubeItem::getBottom);
  }

  @Test
  void rotateTopCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.COUNTER_CLOCKWISE_TOP);
    copy.rotate(RotateDirection.TOP);
    assertThat(copy)
        .isEqualTo(original);
  }

  @Test
  void rotateRight() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.RIGHT);
    assertThat(copy)
        .as("check front moved to top")
        .returns(original.getFront(), CubeItem::getTop)
        .as("check top moved to back")
        .returns(original.getTop(), CubeItem::getBack)
        .as("check back moved to bottom")
        .returns(original.getBack(), CubeItem::getBottom)
        .as("check bottom moved to front")
        .returns(original.getBottom(), CubeItem::getFront)
        .as("check left and right not changed")
        .returns(original.getLeft(), CubeItem::getLeft)
        .returns(original.getRight(), CubeItem::getRight);
  }

  @Test
  void rotateRightCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    copy.rotate(RotateDirection.RIGHT);
    assertThat(copy)
        .isEqualTo(original);
  }

  @Test
  void rotateBack() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.BACK);
    original.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    assertThat(copy)
        .as("back is same as counter clockwise front")
        .isEqualTo(original);
  }

  @Test
  void rotateBackCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.COUNTER_CLOCKWISE_BACK);
    original.rotate(RotateDirection.FRONT);
    assertThat(copy)
        .as("front is same as counter clockwise back")
        .isEqualTo(original);
  }

  @Test
  void rotateLeft() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.LEFT);
    original.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    assertThat(copy)
        .as("left is same as counter clockwise right")
        .isEqualTo(original);
  }

  @Test
  void rotateLeftCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.RIGHT);
    original.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    assertThat(copy)
        .as("right is same as counter clockwise left")
        .isEqualTo(original);
  }

  @Test
  void rotateBottom() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.BOTTOM);
    original.rotate(RotateDirection.TOP);
    assertThat(copy)
        .as("bottom is same as top")
        .isEqualTo(original);
  }

  @Test
  void rotateBottomCounterClockwise() {
    CubeItem original = RandomBeanUtils.randomCubeItem();
    CubeItem copy = new CubeItem(original);
    copy.rotate(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
    original.rotate(RotateDirection.COUNTER_CLOCKWISE_TOP);
    assertThat(copy)
        .as("counter clockwise bottom is same as counter clockwise top")
        .isEqualTo(original);
  }

  @Test
  void isOfColors() {
    CubeItem cubeItem = new CubeItem(0);
    cubeItem.setFront(ColorWrapper.WHITE);
    assertThat(cubeItem.isOfColors(ColorWrapper.WHITE))
        .isTrue();
    assertThat(cubeItem.isOfColors(ColorWrapper.BLUE, ColorWrapper.WHITE))
        .isFalse();
    cubeItem.setLeft(ColorWrapper.BLUE);
    assertThat(cubeItem.isOfColors(ColorWrapper.WHITE))
        .isFalse();
    assertThat(cubeItem.isOfColors(ColorWrapper.BLUE, ColorWrapper.WHITE))
        .isTrue();
  }

  @Test
  void isInPlace_default() {
    for (CubeItem item : new RubikCube().getItems()) {
      assertThat(item.isInPlace())
          .as("expecting every item to be in right place, but %s was not", item)
          .isTrue();
    }
  }

  @Test
  void isInPlace_wrongColor() {
    CubeItem wrongColor = new CubeItem(0);
    wrongColor.setFront(ColorWrapper.BLUE);
    assertThat(wrongColor.isInPlace())
        .as("blue is on the side, white is in front")
        .isFalse();

  }

  @Test
  void isInPlace_rightColorsWithWrongRotation() {
    CubeItem wrongColor = new CubeItem(0);
    wrongColor.setFront(ColorWrapper.BLUE);
    wrongColor.setLeft(ColorWrapper.WHITE);
    assertThat(wrongColor.isInPlace())
        .as("rotation of cube item is incorrect")
        .isFalse();

  }

  @Test
  void isInPlaceIgnoreRotation_default() {
    for (CubeItem item : new RubikCube().getItems()) {
      assertThat(item.isInPlaceIgnoreRotation())
          .as("expecting every item to be in right place, but %s was not", item)
          .isTrue();
    }
  }

  @Test
  void isInPlaceIgnoreRotation_wrongColor() {
    CubeItem wrongColor = new CubeItem(4); // mid front
    wrongColor.setFront(ColorWrapper.BLUE);
    assertThat(wrongColor.isInPlaceIgnoreRotation())
        .as("blue is on the side, white is in front")
        .isFalse();

  }

  @Test
  void isInPlaceIgnoreRotation_rightColorsWithWrongRotation() {
    CubeItem wrongColor = new CubeItem(3); // front left
    wrongColor.setFront(ColorWrapper.BLUE);
    wrongColor.setLeft(ColorWrapper.WHITE);
    assertThat(wrongColor.isInPlaceIgnoreRotation())
        .as("colors are right, rotation of cube item is incorrect")
        .isTrue();

  }

  @Test
  void expectedPosition() {
    RubikCube rubikCube = new RubikCube();
    for (CubeItem item : rubikCube.getItems()) {
      assertThat(item.getExpectedPosition())
          .as("all items are in place, so expected position should match index, but got %s", item)
          .isEqualTo(item.getIndex());
    }
  }
}