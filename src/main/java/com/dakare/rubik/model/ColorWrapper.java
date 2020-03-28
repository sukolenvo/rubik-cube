package com.dakare.rubik.model;

import java.util.Arrays;
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

  public String getColorLetter() {
    switch (this) {
      case WHITE:
        return "W";
      case GREEN:
        return "G";
      case RED:
        return "R";
      case ORANGE:
        return "O";
      case YELLOW:
        return "Y";
      case BLUE:
        return "U";
      case BLACK:
        return "K";
      default:
        throw new IllegalStateException("Unexpected color " + this);
    }
  }

  public static ColorWrapper fromColorLetter(String letter) {
    return Arrays.stream(values())
        .filter(color -> color.getColorLetter().equals(letter))
        .findAny()
        .orElseThrow(() -> new IllegalStateException("Unexpected color letter: " + letter));
  }
}
