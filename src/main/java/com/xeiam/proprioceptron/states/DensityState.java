package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.State;


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
  public void addVars(FreeVar[] vars) {

    densities = vars;
  }
}