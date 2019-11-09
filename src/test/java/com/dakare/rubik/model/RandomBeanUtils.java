package com.dakare.rubik.model;

import com.github.javafaker.Faker;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

public class RandomBeanUtils {

  private static final EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom();
  private static final Faker faker = new Faker();

  public static CubeItem randomCubeItem() {
    return new CubeItem(faker.number().numberBetween(0, RubikCube.SIZE * RubikCube.SIZE * RubikCube.SIZE))
        .setTop(random.nextObject(ColorWrapper.class))
        .setBottom(random.nextObject(ColorWrapper.class))
        .setLeft(random.nextObject(ColorWrapper.class))
        .setRight(random.nextObject(ColorWrapper.class))
        .setFront(random.nextObject(ColorWrapper.class))
        .setBack(random.nextObject(ColorWrapper.class));
  }
}
