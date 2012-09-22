package com.xeiam.proprioceptron.states;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.State;

public class AngleState implements State {

  // the angle will be regularly updated as the angle from the last joint. if it points in the same direction as the last joint,
  // then the angle is 0, positive is counterclockwise, negative angles are clockwise.
  public FreeVar[] angles;

  double maxangle; // collision detection will be either difficult to implement or computationally expensive without these fields.
  double[] maxangles;

  public void initialize() {

    maxangles = new double[angles.length];
    maxangles[0] = 360.0;
    for (int i = 1; i < angles.length; i++)
      maxangles[i] = 170.0;
    maxangle = 360.0;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "AngleState * " + angles.length };
  }

  @Override
  public FreeVar[] toVector() {

    return angles;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    angles = vars;

  }

}