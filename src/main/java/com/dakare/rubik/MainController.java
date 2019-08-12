package com.dakare.rubik;

import com.dakare.rubik.model.ColorsHelper;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.view.CubeView;
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

    @FXML
    private void initialize() {
        Group root = new Group();
        RubikCube rubikCube = new RubikCube();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    CubeView cube = new CubeView(rubikCube.getCubeItem(x, y, z));
                    cube.setTranslateX(CubeView.SIZE * (x - 1));
                    cube.setTranslateY(CubeView.SIZE * (y - 1));
                    cube.setTranslateZ(CubeView.SIZE * (z - 1));
                    root.getChildren().add(cube);
                }
            }
        }
        rotateVertical = new Rotate(45, Rotate.X_AXIS);
        root.getTransforms().add(rotateVertical);
        rotateHorizontal = new Rotate(45, Rotate.Y_AXIS);
        root.getTransforms().add(rotateHorizontal);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-10 * CubeView.SIZE);
        camera.setFarClip(300);
        this.root.getChildren().add(camera);
        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.DISABLED);
        subScene.setCamera(camera);
        subScene.setFill(Color.grayRgb(150));
        handleMouse(subScene);
        this.root.getChildren().add(subScene);
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
