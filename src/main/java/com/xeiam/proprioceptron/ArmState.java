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
package com.xeiam.proprioceptron;

import java.util.List;

/**
 * @author Zackkenyon
 * @create Sep 11, 2012 ArmState combines and extracts physically relevant combinations of free variables for actuators
 */

public class ArmState implements State {

  public String[] documentation;
  public FreeVar[] vector;
  public AngleState angles;
  public AngularVelocityState angularvels;
  public TorqueState torques;
  public PosXState posxs;
  public PosYState posys;
  public TensionState tensions;
  public EnergyState energy;
  public DistanceFromGoalState distancefromgoal;
  public LengthState lengths;
  public DensityState densities;
  public List<JointState> joints;

  /**
   * Constructor
   * 
   * @param arm
   */
  public ArmState(List<JointState> joints) {

    this.joints = joints;
    torques = new TorqueState();
    tensions = new TensionState();
    angles = new AngleState();
    posxs = new PosXState();
    posys = new PosYState();
    distancefromgoal = new DistanceFromGoalState();
    angularvels = new AngularVelocityState();
    energy = new EnergyState();
    lengths = new LengthState();
    densities = new DensityState();
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
      t3[i] = joints.get(i).posx;
      t4[i] = joints.get(i).posy;
      t5[i] = joints.get(i).tension;
      t6[i] = joints.get(i).energy;
      t7[i] = joints.get(i).distance;
      t8[i] = joints.get(i).length;
      t9[i] = joints.get(i).density;

    }
    angles.addVars(t0);
    angularvels.addVars(t1);
    torques.addVars(t2);
    posxs.addVars(t3);
    posys.addVars(t4);
    tensions.addVars(t5);
    energy.addVars(t6);
    distancefromgoal.addVars(t7);
    lengths.addVars(t8);
    densities.addVars(t9);

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
