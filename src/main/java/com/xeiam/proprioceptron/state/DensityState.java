package com.xeiam.proprioceptron.state;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.states.State;

public class DensityState implements State {

  public FreeVar[] densities;

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