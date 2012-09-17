package com.xeiam.proprioceptron;

class DistanceFromGoalState implements State {

  FreeVar[] distances;

  @Override
  public String[] vectorDoc() {

    return new String[] { "distancesfromgoal * " + distances.length };
  }

  @Override
  public FreeVar[] toVector() {

    return distances;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    distances = vars;

  }

}