package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;

public class AngularVelocityState extends State {

  public FreeVar[] vars;

  @Override
  public FreeVar[] getVars() {

    return vars;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "angular velocities" }; // + id
  }

  @Override
  public void setVars(FreeVar[] vars) {

    this.vars = vars;

  }
}