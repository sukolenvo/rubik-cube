package com.dakare.rubik;

import com.dakare.rubik.model.RubikCube;
import com.dakare.rubik.view.AnimationPlayService;
import com.dakare.rubik.view.AnimationPlayServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public RubikCube getRubikCube() {
    return RubikCube.createMixed();
  }
//
//  @Bean
//  public AnimationPlayService animationPlayService() {
//    return new AnimationPlayServiceImpl();
//  }
}
