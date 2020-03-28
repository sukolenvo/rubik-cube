package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class BeanConfig {

  @Bean
  public RubikCube getRubikCube() {
    RubikCube rubikCube = new RubikCube();
    rubikCube.mix();
    log.info("Rubik cube is mixed. New state is {}", rubikCube.getCurrentState());
    return rubikCube;
  }
//
//  @Bean
//  public AnimationPlayService animationPlayService() {
//    return new AnimationPlayServiceImpl();
//  }
}
