package com.xeiam.proprioceptron;

class AngularVelocityState implements State {

  FreeVar[] angularvelocities;

  @Override
  public FreeVar[] toVector() {

    return angularvelocities;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "angular velocities" }; // + id
  }

  @Override
  public void addVars(FreeVar[] angularvelocities) {

    this.angularvelocities = angularvelocities;

  }
}