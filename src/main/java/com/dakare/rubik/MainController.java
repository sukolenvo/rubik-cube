package com.dakare.rubik;

import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.view.ItemView;
import com.dakare.rubik.view.RubikCubeView;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

  @FXML
  private Group root;
  private Rotate rotateVertical;
  private Rotate rotateHorizontal;
  private double sceneX;
  private double sceneY;
  private RubikCubeView rubikCubeView;
  private AtomicBoolean rotating = new AtomicBoolean();

  @FXML
  private void initialize() {
    rubikCubeView = new RubikCubeView();
    rotateVertical = new Rotate(45, Rotate.X_AXIS);
    rubikCubeView.getTransforms().add(rotateVertical);
    rotateHorizontal = new Rotate(45, Rotate.Y_AXIS);
    rubikCubeView.getTransforms().add(rotateHorizontal);

    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-10 * ItemView.SIZE);
    camera.setFarClip(300);
    this.root.getChildren().add(camera);
    SubScene subScene = new SubScene(rubikCubeView, 800, 600, true, SceneAntialiasing.DISABLED);
    subScene.setCamera(camera);
    subScene.setFill(Color.grayRgb(150));
    handleMouse(subScene);
    Platform.runLater(this::handleKeyboard);
    this.root.getChildren().add(subScene);
  }

  private void handleKeyboard() {
    root.getScene().setOnKeyPressed(event -> {
      RotateDirection.getByKeyCode(event.getCode())
          .ifPresent(rubikCubeView::rotate);
    });
  }

  private void rotateFront(boolean clockwise) {
    rotate(clockwise, 0, 1, 2, 3, 4, 5, 6, 7, 8);
  }

  private void rotate(boolean clockwise, int... indices) {
    if (rotating.compareAndSet(false, true)) {
      Preconditions.checkArgument(indices.length == 9,
          "expecting 9 items for rotation, got %d", indices.length);
      rotate(clockwise, indices[0], indices[2]);
      rotate(clockwise, indices[1], indices[5]);
      rotate(clockwise, indices[2], indices[8]);
      rotate(clockwise, indices[5], indices[7]);
      rotate(clockwise, indices[8], indices[6]);
      rotate(clockwise, indices[7], indices[3]);
      rotate(clockwise, indices[6], indices[0]);
      rotate(clockwise, indices[3], indices[1]);
      rotate(clockwise, indices[4], indices[4]);
//      rubikCube.swap(indices[0], indices[2]);
//      rubikCube.swap(indices[1], indices[5]);
//      rubikCube.swap(indices[0], clockwise ? indices[8] : indices[6]);
//      rubikCube.swap(indices[1], clockwise ? indices[7] : indices[3]);
//      rubikCube.swap(indices[0], clockwise ? indices[6] : indices[8]);
//      rubikCube.swap(indices[1], clockwise ? indices[3] : indices[6]);
    }
  }

  private void rotate(boolean clockwise, int first, int second) {
//    int from;
//    int to;
//    if (clockwise) {
//      from = first;
//      to = second;
//    } else {
//      from = second;
//      to = first;
//    }
//    Node node = rubikCubeView.getChildren().get(rubikCube.getCubeItem(from).getIndex());
//    double initialRotate = node.getRotate();
//    int angleMultiplier = clockwise ? 1 : -1;
//    Transition transition = new Transition() {
//
//      {
//        setCycleDuration(Duration.seconds(1));
//        setCycleCount(1);
//      }
//
//      @Override
//      protected void interpolate(double frac) {
//        node.setRotate(initialRotate + angleMultiplier * 90 * frac);
//        node.setTranslateX(
//            getXCoordinate(from) + (getXCoordinate(to) - getXCoordinate(from)) * frac);
//        node.setTranslateY(
//            getYCoordinate(from) + (getYCoordinate(to) - getYCoordinate(from)) * frac);
//        node.setTranslateZ(
//            getZCoordinate(from) + (getZCoordinate(to) - getZCoordinate(from)) * frac);
//      }
//    };
//    transition.setOnFinished(event -> {
//      node.setRotate(initialRotate + 90 * angleMultiplier);
//      node.setTranslateX(getXCoordinate(to));
//      node.setTranslateY(getYCoordinate(to));
//      node.setTranslateZ(getZCoordinate(to));
//      rotating.set(false);
//    });
//    transition.play();
  }

  private void handleMouse(SubScene subScene) {
    subScene.setOnMousePressed(event -> {
      sceneX = event.getSceneX();
      sceneY = event.getSceneY();
    });

    subScene.setOnMouseDragged(event -> {
      double diffX = event.getSceneX() - sceneX;
      double diffY = event.getSceneY() - sceneY;
      if (Math.abs(diffX) > 1) {
        rotateHorizontal.setAngle((rotateHorizontal.getAngle() - diffX) * 1);
        sceneX = event.getSceneX();
      }
      if (Math.abs(diffY) > 1) {
        rotateVertical.setAngle((rotateVertical.getAngle() + diffY) * 1);
        sceneY = event.getSceneY();
      }
    });
  }
}
