package com.dakare.rubik.model;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Wrapper class for meaningful {@link #toString()}.
 */
@Getter
@RequiredArgsConstructor
public enum ColorWrapper {

  WHITE(Color.WHITE),
  GREEN(Color.GREEN),
  RED(Color.RED),
  ORANGE(Color.ORANGE),
  YELLOW(Color.YELLOW),
  BLUE(Color.BLUE),
  BLACK(Color.BLACK);

  private final Color color;
}
