package com.xeiam.proprioceptron;

class TensionState implements State {

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