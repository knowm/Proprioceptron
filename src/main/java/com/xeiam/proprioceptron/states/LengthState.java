package com.xeiam.proprioceptron.states;


public class LengthState extends State {


  @Override
  public String[] vectorDoc() {

    return new String[] { "lengths * " + vars.length };
  }
}