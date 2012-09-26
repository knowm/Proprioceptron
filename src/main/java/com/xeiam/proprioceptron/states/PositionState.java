package com.xeiam.proprioceptron.states;


public class PositionState extends State {

  @Override
  public String[] vectorDoc() {

    return new String[] { "Positions * " + vars.length };
  }
}
