package com.xeiam.proprioceptron.states;


public class EnergyState extends State {

  // this is in anticipation of giving the the learning algorithm a much finer solution. namely one in which
  // energy usage is minimized.

  @Override
  public String[] vectorDoc() {

    return new String[] { "energy * " + vars.length };
  }
}