package com.dakare.rubik.rotate;

import java.util.Arrays;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RotateDirection {

  FRONT(KeyCode.Q, true),
  COUNTER_CLOCKWISE_FRONT(KeyCode.W, false),
  TOP(KeyCode.E, true),
  COUNTER_CLOCKWISE_TOP(KeyCode.R, false),
  RIGHT(KeyCode.T, true),
  COUNTER_CLOCKWISE_RIGHT(KeyCode.Y, false),
  BACK(KeyCode.A, true),
  COUNTER_CLOCKWISE_BACK(KeyCode.S, false),
  BOTTOM(KeyCode.D, true),
  COUNTER_CLOCKWISE_BOTTOM(KeyCode.F, false),
  LEFT(KeyCode.G, true),
  COUNTER_CLOCKWISE_LEFT(KeyCode.H, false);

  private final KeyCode key;
  private final boolean clockwise;

  public static Optional<RotateDirection> getByKeyCode(KeyCode keyCode) {
    return Arrays.stream(values())
        .filter(item -> item.key == keyCode)
        .findAny();
  }

  public RotateDirection getOppositeDirection() {
    switch (this) {
      case FRONT:
        return COUNTER_CLOCKWISE_FRONT;
      case COUNTER_CLOCKWISE_FRONT:
        return FRONT;
      case TOP:
        return COUNTER_CLOCKWISE_TOP;
      case COUNTER_CLOCKWISE_TOP:
        return TOP;
      case RIGHT:
        return COUNTER_CLOCKWISE_RIGHT;
      case COUNTER_CLOCKWISE_RIGHT:
        return RIGHT;
      case BACK:
        return COUNTER_CLOCKWISE_BACK;
      case COUNTER_CLOCKWISE_BACK:
        return BACK;
      case BOTTOM:
        return COUNTER_CLOCKWISE_BOTTOM;
      case COUNTER_CLOCKWISE_BOTTOM:
        return BOTTOM;
      case LEFT:
        return COUNTER_CLOCKWISE_LEFT;
      case COUNTER_CLOCKWISE_LEFT:
        return LEFT;
      default:
        throw new IllegalStateException("Unexpected rotation type " + this);
    }
  }
}
