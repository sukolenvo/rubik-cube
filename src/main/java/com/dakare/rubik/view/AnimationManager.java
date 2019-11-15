package com.dakare.rubik.view;

import com.dakare.rubik.rotate.RotateDirection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AnimationManager {

  private AtomicBoolean animating = new AtomicBoolean();

  public void animate(List<ItemView> itemViews, RotateDirection rotateDirection) {
    List<ItemView> itemsToMove = itemViews.stream()
        .filter(ItemView::requiresMove)
        .collect(Collectors.toList());
    if (!itemsToMove.isEmpty()) {
      animating.set(true);
      RotateSideAnimation rotateSideAnimation = new RotateSideAnimation(itemsToMove, rotateDirection);
      rotateSideAnimation.setOnFinished(event -> {
        itemsToMove.forEach(item -> {
          animating.set(false);
        });
      });
      rotateSideAnimation.play();
    }
  }

  public boolean isAnimating() {
    return animating.get();
  }
}
