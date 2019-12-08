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
}
