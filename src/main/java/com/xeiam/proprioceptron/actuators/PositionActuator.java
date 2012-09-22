package com.xeiam.proprioceptron.actuators;

import com.xeiam.proprioceptron.Vector;
import com.xeiam.proprioceptron.states.AngleState;
import com.xeiam.proprioceptron.states.LengthState;
import com.xeiam.proprioceptron.states.PositionState;

/**
 * Takes the angles and lengths of the rods and retrieves positions
 * 
 * @author Zackkenyon
 * @create Sep 18, 2012
 */
public class PositionActuator implements Actuator {

  PositionState positionstate;
  AngleState angles;
  LengthState lengths;

  public void setDomain(AngleState angles, LengthState lengths) {

    this.angles = angles;
    this.lengths = lengths;
  }

  public void setRange(PositionState positionstate) {

    this.positionstate = positionstate;
  }

  @Override
  public void actuate() {

    positionstate.positions[0].setDimensional(Vector.fromPolar(lengths.lengths[0].getVar(), angles.angles[0].getVar()));

    for (int i = 1; i < angles.angles.length; i++) {
      // not great for garbage collector. also quite lengthy. is a quick fix for a painting concurrency problem I was having.
      positionstate.positions[i].setDimensional(Vector.plus((Vector) positionstate.positions[i - 1].getDimensional(), Vector.fromPolar(lengths.lengths[i].getVar(), angles.angles[i].getVar())));
    }

  }

}
