package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import javafx.scene.Group;

public class RubikCubeView extends Group {

    private final RubikCube rubikCube;

    public RubikCubeView() {
        rubikCube = new RubikCube();
        setItems();
    }

    private void setItems() {
        for (CubeItem item : rubikCube.getItems()) {
            ItemView cube = new ItemView(item);
            cube.setTranslateX(getXCoordinate(item));
            cube.setTranslateY(getYCoordinate(item));
            cube.setTranslateZ(getZCoordinate(item));
            getChildren().add(cube);
        }
    }

    private int getZCoordinate(CubeItem cubeItem) {
        return ItemView.SIZE * (cubeItem.getZ() - 1);
    }

    private int getYCoordinate(CubeItem cubeItem) {
        return ItemView.SIZE * (cubeItem.getY() - 1);
    }

    private int getXCoordinate(CubeItem cubeItem) {
        return ItemView.SIZE * (cubeItem.getX() - 1);
    }

    public void rotate(RotateDirection rotateDirection) {
        rubikCube.rotate(rotateDirection);
        getChildren().clear();
        setItems();
    }
}
