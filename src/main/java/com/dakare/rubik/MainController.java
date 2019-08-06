package com.dakare.rubik;

import com.dakare.rubik.model.ColorsHelper;
import com.dakare.rubik.view.CubeView;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @FXML
    private Group root;

    @FXML
    private void initialize() {
        Group root = new Group();
        drawLayer(root, -1);
        drawLayer(root, 0);
        drawLayer(root, 1);

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-10 * CubeView.SIZE);
        camera.setTranslateY(-5 * CubeView.SIZE);
        camera.setTranslateX(5 * CubeView.SIZE);
        camera.setRotate(30);
        camera.setRotationAxis(new Point3D(-1, -1, 0));
        camera.setFarClip(300);
        root.getChildren().add(camera);
        SubScene subScene = new SubScene(root, 800, 600, true, SceneAntialiasing.DISABLED);
        subScene.setCamera(camera);
        subScene.setFill(Color.grayRgb(150));
        this.root.getChildren().add(subScene);
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
