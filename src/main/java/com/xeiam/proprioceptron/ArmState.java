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

import java.util.ArrayList;

/**
 * @author Zackkenyon
 * @create Sep 11, 2012 ArmState combines and extracts physically relevant combinations of free variables for actuators
 */

public class ArmState implements State {

  public String[] documentation;
  public FreeVar[] vector;
  public TorqueState torques;
  public TensionState tensions;
  public AngleState angles;
  public PositionState positions;
  public AngularVelocityState angularvels;
  public EnergyState energy;
  public ArrayList<JointState> joints;
  /**
   * Constructor
   * 
   * @param arm
   */
  public ArmState(ArrayList<JointState> joints) {

    this.joints = joints;
    torques = new TorqueState();
    tensions = new TensionState();
    angles = new AngleState();
    positions = new PositionState();
    angularvels = new AngularVelocityState();
    energy = new EnergyState();

  }

  public void initialize() {

    FreeVar[] t0 = new FreeVar[joints.size()];
    FreeVar[] t1 = new FreeVar[joints.size()];
    FreeVar[] t2 = new FreeVar[joints.size()];
    FreeVar[] t3 = new FreeVar[joints.size()];
    FreeVar[] t4 = new FreeVar[joints.size()];
    FreeVar[] t5 = new FreeVar[joints.size()];
    FreeVar[] t6 = new FreeVar[joints.size()];

    for (int i = 0; i < joints.size(); i++) {
      t0[i] = joints.get(i).angle;
      t1[i] = joints.get(i).angularvelocity;
    }
    angles.addVars(t0);
    angularvels.addVars(t1);

  }

  @Override
  public FreeVar[] toVector() {

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

  // public static ArmState connect(ArmState x, JointState y) {
  //
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // public static ArmState connect(JointState x, JointState y) {
  //
  // // TODO Auto-generated method stub
  // return null;
  // }

  /**
   * these are really angular impulses, but timing has not yet been specified
   * 
   * @author Zackkenyon
   * @create Sep 11, 2012
   */
  class TorqueState implements State {

    FreeVar[] torques;


    @Override
    public FreeVar[] toVector() {

      return torques;
    }

    @Override
    public String[] vectorDoc() {

      return new String[] { torques.length + " torques" }; // + id
    }

    @Override
    public void addVars(FreeVar[] torques) {

      this.torques = torques;

    }
  }

  class TensionState implements State {

    FreeVar[] tensions;
    String[] doc;


    @Override
    public FreeVar[] toVector() {

      return tensions;
    }

    @Override
    public String[] vectorDoc() {

      return doc;
    }

    @Override
    public void addVars(FreeVar[] tensions) {

      this.tensions = tensions;

    }
  }

  class PositionState implements State {

    // TODO rewrite in new posx, posy format.
    Vector[] vectorpositions;
    FreeVar[] unpackedpositions;
    String[] doc;


    @Override
    public FreeVar[] toVector() {

      return unpackedpositions;
    }

    @Override
    public String[] vectorDoc() {

      return doc;
    }

    @Override
    public void addVars(FreeVar[] positions) {

      unpackedpositions = positions;

    }
  }

  class AngularVelocityState implements State {

    FreeVar[] angularvelocities;


    @Override
    public FreeVar[] toVector() {

      return angularvelocities;
    }

    @Override
    public String[] vectorDoc() {

      return new String[] { "angular velocities" }; // + id
    }

    @Override
    public void addVars(FreeVar[] angularvelocities) {

      this.angularvelocities = angularvelocities;

    }
  }

  class AngleState implements State {

    // the angle will be regularly updated as the angle from the last joint. if it points in the same direction as the last joint,
    // then the angle is 0, positive is counterclockwise, negative angles are clockwise.
    FreeVar[] angles;

    float maxangle; // collision detection will be either difficult to implement or computationally expensive without these fields.
    float[] maxangles;


    public void initialize() {

      maxangles = new float[angles.length];
      maxangles[0] = 360f;
      for (int i = 1; i < angles.length; i++)
        maxangles[i] = 170f;
      maxangle = 360;
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

  class EnergyState implements State {

    // this is in anticipation of giving the the learning algorithm a much finer solution. namely one in which
    // energy usage is minimized.
    FreeVar[] energy;


    @Override
    public String[] vectorDoc() {

      return new String[] { "energy * " + energy.length };
    }

    @Override
    public FreeVar[] toVector() {

      return energy;
    }

    @Override
    public void addVars(FreeVar[] vars) {

      energy = vars;

    }

  }

}
