package com.xeiam.proprioceptron.thematrix;


public class PlayerCommand {

  float rotation;
  float forwardmotion;

  public PlayerCommand(float rotation, float forwardmotion) {

    this.rotation = rotation;
    this.forwardmotion = forwardmotion;

  }

  public float getRotation() {

    return rotation;
  }

  public float getForwardMotion() {

    return forwardmotion;
  }
}
