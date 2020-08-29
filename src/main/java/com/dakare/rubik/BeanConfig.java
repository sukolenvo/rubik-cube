package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanConfig {

  @Value("${app.cube.mode:MIXED}")
  private CubeMode cubeMode;

  @Bean
  public RubikCube getRubikCube() {
    RubikCube rubikCube = new RubikCube();
    if (cubeMode == CubeMode.MIXED) {
      rubikCube.mix();
      log.info("Rubik cube is mixed. New state is {}", rubikCube.getCurrentState());
    }
    return rubikCube;
  }
//
//  @Bean
//  public AnimationPlayService animationPlayService() {
//    return new AnimationPlayServiceImpl();
//  }
}
