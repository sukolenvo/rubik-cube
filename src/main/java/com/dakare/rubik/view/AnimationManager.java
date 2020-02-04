package com.dakare.rubik.view;

import com.dakare.rubik.rotate.RotateDirection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnimationManager {

  private AtomicBoolean animating = new AtomicBoolean();
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  public void animate(List<ItemView> itemViews, RotateDirection rotateDirection) {
    List<ItemView> itemsToMove = itemViews.stream()
        .filter(ItemView::requiresMove)
        .collect(Collectors.toList());
    log.debug("Rotating {} items in {} direction", itemsToMove.size(), rotateDirection);
    if (!itemsToMove.isEmpty()) {
      animating.set(true);
      Platform.runLater(() -> {
        new RotateSideAnimation(itemsToMove, rotateDirection, event -> {
          animating.set(false);
          applicationEventPublisher.publishEvent(new AnimationFinishedEvent());
        }).play();
      });
    }
  }

  public boolean isAnimating() {
    return animating.get();
  }
}
