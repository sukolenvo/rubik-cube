package com.dakare.rubik.view;

import com.dakare.rubik.rotate.RotateDirection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.animation.Transition;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class RotateSideAnimation extends Transition {

  private static final Duration ANIMATION_DURATION = Duration.seconds(1);

  private final Map<ItemView, Point3D> fromPosition;
  private final Consumer<Double> rotationFunction;

  public RotateSideAnimation(List<ItemView> items, RotateDirection rotateDirection) {
    fromPosition = items.stream().collect(Collectors.toMap(Function.identity(),
        item -> new Point3D(item.getTranslateX(), item.getTranslateY(), item.getTranslateZ())));
    setCycleDuration(ANIMATION_DURATION);
    setCycleCount(1);
    rotationFunction = prepareRotation(items, rotateDirection);
  }

  private Consumer<Double> prepareRotation(List<ItemView> items, RotateDirection rotateDirection) {
    switch (rotateDirection) {
      case FRONT: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.Z_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialZ) -> item.setAngle(initialZ + 90 * fraction));
        };
      }
      case COUNTER_CLOCKWISE_FRONT: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.Z_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialZ) -> item.setAngle(initialZ - 90 * fraction));
        };
      }
      case RIGHT: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.X_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialX) -> item.setAngle(initialX - 90 * fraction));
        };
      }
      case COUNTER_CLOCKWISE_RIGHT: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.X_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialX) -> item.setAngle(initialX + 90 * fraction));
        };
      }
      case TOP: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.Y_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialY) -> item.setAngle(initialY + 90 * fraction));
        };
      }
      case COUNTER_CLOCKWISE_TOP: {
        Map<Rotate, Double> initialAngles = items.stream()
            .map(item -> item.getRotateAroundAxis(Rotate.Y_AXIS))
            .collect(Collectors.toMap(Function.identity(), Rotate::getAngle));
        return fraction -> {
          initialAngles.forEach((item, initialY) -> item.setAngle(initialY - 90 * fraction));
        };
      }
      default:
        throw new UnsupportedOperationException("Unexpected rotate direction: " + rotateDirection);
    }
  }

  @Override
  protected void interpolate(double frac) {
    rotationFunction.accept(frac);
    fromPosition.forEach((item, initialPosition) -> {
      item.setTranslateX(initialPosition.getX() + (item.getExpectedX() - initialPosition.getX()) * frac);
      item.setTranslateY(initialPosition.getY() + (item.getExpectedY() - initialPosition.getY()) * frac);
      item.setTranslateZ(initialPosition.getZ() + (item.getExpectedZ() - initialPosition.getZ()) * frac);
    });
  }
}
