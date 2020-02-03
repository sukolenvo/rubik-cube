package com.dakare.rubik.view;

import com.dakare.rubik.model.CubeItem;
import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import java.util.List;
import javafx.scene.Group;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class RubikCubeView extends Group {

  @Autowired
  private RubikCube rubikCube;
  @Autowired
  private AnimationManager animationManager;

  @PostConstruct
  public void setItems() {
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
