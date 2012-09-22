package com.xeiam.proprioceptron.state;

import com.xeiam.proprioceptron.FreeVar;

public class LengthState implements State {

  public FreeVar[] lengths;

  @Override
  public String[] vectorDoc() {

    return new String[] { "lengths * " + lengths.length };
  }

  @Override
  public FreeVar[] toVector() {

    return lengths;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    lengths = vars;
  }

}