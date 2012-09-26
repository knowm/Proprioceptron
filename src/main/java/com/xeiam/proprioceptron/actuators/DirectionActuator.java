package com.xeiam.proprioceptron.actuators;

import com.xeiam.proprioceptron.Vector;
import com.xeiam.proprioceptron.states.AngleState;
import com.xeiam.proprioceptron.states.DirectionState;
import com.xeiam.proprioceptron.states.LengthState;

public class DirectionActuator implements Actuator {

  AngleState angles;
  LengthState lengths;
  DirectionState directions;

  public void setDomain(AngleState angles, LengthState lengths) {

    this.angles = angles;
    this.lengths = lengths;
  }

  public void setRange(DirectionState directions) {

    this.directions = directions;
  }

  @Override
  public void actuate() {

    for (int i = 0; i < angles.vars.length; i++) {
      directions.vars[i].setDimensional(Vector.fromPolar(lengths.vars[i].getVar(), angles.vars[i].getVar()));
    }

  }

}