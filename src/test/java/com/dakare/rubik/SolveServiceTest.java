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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(TestConfig.class)
@DirtiesContext
class SolveServiceTest {

  @Autowired
  private SolveService solveService;

  @Autowired
  private RubikCube rubikCube;

  @Autowired
  private AnimationPlayService animationPlayService;

  @BeforeEach
  void setup() {
    doAnswer(invocation -> {
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
}

class TestConfig {

  @Bean
  public RubikCube rubikCube() {
    return RubikCube.createDefault();
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
