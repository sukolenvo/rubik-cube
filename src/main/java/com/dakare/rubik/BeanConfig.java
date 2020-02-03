package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.view.RubikCubeView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public RubikCube getRubikCube() {
    return RubikCube.createMixed();
  }

}
