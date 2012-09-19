package com.xeiam.proprioceptron;


class DensityState implements State {

  FreeVar[] densities;

  @Override
  public String[] vectorDoc() {

    return new String[] { "densities * " + densities.length };
  }

  @Override
  public FreeVar[] toVector() {

    return densities;
  }

  @Override
  public void addVars(FreeVar[] densities) {

    this.densities = densities;
  }
}