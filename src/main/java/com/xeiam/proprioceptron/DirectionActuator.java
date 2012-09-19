package com.xeiam.proprioceptron;

class DirectionActuator implements Actuator{

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

    for (int i = 0; i < angles.angles.length; i++) {
      directions.directions[i].setDimensional(Vector.fromPolar(lengths.lengths[i].getVar(), angles.angles[i].getVar()));
    }

  }
  
}