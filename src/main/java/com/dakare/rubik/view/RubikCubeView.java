package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import java.util.List;
import javafx.scene.Group;

public class RubikCubeView extends Group {

  private final RubikCube rubikCube;
  private final AnimationManager animationManager;

  public RubikCubeView() {
    animationManager = new AnimationManager();
    rubikCube = RubikCube.createMixed();
    setItems();
  }

  private void setItems() {
    for (CubeItem item : rubikCube.getItems()) {
      getChildren().add(new ItemView(item));
    }
  }

  @SuppressWarnings("unchecked")
  public void rotate(RotateDirection rotateDirection) {
    if (!animationManager.isAnimating()) {
      rubikCube.rotate(rotateDirection);
      animationManager.animate((List) getChildren(), rotateDirection);
    }
  }
}
