package com.dakare.rubik;

import com.dakare.rubik.model.ColorsHelper;
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
        drawLayer(root, -1);
        drawLayer(root, 0);
        drawLayer(root, 1);
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

    private void drawLayer(Group root, int depth) {
        CubeView leftBottom = new CubeView(ColorsHelper.random());
        leftBottom.setTranslateX(-1 * CubeView.SIZE);
        leftBottom.setTranslateY(CubeView.SIZE);
        leftBottom.setTranslateZ(depth * CubeView.SIZE);

        CubeView bottom = new CubeView(ColorsHelper.random());
        bottom.setTranslateX(0);
        bottom.setTranslateY( CubeView.SIZE);
        bottom.setTranslateZ(depth * CubeView.SIZE);

        CubeView rightBottom = new CubeView(ColorsHelper.random());
        rightBottom.setTranslateX(CubeView.SIZE);
        rightBottom.setTranslateY(CubeView.SIZE);
        rightBottom.setTranslateZ(depth * CubeView.SIZE);

        CubeView leftMiddle = new CubeView(ColorsHelper.random());
        leftMiddle.setTranslateX(-1 * CubeView.SIZE);
        leftMiddle.setTranslateZ(depth * CubeView.SIZE);

        CubeView middle = new CubeView(ColorsHelper.random());
        middle.setTranslateZ(depth * CubeView.SIZE);

        CubeView rightMiddle = new CubeView(ColorsHelper.random());
        rightMiddle.setTranslateX(CubeView.SIZE);
        rightMiddle.setTranslateZ(depth * CubeView.SIZE);

        CubeView leftTop = new CubeView(ColorsHelper.random());
        leftTop.setTranslateX(-CubeView.SIZE);
        leftTop.setTranslateY(-CubeView.SIZE);
        leftTop.setTranslateZ(depth * CubeView.SIZE);

        CubeView top = new CubeView(ColorsHelper.random());
        top.setTranslateX(0);
        top.setTranslateY(-CubeView.SIZE);
        top.setTranslateZ(depth * CubeView.SIZE);

        CubeView rightTop = new CubeView(ColorsHelper.random());
        rightTop.setTranslateX(CubeView.SIZE);
        rightTop.setTranslateY(-CubeView.SIZE);
        rightTop.setTranslateZ(depth * CubeView.SIZE);

        root.getChildren().add(leftTop);
        root.getChildren().add(top);
        root.getChildren().add(rightTop);
        root.getChildren().add(leftMiddle);
        root.getChildren().add(middle);
        root.getChildren().add(rightMiddle);
        root.getChildren().add(bottom);
        root.getChildren().add(rightBottom);
        root.getChildren().add(leftBottom);
    }
}
