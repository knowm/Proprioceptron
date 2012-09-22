package com.xeiam.proprioceptron.actuators;

import com.xeiam.proprioceptron.Vector;
import com.xeiam.proprioceptron.states.AngleState;
import com.xeiam.proprioceptron.states.AngularVelocityState;
import com.xeiam.proprioceptron.states.DensityState;
import com.xeiam.proprioceptron.states.DirectionState;
import com.xeiam.proprioceptron.states.LengthState;
import com.xeiam.proprioceptron.states.TensionState;
import com.xeiam.proprioceptron.states.TorqueState;

public class TorqueActuator implements Actuator {

  TorqueState torques;
  LengthState lengths;
  DensityState densities;
  DirectionState directions;
  AngleState angles;
  AngularVelocityState angularvelocities;
  TensionState tensions;

  public void setDomain(TorqueState torques, LengthState lengths, DensityState densities, DirectionState directions, AngleState angles) {

    this.torques = torques;
    this.lengths = lengths;
    this.densities = densities;
    this.directions = directions;
    this.angles = angles;
  }

  public void setRange(AngularVelocityState angularvelocities, TensionState tensions) {

    this.angularvelocities = angularvelocities;
    this.tensions = tensions;
  }

  @Override
  public void actuate() {

    for (int i = 0; i < lengths.lengths.length; i++) {
      // first we add the torque to the angular momentum
      angularvelocities.angularvelocities[i].setVar(angularvelocities.angularvelocities[i].getVar() + torques.torques[i].getVar() / lengths.lengths[i].getVar() / densities.densities[i].getVar() / 2.0);
      // then we construct a new tension vector to oppose the torque.
      ((Vector) tensions.tensions[i].getDimensional()).plusequals(Vector.project(Vector.fromPolar(torques.torques[i].getVar(), angles.angles[i].getVar()), (Vector) directions.directions[i].getDimensional()));
      // note that this does not give us a closed form for angular velocity at time t, I have to do some more math to see if there is one at all, but this converges pretty quickly,
    }
  }
}
