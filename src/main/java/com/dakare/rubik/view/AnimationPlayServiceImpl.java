package com.dakare.rubik.view;

import com.dakare.rubik.rotate.RotateDirection;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnimationPlayServiceImpl implements AnimationPlayService {

  @Autowired
  private RubikCubeView rubikCubeView;

  @Value("${app.solve.play-mode:AUTO}")
  private SolvePlayMode solvePlayMode;
  private final Object lock = new Object();


  @EventListener
  public void onAnimationFinished(AnimationFinishedEvent event) {
    log.debug("Animation finished");
    if (solvePlayMode == SolvePlayMode.AUTO) {
      synchronized (lock) {
        lock.notifyAll();
      }
    }
  }

  @EventListener
  public void onKeyPressed(KeyCode keyCode) {
    if (solvePlayMode == SolvePlayMode.KEY_L && keyCode == KeyCode.L) {
      synchronized (lock) {
        lock.notifyAll();
      }
    }
  }

  @Override
  public void executeAction(RotateDirection rotateDirection) {
    rubikCubeView.rotate(rotateDirection);
    synchronized (lock) {
      try {
        lock.wait();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
