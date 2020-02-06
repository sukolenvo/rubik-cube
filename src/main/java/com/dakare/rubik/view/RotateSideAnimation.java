package com.dakare.rubik.view;

import com.dakare.rubik.rotate.RotateDirection;
import com.google.common.base.Preconditions;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class RotateSideAnimation extends Transition implements EventHandler<ActionEvent> {

  private static final Duration ANIMATION_DURATION = Duration.millis(350);

  private final Map<ItemView, Point3D> fromPosition;
  private final Consumer<Double> rotationFunction;
  private final List<ItemView> items;
  private final EventHandler<ActionEvent> onFinishedListener;

  public RotateSideAnimation(List<ItemView> items, RotateDirection rotateDirection,
      EventHandler<ActionEvent> onFinishedListener) {
    this.items = items;
    this.onFinishedListener = onFinishedListener;
    fromPosition = items.stream().collect(Collectors.toMap(Function.identity(),
        item -> new Point3D(item.getTranslateX(), item.getTranslateY(), item.getTranslateZ())));
    rotationFunction = prepareRotation(items, rotateDirection);
    setCycleDuration(ANIMATION_DURATION);
    setCycleCount(1);
    setOnFinished(this);
  }

  private Consumer<Double> prepareRotation(List<ItemView> items, RotateDirection rotateDirection) {
    switch (rotateDirection) {
      case COUNTER_CLOCKWISE_BACK:
      case FRONT: {
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(90 * fraction);
      }
      case BACK:
      case COUNTER_CLOCKWISE_FRONT: {
        Rotate rotate = new Rotate(0, Rotate.Z_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(-90 * fraction);
      }
      case COUNTER_CLOCKWISE_LEFT:
      case RIGHT: {
        Rotate rotate = new Rotate(0, Rotate.X_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(-90 * fraction);
      }
      case LEFT:
      case COUNTER_CLOCKWISE_RIGHT: {
        Rotate rotate = new Rotate(0, Rotate.X_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(90 * fraction);
      }
      case BOTTOM:
      case TOP: {
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(90 * fraction);
      }
      case COUNTER_CLOCKWISE_BOTTOM:
      case COUNTER_CLOCKWISE_TOP: {
        Rotate rotate = new Rotate(0, Rotate.Y_AXIS);
        items.forEach(item -> item.getTransforms().add(rotate));
        return fraction -> rotate.setAngle(-90 * fraction);
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

  @Override
  public void handle(ActionEvent event) {
    items.forEach(item -> {
      Preconditions.checkArgument(item.getTransforms().size() == 1,
          "this implementation with other transformations");
      item.getTransforms().clear();
      item.applyColors();
    });
    onFinishedListener.handle(event);
  }
}
