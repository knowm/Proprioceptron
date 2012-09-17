package com.xeiam.proprioceptron;

class PosYState implements State {

  // TODO rewrite in new posx, posy format.
  FreeVar[] posys;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return posys;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] posys) {

    this.posys = posys;

  }
}