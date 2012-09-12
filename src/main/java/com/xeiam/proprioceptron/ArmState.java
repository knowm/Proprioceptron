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
 * @author zackkenyon
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

  }


  @Override
  public FreeVar[] toVector() {

    return vector;
  }

  @Override
  public State fromVector(FreeVar[] vec) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] vectorDoc() {

    // TODO Auto-generated method stub
    return null;
  }
  public static ArmState connect(ArmState x, JointState y) {

    // TODO Auto-generated method stub
    return null;
  }

  public static ArmState connect(JointState x, JointState y) {

    // TODO Auto-generated method stub
    return null;
  }

  /**
   * these are really angular impulses, but timing has not yet been specified
   * 
   * @author Zackkenyon
   * @create Sep 11, 2012
   */
  class TorqueState implements State {

    FreeVar[] torques;
    String[] doc;

    public TorqueState(FreeVar[] torques) {

      this.torques = torques;
      doc = new String[] { torques.length + " torques" }; // + id
    }

    @Override
    public FreeVar[] toVector() {

      return torques;
    }

    @Override
    public String[] vectorDoc() {

      return doc;
    }

    @Override
    public State fromVector(FreeVar[] torques) {

      this.torques = torques;
      this.doc = doc;
      return this;
    }
  }

  class TensionState implements State {

    FreeVar[] tensions;
    String[] doc;

    public TensionState(FreeVar[] tensions) {

      this.tensions = tensions;
      doc = new String[] { tensions.length + " tension" }; // + id
    }

    @Override
    public FreeVar[] toVector() {

      return tensions;
    }

    @Override
    public String[] vectorDoc() {

      return doc;
    }

    @Override
    public State fromVector(FreeVar[] tensions) {

      this.tensions = tensions;
      this.doc = doc;
      return this;
    }
  }

  class PositionState implements State {

    // TODO rewrite in new posx, posy format.
    Vector[] vectorpositions;
    FreeVar[] unpackedpositions;
    String[] doc;

    public PositionState(FreeVar[] positions) {

      this.unpackedpositions = positions;
      doc = new String[] { positions.length / 2 + "positions" }; // + id
    }

    @Override
    public FreeVar[] toVector() {

      return unpackedpositions;
    }

    @Override
    public String[] vectorDoc() {

      return doc;
    }

    @Override
    public State fromVector(FreeVar[] positions) {

      unpackedpositions = positions;
      return this;
    }
  }

  class AngularVelocityState implements State {

    FreeVar[] angularvelocities;

    public AngularVelocityState(FreeVar[] angularvelocities) {

      this.angularvelocities = angularvelocities;

    }

    @Override
    public FreeVar[] toVector() {

      return angularvelocities;
    }

    @Override
    public String[] vectorDoc() {

      return new String[] { "angular velocities" }; // + id
    }

    @Override
    public State fromVector(FreeVar[] angularvelocities) {

      this.angularvelocities = angularvelocities;
      return this;
    }
  }

  class AngleState implements State {

    FreeVar[] angles;

    float[] maxangles;

    public AngleState(FreeVar[] angles) {

      this.angles = angles;
    }

    public void initialize() {

      maxangles = new float[angles.length];

    }
    @Override
    public String[] vectorDoc() {

      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public FreeVar[] toVector() {

      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public State fromVector(FreeVar[] vars) {

      // TODO Auto-generated method stub
      return null;
    }

  }

  class EnergyState implements State {

    // TODO make fields.
    public EnergyState() {

      // TODO write constructor.
    }

    @Override
    public String[] vectorDoc() {

      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public FreeVar[] toVector() {

      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public State fromVector(FreeVar[] vars) {

      // TODO Auto-generated method stub
      return null;
    }

  }

}
