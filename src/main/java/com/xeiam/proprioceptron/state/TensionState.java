package com.xeiam.proprioceptron.state;

import com.xeiam.proprioceptron.FreeVar;

public class TensionState implements State {

  public FreeVar[] tensions;
  public String[] doc;

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