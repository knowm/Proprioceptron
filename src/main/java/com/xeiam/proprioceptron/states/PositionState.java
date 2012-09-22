package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;

public class PositionState implements State {

  public FreeVar[] positions;

  @Override
  public String[] vectorDoc() {

    return new String[] { "Positions * " + positions.length };
  }

  @Override
  public FreeVar[] toVector() {

    return positions;
  }

  @Override
  public void addVars(FreeVar[] positions) {

    this.positions = positions;

  }

}
