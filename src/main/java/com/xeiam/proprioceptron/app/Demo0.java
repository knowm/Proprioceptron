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
package com.xeiam.proprioceptron.app;

import java.util.ArrayList;
import java.util.List;

import com.xeiam.proprioceptron.ActuationCommand;
import com.xeiam.proprioceptron.ActuationSequence;
import com.xeiam.proprioceptron.app.animate.RoboticArmFrame;
import com.xeiam.proprioceptron.app.animate.RoboticArmPanel;
import com.xeiam.proprioceptron.state.Joint;
import com.xeiam.proprioceptron.states.Arm;

/**
 * Demo0 shows a predetermined (hard-coded) sequence of movements of an arm with a single joint. It should look like a fly swatter hitting a surface and springing back up into neutral position
 * 
 * @author timmolter
 * @create Sep 20, 2012
 */
public class Demo0 {

  /**
   * @param args
   */
  public static void main(String[] args) {

    // 1. Create Arm
    List<Joint> joints = new ArrayList<Joint>();
    joints.add(new Joint(5.0, 1.0, null));

    // TODO hide this initialization inside Joint or Arm
    for (int i = 0; i < joints.size(); i++) {
      joints.get(i).initialize(i * .000001 + .000001, 0.0);
    }
    Arm arm = new Arm(joints);
    arm.initialize();

    // 2. create a actuation sequence
    ActuationSequence actuationSequence = new ActuationSequence();
    actuationSequence.addActuationCommand(new ActuationCommand("0", 0));
    actuationSequence.addActuationCommand(new ActuationCommand("0", 45));
    actuationSequence.addActuationCommand(new ActuationCommand("0", 90));
    actuationSequence.addActuationCommand(new ActuationCommand("0", 45));
    actuationSequence.addActuationCommand(new ActuationCommand("0", 0));
    actuationSequence.addActuationCommand(new ActuationCommand("0", -45));
    actuationSequence.addActuationCommand(new ActuationCommand("0", -90));
    actuationSequence.addActuationCommand(new ActuationCommand("0", -45));

    new RoboticArmFrame(new RoboticArmPanel(arm, actuationSequence));

  }

}
