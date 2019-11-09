package com.dakare.rubik.rotate;

import java.util.Arrays;
import java.util.Optional;
import javafx.scene.input.KeyCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RotateDirection {

    FRONT(KeyCode.R, true),
    COUNTER_CLOCKWISE_FRONT(KeyCode.T, false),
    RIGHT(KeyCode.G, true),
    COUNTER_CLOCKWISE_RIGHT(KeyCode.B, false),
    TOP(KeyCode.E, true),
    COUNTER_CLOCKWISE_TOP(KeyCode.D, false);

    private final KeyCode key;
    private final boolean clockwise;

    public static Optional<RotateDirection> getByKeyCode(KeyCode keyCode) {
        return Arrays.stream(values())
            .filter(item -> item.key == keyCode)
            .findAny();
    }
}