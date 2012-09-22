package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;

public class EnergyState implements State {

  // this is in anticipation of giving the the learning algorithm a much finer solution. namely one in which
  // energy usage is minimized.
  FreeVar[] energy;

  @Override
  public String[] vectorDoc() {

    return new String[] { "energy * " + energy.length };
  }

  @Override
  public FreeVar[] toVector() {

    return energy;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    energy = vars;

  }

}