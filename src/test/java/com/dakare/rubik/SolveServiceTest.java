package com.dakare.rubik;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import com.dakare.rubik.model.ColorWrapper;
import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.view.AnimationPlayService;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(TestConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class SolveServiceTest {

  private static final Logger log = LoggerFactory.getLogger(SolveServiceTest.class);

  @Autowired
  private SolveService solveService;

  @Autowired
  private RubikCube rubikCube;

  @Autowired
  private AnimationPlayService animationPlayService;

  @BeforeEach
  void setup() {
    doAnswer(invocation -> {
      log.debug("Rotating {}", (RotateDirection) invocation.getArgument(0));
      rubikCube.rotate(invocation.getArgument(0));
      return null;
    }).when(animationPlayService).executeAction(any());
  }

  @Test
  void putCrossCubeItemInPlace_z0() {
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.FRONT);
    solveService.putCrossCubeItemInPlace(RotateDirection.LEFT, ColorWrapper.BLUE, 0, 1);
    CubeItem cubeItem = rubikCube.getItems()[3];
    assertThat(cubeItem.isInPlaceIgnoreRotation())
        .as("expecting cube item to be moved on position 3, but got %s", cubeItem)
        .isTrue();
  }

  @Test
  void putCrossCubeItemInPlace_side() {
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    solveService.putCrossCubeItemInPlace(RotateDirection.LEFT, ColorWrapper.BLUE, 0, 1);
    CubeItem cubeItem = rubikCube.getItems()[3];
    assertThat(cubeItem.isInPlaceIgnoreRotation())
        .as("expecting cube item to be moved on position 3, but got %s", cubeItem)
        .isTrue();
  }

  @Test
  void rotateCrossCubeItemIfNeeded_side() {
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    solveService.putCrossCubeItemInPlace(RotateDirection.LEFT, ColorWrapper.BLUE, 0, 1);
    CubeItem cubeItem = rubikCube.getItems()[3];
    Preconditions.checkArgument(cubeItem.isInPlaceIgnoreRotation(), "expecting item to be in place before rotating");
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting item to require rotation before test");

    solveService.rotateCrossCubeItemIfNeeded(cubeItem);

    assertThat(cubeItem.isInPlace())
        .as("expecting cube item to be rotated, but it was: %s", cubeItem)
        .isTrue();
  }

  @Test
  void completeFirstSideCorner_index8() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    CubeItem cubeItem = rubikCube.getItems()[2];
    Preconditions.checkArgument(!cubeItem.isInPlace(), "cube item expected to be moved");
    solveService.completeFirstSideCorner(cubeItem, 2, 0);
    assertThat(cubeItem.isInPlace())
        .as("expecting %s to be in place", cubeItem)
        .isTrue();
  }

  @Test
  void rotateIfNeeded_left() {
    rubikCube.rotate(RotateDirection.RIGHT);
    rubikCube.rotate(RotateDirection.TOP);
    rubikCube.rotate(RotateDirection.FRONT);
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.GREEN);
    Preconditions.checkArgument(cubeItem.isInPlaceIgnoreRotation() && !cubeItem.isInPlace(),
        "cube item should be on right position but with wrong rotation");
    solveService.rotateCrossCubeItemIfNeeded(cubeItem);
    assertThat(cubeItem.isInPlace())
        .as("check cube item is rotated appropriately")
        .isTrue();
  }

  @Test
  void rotateIfNeeded_right() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_TOP);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.WHITE, ColorWrapper.BLUE);
    Preconditions.checkArgument(cubeItem.isInPlaceIgnoreRotation() && !cubeItem.isInPlace(),
        "cube item should be on left position but with wrong rotation");
    solveService.rotateCrossCubeItemIfNeeded(cubeItem);
    assertThat(cubeItem.isInPlace())
        .as("check cube item is rotated appropriately")
        .isTrue();
  }

  @Test
  void completeLevelTwoItem_redGreen_fromIndex9() {
    rubikCube.rotate(RotateDirection.TOP);
    rubikCube.rotate(RotateDirection.TOP);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.GREEN);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.COUNTER_CLOCKWISE_TOP, RotateDirection.RIGHT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void completeLevelTwoItem_redGreen_fromIndex17() {
    rubikCube.rotate(RotateDirection.RIGHT);
    rubikCube.rotate(RotateDirection.RIGHT);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.GREEN);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.COUNTER_CLOCKWISE_TOP, RotateDirection.RIGHT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void completeLevelTwoItem_redGreen_fromIndex25() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_TOP);
    rubikCube.rotate(RotateDirection.BACK);
    rubikCube.rotate(RotateDirection.BACK);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.GREEN);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.COUNTER_CLOCKWISE_TOP, RotateDirection.RIGHT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void completeLevelTwoItem_redBlue_fromIndex11() {
    rubikCube.rotate(RotateDirection.TOP);
    rubikCube.rotate(RotateDirection.TOP);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.BLUE);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.TOP, RotateDirection.COUNTER_CLOCKWISE_LEFT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void completeLevelTwoItem_redBlue_fromIndex15() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.BLUE);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.TOP, RotateDirection.COUNTER_CLOCKWISE_LEFT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void completeLevelTwoItem_redBlue_fromIndex25() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.BACK);
    List<CubeItem> frontItems = rubikCube.getFrontItems().stream().map(CubeItem::new).collect(Collectors.toList());
    CubeItem cubeItem = rubikCube.findByColors(ColorWrapper.RED, ColorWrapper.BLUE);
    Preconditions.checkArgument(!cubeItem.isInPlace(), "expecting initial position to be wrong for %s", cubeItem);
    solveService.completeLevelTwoItem(cubeItem, RotateDirection.TOP, RotateDirection.COUNTER_CLOCKWISE_LEFT);
    assertThat(cubeItem.isInPlace())
        .as("expecting item to be move to correct position, but got %s", cubeItem)
        .isTrue();
    assertThat(rubikCube.getFrontItems())
        .as("front side of rubik cube should remain unchanged")
        .containsExactlyElementsOf(frontItems);
  }

  @Test
  void moveCorners_swapBottom() {
    rubikCube.rotate(RotateDirection.BOTTOM);
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.LEFT);
    solveService.moveCorners(0);
    assertThat(rubikCube.getItems()[18].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[18])
        .isTrue();
    assertThat(rubikCube.getItems()[20].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[20])
        .isTrue();
    assertThat(rubikCube.getItems()[24].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[24])
        .isTrue();
    assertThat(rubikCube.getItems()[26].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[26])
        .isTrue();
  }

  @Test
  void moveCorners_swapRight() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.BOTTOM);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_BOTTOM);
    solveService.moveCorners(0);
    assertThat(rubikCube.getItems()[18].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[18])
        .isTrue();
    assertThat(rubikCube.getItems()[20].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[20])
        .isTrue();
    assertThat(rubikCube.getItems()[24].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[24])
        .isTrue();
    assertThat(rubikCube.getItems()[26].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[26])
        .isTrue();
  }

  @Test
  void moveCorners_swapDiagonal() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.RIGHT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.RIGHT);
    solveService.moveCorners(0);
    assertThat(rubikCube.getItems()[18].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[18])
        .isTrue();
    assertThat(rubikCube.getItems()[20].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[20])
        .isTrue();
    assertThat(rubikCube.getItems()[24].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[24])
        .isTrue();
    assertThat(rubikCube.getItems()[26].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[26])
        .isTrue();
  }

  @Test
  void moveCorners_swapThree() {
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_RIGHT);
    rubikCube.rotate(RotateDirection.FRONT);
    rubikCube.rotate(RotateDirection.BOTTOM);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_LEFT);
    rubikCube.rotate(RotateDirection.COUNTER_CLOCKWISE_FRONT);
    rubikCube.rotate(RotateDirection.LEFT);
    solveService.moveCorners(0);
    assertThat(rubikCube.getItems()[18].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[18])
        .isTrue();
    assertThat(rubikCube.getItems()[20].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[20])
        .isTrue();
    assertThat(rubikCube.getItems()[24].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[24])
        .isTrue();
    assertThat(rubikCube.getItems()[26].isInPlaceIgnoreRotation())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[26])
        .isTrue();
  }

  @Test
  void solve() {
    rubikCube.mix();
    solveService.solve();
    for (int i = 0; i < 18; i++) {
      if (i == 11) {
        continue;
      }
      assertThat(rubikCube.getItems()[i].isInPlace())
          .as("first two levels should be in place, but it was not for %s", rubikCube.getItems()[i])
          .isTrue();
    }
    assertThat(rubikCube.getItems()[18].isInPlace())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[18])
        .isTrue();
    assertThat(rubikCube.getItems()[20].isInPlace())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[20])
        .isTrue();
    assertThat(rubikCube.getItems()[24].isInPlace())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[24])
        .isTrue();
    assertThat(rubikCube.getItems()[26].isInPlace())
        .as("expecting to move corners, but got %s", rubikCube.getItems()[26])
        .isTrue();
  }
}

class TestConfig {

  @Bean
  public RubikCube rubikCube() {
    return new RubikCube();
  }

  @Bean
  public SolveService solveService() {
    return new SolveService();
  }

  @Bean
  public AnimationPlayService animationPlayService() {
    return mock(AnimationPlayService.class);
  }
}
