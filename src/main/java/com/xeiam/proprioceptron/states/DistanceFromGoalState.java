package com.xeiam.proprioceptron.states;


public class DistanceFromGoalState extends State {

  @Override
  public String[] vectorDoc() {

    return new String[] { "distancesfromgoal * " + vars.length };
  }
}