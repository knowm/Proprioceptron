/**
 * Copyright 2012 MANC LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.proprioceptron.states;

import java.util.List;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.actuators.AngularVelocityActuator;
import com.xeiam.proprioceptron.actuators.CentrifugalForceActuator;
import com.xeiam.proprioceptron.actuators.DirectionActuator;
import com.xeiam.proprioceptron.actuators.PositionActuator;
import com.xeiam.proprioceptron.actuators.TensionActuator;
import com.xeiam.proprioceptron.actuators.TorqueActuator;

/**
 * ArmState combines and extracts physically relevant combinations of free variables for actuators
 * 
 * @author Zackkenyon
 * @create Sep 11, 2012
 */
public class Arm implements State {

  public List<Joint> joints;
  public FreeVar[] vector;
  public AngleState angles;
  public AngularVelocityState angularvels;
  public TorqueState torques;
  public PositionState positions;
  public TensionState tensions;
  public EnergyState energy;
  public DistanceFromGoalState distancefromgoal;
  public LengthState lengths;
  public DirectionState directions;
  public DensityState densities;

  public PositionActuator pactuator = new PositionActuator();
  public AngularVelocityActuator avactuator = new AngularVelocityActuator();
  public CentrifugalForceActuator cfactuator = new CentrifugalForceActuator();
  public DirectionActuator dactuator = new DirectionActuator();
  public TensionActuator tnactuator = new TensionActuator();
  public TorqueActuator tqactuator = new TorqueActuator();

  /**
   * Constructor
   * 
   * @param arm
   */
  public Arm(List<Joint> joints) {

    this.joints = joints;
    torques = new TorqueState();
    tensions = new TensionState();
    angles = new AngleState();
    positions = new PositionState();
    distancefromgoal = new DistanceFromGoalState();
    angularvels = new AngularVelocityState();
    energy = new EnergyState();
    lengths = new LengthState();
    densities = new DensityState();
    directions = new DirectionState();

  }

  public void initialize() {

    FreeVar[] t0 = new FreeVar[joints.size()];
    FreeVar[] t1 = new FreeVar[joints.size()];
    FreeVar[] t2 = new FreeVar[joints.size()];
    FreeVar[] t3 = new FreeVar[joints.size()];
    FreeVar[] t4 = new FreeVar[joints.size()];
    FreeVar[] t5 = new FreeVar[joints.size()];
    FreeVar[] t6 = new FreeVar[joints.size()];
    FreeVar[] t7 = new FreeVar[joints.size()];
    FreeVar[] t8 = new FreeVar[joints.size()];
    FreeVar[] t9 = new FreeVar[joints.size()];

    for (int i = 0; i < joints.size(); i++) {
      t0[i] = joints.get(i).angle;
      t1[i] = joints.get(i).angularvelocity;
      t2[i] = joints.get(i).torque;
      t3[i] = joints.get(i).position;
      t4[i] = joints.get(i).tension;
      t5[i] = joints.get(i).energy;
      t6[i] = joints.get(i).distance;
      t7[i] = joints.get(i).length;
      t8[i] = joints.get(i).density;
      t9[i] = joints.get(i).direction;

    }
    angles.addVars(t0);
    angularvels.addVars(t1);
    torques.addVars(t2);
    positions.addVars(t3);
    tensions.addVars(t4);
    energy.addVars(t5);
    distancefromgoal.addVars(t6);
    lengths.addVars(t7);
    densities.addVars(t8);
    directions.addVars(t9);

    pactuator.setDomain(angles, lengths);
    avactuator.setDomain(angularvels);
    cfactuator.setDomain(positions, lengths, densities, angularvels, directions);
    dactuator.setDomain(angles, lengths);
    tnactuator.setDomain(tensions, directions, densities, lengths);
    tqactuator.setDomain(torques, lengths, densities, directions, angles);
    pactuator.setRange(positions);
    avactuator.setRange(angles);
    cfactuator.setRange(tensions);
    dactuator.setRange(directions);
    tnactuator.setRange(torques);
    tqactuator.setRange(angularvels, tensions);

  }

  public void update() {

    avactuator.actuate();
    pactuator.actuate();
    dactuator.actuate();
    cfactuator.actuate();
    tnactuator.actuate();
    tqactuator.actuate();

  }

  @Override
  public FreeVar[] toVector() {

    // doesn't do anything right now
    return vector;
  }

  @Override
  public void addVars(FreeVar[] vec) {

    // TODO Auto-generated method stub

  }

  @Override
  public String[] vectorDoc() {

    // TODO Auto-generated method stub
    return null;
  }

}
