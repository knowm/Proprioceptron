package com.xeiam.proprioceptron;

/**
 * these are really angular impulses, but timing has not yet been specified
 * 
 * @author Zackkenyon
 * @create Sep 11, 2012
 */
class TorqueState implements State {

  FreeVar[] torques;

  @Override
  public FreeVar[] toVector() {

    return torques;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { torques.length + " torques" }; // + id
  }

  @Override
  public void addVars(FreeVar[] torques) {

    this.torques = torques;

  }
}