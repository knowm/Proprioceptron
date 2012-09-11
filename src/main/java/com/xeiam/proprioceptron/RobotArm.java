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
 * @create Aug 21, 2012
 */
public class RobotArm {

  // pseudocode
  /*
   * apply restoring forces. starting at tail. if greatest length difference greater than some threshold then repeat apply impulses -conserve momentum -apply changes to head and tail repeat
   */

  ArrayList<Joint> arm;
  public RobotArm() {// example

    arm = new ArrayList<Joint>();
    arm.add(new Joint(Vector.Zero()));
    arm.add(new Joint(new Vector(1, 0)));
    arm.add(new Joint(new Vector(3, 0)));
    arm.add(new Joint(new Vector(4, 0)));
    arm.add(new Joint(new Vector(8, 0)));

  }

  public void initialize() {

    for (int i = 0; i < arm.size() - 1; i++) {
      arm.get(i).setout(arm.get(i + 1));
      arm.get(i + 1).setin(arm.get(i));
    }
    for (int i = 0; i < arm.size() - 1; i++) {
      arm.get(i).setangle();
    }
  }

  public void updatearm(int[] spikes) {

    for (int i = 0; i < spikes.length; i++) {
      arm.get(i).omegadot = spikes[i] / 10000000.0f;
      arm.get(i).accelerate();
      arm.get(i).move();
    }
  }

  public ArrayList<Vector> print() {

    ArrayList<Vector> myVectors = new ArrayList<Vector>();
    for (int i = 0; i < arm.size(); i++) {
      myVectors.add(arm.get(i).getposition());
      // this method should only need to be called once, since this is
      // a reference
    }

    return myVectors;
  }
}
