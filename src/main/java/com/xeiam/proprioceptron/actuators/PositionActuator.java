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

    positionstate.vars[0].setDimensional(Vector.fromPolar(lengths.vars[0].getVar(), angles.vars[0].getVar()));

    for (int i = 1; i < angles.vars.length; i++) {
      // not great for garbage collector. also quite lengthy. is a quick fix for a painting concurrency problem I was having.
      positionstate.vars[i].setDimensional(Vector.plus((Vector) positionstate.vars[i - 1].getDimensional(), Vector.fromPolar(lengths.vars[i].getVar(), angles.vars[i].getVar())));
    }

  }

}
