package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.rotate.RotateDirection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public RubikCube getRubikCube() {
    RubikCube rubikCube = RubikCube.createMixed();
//    rubikCube.rotate(RotateDirection.LEFT);
    return rubikCube;
  }

}
