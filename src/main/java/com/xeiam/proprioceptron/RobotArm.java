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
import java.util.List;

/**
 * @author zackkenyon
 * @create Aug 21, 2012
 */
public class RobotArm {

  // pseudocode
  /*
   * apply restoring forces. starting at tail. if greatest length difference greater than some threshold then repeat apply impulses -conserve momentum -apply changes to head and tail repeat
   */

  List<JointState> joints;

  /**
   * Constructor
   */
  public RobotArm() { // example

    joints = new ArrayList<JointState>();
    joints.add(new JointState(Vector.Zero()));
    joints.add(new JointState(new Vector(1, 0)));
    joints.add(new JointState(new Vector(3, 0)));
    joints.add(new JointState(new Vector(4, 0)));
    joints.add(new JointState(new Vector(8, 0)));

  }

  /**
   * javadoc
   */
  public void initialize() {

    for (int i = 0; i < joints.size() - 1; i++) {
      joints.get(i).setout(joints.get(i + 1));
      joints.get(i + 1).setin(joints.get(i));
    }
    for (int i = 0; i < joints.size() - 1; i++) {
      joints.get(i).setangle();
    }
  }

  public void updateRoboticArm(int[] spikes) {

    for (int i = 0; i < spikes.length; i++) {
      joints.get(i).omegadot = spikes[i] / 10000000.0f;
      joints.get(i).accelerate();
      joints.get(i).move();
    }
  }

  public ArrayList<Vector> getDrawList() {

    ArrayList<Vector> myVectors = new ArrayList<Vector>();
    for (int i = 0; i < joints.size(); i++) {
      myVectors.add(joints.get(i).getposition());
      // this method should only need to be called once, since this is
      // a reference
    }

    return myVectors;
  }
}
