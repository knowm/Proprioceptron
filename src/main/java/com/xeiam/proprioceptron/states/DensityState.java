package com.xeiam.proprioceptron.states;


public class DensityState extends State {


  @Override
  public String[] vectorDoc() {

    return new String[] { "densities * " + vars.length };
  }
}