package com.xeiam.proprioceptron.actuators;

import com.xeiam.proprioceptron.Vector;
import com.xeiam.proprioceptron.states.AngularVelocityState;
import com.xeiam.proprioceptron.states.DensityState;
import com.xeiam.proprioceptron.states.DirectionState;
import com.xeiam.proprioceptron.states.LengthState;
import com.xeiam.proprioceptron.states.PositionState;
import com.xeiam.proprioceptron.states.TensionState;

public class CentrifugalForceActuator implements Actuator {

  PositionState positions;
  LengthState lengths;
  DensityState densities;
  AngularVelocityState angularvels;
  DirectionState directions;
  // codomain vars
  TensionState tensions;

  public void setDomain(PositionState positions, LengthState lengths, DensityState densities, AngularVelocityState angularvels, DirectionState directions) {

    this.positions = positions;
    this.lengths = lengths;
    this.densities = densities;
    this.angularvels = angularvels;
    this.directions = directions;

  }

  public void setRange(TensionState tensions) {

    this.tensions = tensions;
  }

  @Override
  public void actuate() {

    for (int i = 0; i < lengths.vars.length; i++) {
      tensions.vars[i].setDimensional(Vector.scale(angularvels.vars[i].getVar() * angularvels.vars[i].getVar() / lengths.vars[i].getVar(), (Vector) directions.vars[i].getDimensional()));
    }
  }
}