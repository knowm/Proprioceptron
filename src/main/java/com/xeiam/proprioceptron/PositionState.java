package com.xeiam.proprioceptron;


public class PositionState implements State {

  FreeVar[] positions;

  @Override
  public String[] vectorDoc() {

    return new String[] { "Positions * " + positions.length };
  }

  @Override
  public FreeVar[] toVector() {

    // TODO Auto-generated method stub
    return positions;
  }

  @Override
  public void addVars(FreeVar[] positions) {

    this.positions = positions;

  }

}
