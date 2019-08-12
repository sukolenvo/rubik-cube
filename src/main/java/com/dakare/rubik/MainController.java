package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.view.CubeView;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @FXML
    private Group root;
    private Rotate rotateVertical;
    private Rotate rotateHorizontal;
    private double sceneX;
    private double sceneY;
    private Group cubeRoot;
    private RubikCube rubikCube;

    @FXML
    private void initialize() {
        cubeRoot = new Group();
        rubikCube = new RubikCube();
        for (int i = 0; i < 27; i++) {
            CubeView cube = new CubeView(rubikCube.getCubeItem(i));
            cube.setTranslateX(getXCoordinate(i));
            cube.setTranslateY(getYCoordinate(i));
            cube.setTranslateZ(getZCoordinate(i));
            cubeRoot.getChildren().add(cube);
        }
        rotateVertical = new Rotate(45, Rotate.X_AXIS);
        cubeRoot.getTransforms().add(rotateVertical);
        rotateHorizontal = new Rotate(45, Rotate.Y_AXIS);
        cubeRoot.getTransforms().add(rotateHorizontal);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-10 * CubeView.SIZE);
        camera.setFarClip(300);
        this.root.getChildren().add(camera);
        SubScene subScene = new SubScene(cubeRoot, 800, 600, true, SceneAntialiasing.DISABLED);
        subScene.setCamera(camera);
        subScene.setFill(Color.grayRgb(150));
        handleMouse(subScene);
        Platform.runLater(this::handleKeyboard);
        this.root.getChildren().add(subScene);
    }

    private int getZCoordinate(int index) {
        return CubeView.SIZE * (RubikCube.getZ(index) - 1);
    }

    private int getYCoordinate(int index) {
        return CubeView.SIZE * (RubikCube.getY(index) - 1);
    }

    private int getXCoordinate(int index) {
        return CubeView.SIZE * (RubikCube.getX(index) - 1);
    }

    private void handleKeyboard() {
        root.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.T) {
                rotateFront(true);
            } else if (event.getCode() == KeyCode.R) {
                rotateFront(false);
            }
        });
    }

    private void rotateFront(boolean clockwise) {
        rotate(0, 2);
        rotate(1, 5);
        rotate(2, 8);
        rotate(5, 7);
        rotate(8, 6);
        rotate(7, 3);
        rotate(6, 0);
        rotate(3, 1);
        rotate(4, 4);
        rubikCube.swap(0, 2);
        rubikCube.swap(1, 5);
        rubikCube.swap(0, 8);
        rubikCube.swap(1, 7);
        rubikCube.swap(0, 6);
        rubikCube.swap(1, 3);
    }

    private void rotate(int from, int to) {
        Node node = cubeRoot.getChildren().get(rubikCube.getCubeItem(from).getIndex());
        double initialRotate = node.getRotate();
        Transition transition = new Transition() {

            {
                setCycleDuration(Duration.seconds(1));
                setCycleCount(1);
            }

            @Override
            protected void interpolate(double frac) {
                node.setRotate(initialRotate + 90 * frac);
                node.setTranslateX(getXCoordinate(from) + (getXCoordinate(to) - getXCoordinate(from)) * frac);
                node.setTranslateY(getYCoordinate(from) + (getYCoordinate(to) - getYCoordinate(from)) * frac);
                node.setTranslateZ(getZCoordinate(from) + (getZCoordinate(to) - getZCoordinate(from)) * frac);
            }
        };
        transition.setOnFinished(event -> {
            node.setRotate(initialRotate + 90);
            node.setTranslateX(getXCoordinate(to));
            node.setTranslateY(getYCoordinate(to));
            node.setTranslateZ(getZCoordinate(to));
        });
        transition.play();
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
