package com.dakare.rubik.model;

import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CubeItem {
    @Builder.Default
    private Color top = Color.BLACK;
    @Builder.Default
    private Color bottom = Color.BLACK;
    @Builder.Default
    private Color left = Color.BLACK;
    @Builder.Default
    private Color right = Color.BLACK;
    @Builder.Default
    private Color front = Color.BLACK;
    @Builder.Default
    private Color back = Color.BLACK;
}
