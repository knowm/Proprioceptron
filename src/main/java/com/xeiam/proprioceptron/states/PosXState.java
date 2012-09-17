package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.State;

class PosXState implements State {

  // TODO rewrite in new posx, posy format.
  FreeVar[] posxs;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return posxs;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] posxs) {

    this.posxs = posxs;

  }
}