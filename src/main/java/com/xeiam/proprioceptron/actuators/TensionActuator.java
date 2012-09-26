package com.xeiam.proprioceptron.actuators;

import com.xeiam.proprioceptron.Vector;
import com.xeiam.proprioceptron.states.DensityState;
import com.xeiam.proprioceptron.states.DirectionState;
import com.xeiam.proprioceptron.states.LengthState;
import com.xeiam.proprioceptron.states.TensionState;
import com.xeiam.proprioceptron.states.TorqueState;

/**
 * takes the centripetal forces calulated in CentrifugalForceActuator and propogates them through rigid bodies producing instantaneous torques and further tensions.
 * 
 * @author Zackkenyon
 * @create Sep 18, 2012
 */
public class TensionActuator implements Actuator {

  // to see how this works, imagine a full cycle, with a torque applied to the middle joint of a simple double pendulum.
  // we must verify that momenta are conserved in the limit.
  // the inertia of the rod will not produce a resistance for a single time step. in the next time step, the rod's angular momentum
  // will induce a tension along the rod to the joint, this will be projected onto and orthogonally to the rod to determine tension
  // along the propagating rod and torque at the joint.
  TensionState tensions;
  DirectionState directions;
  DensityState densities;
  LengthState lengths;

  TorqueState torques;

  public void setDomain(TensionState tensions, DirectionState directions, DensityState densities, LengthState lengths) {

    this.tensions = tensions;
    this.directions = directions;
    this.densities = densities;
  }

  public void setRange(TorqueState torques) {

    this.torques = torques;
  }

  @Override
  public void actuate() {

    for (int i = tensions.vars.length - 1; i > 0; i--) {
      Vector temp = Vector.project((Vector) tensions.vars[i].getDimensional(), (Vector) directions.vars[i].getDimensional());
      ((Vector) tensions.vars[i - 1].getDimensional()).plusequals(temp);
      temp = Vector.minus((Vector) tensions.vars[i].getDimensional(), temp);
      torques.vars[i].setVar(Vector.magnitude(temp) * Vector.orientation(temp, (Vector) directions.vars[i].getDimensional()));
    }
  }
}