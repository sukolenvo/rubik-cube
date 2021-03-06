package com.dakare.rubik;

import com.dakare.rubik.rotate.RotateDirection;
import com.dakare.rubik.view.ItemView;
import com.dakare.rubik.view.RubikCubeView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

  @FXML
  private Group root;
  private Rotate rotateVertical;
  private Rotate rotateHorizontal;
  private double sceneX;
  private double sceneY;
  @Autowired
  private RubikCubeView rubikCubeView;
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @FXML
  private void initialize() {
    Group rootGroup = new Group();
    rotateVertical = new Rotate(45, Rotate.X_AXIS);
    rubikCubeView.getTransforms().add(rotateVertical);
    rotateHorizontal = new Rotate(45, Rotate.Y_AXIS);
    rubikCubeView.getTransforms().add(rotateHorizontal);
    rootGroup.getChildren().add(rubikCubeView);

    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-10 * ItemView.SIZE);
    camera.setFarClip(300);
    this.root.getChildren().add(camera);
    SubScene subScene = new SubScene(rootGroup, 800, 600, true, SceneAntialiasing.DISABLED);
    subScene.setCamera(camera);
    subScene.setFill(Color.grayRgb(150));
    handleMouse(subScene);
    Platform.runLater(this::handleKeyboard);
    this.root.getChildren().add(subScene);
    // default light on mac is too dark
    AmbientLight pointLight = new AmbientLight(Color.WHITESMOKE);
    pointLight.setTranslateX(0);
    pointLight.setTranslateY(0);
    pointLight.setTranslateZ(-300);
    pointLight.setRotate(90);
    Group lightGroup = new Group();
    lightGroup.getChildren().add(pointLight);
    rootGroup.getChildren().add(lightGroup);
  }

  private void handleKeyboard() {
    root.getScene().setOnKeyPressed(event -> {
      RotateDirection.getByKeyCode(event.getCode())
          .ifPresent(rubikCubeView::rotate);
      applicationEventPublisher.publishEvent(event.getCode());
    });
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
