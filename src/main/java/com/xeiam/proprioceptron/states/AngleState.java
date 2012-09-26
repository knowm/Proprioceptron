package com.xeiam.proprioceptron.states;


public class AngleState extends State {

  // the angle will be regularly updated as the angle from the last joint. if it points in the same direction as the last joint,
  // then the angle is 0, positive is counterclockwise, negative angles are clockwise.

  double maxangle; // collision detection will be either difficult to implement or computationally expensive without these fields.
  double[] maxangles;

  public void initialize() {

    maxangles = new double[vars.length];
    maxangles[0] = 360.0;
    for (int i = 1; i < vars.length; i++)
      maxangles[i] = 170.0;
    maxangle = 360.0;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "AngleState * " + vars.length };
  }

}