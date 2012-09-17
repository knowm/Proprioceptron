package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.State;

class LinearAccelerationState implements State {

  FreeVar[] tensions;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return tensions;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] tensions) {

    this.tensions = tensions;

  }
}