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

/**
 * Demo1 shows a predetermined (hard-coded) sequence of movements of an arm with a single joint. It should "random-walk" left or right in increments of the minimum torque.
 * 
 * @author Zackkenyon
 * @create Sep 20, 2012
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.xeiam.proprioceptron.ActuationCommand;
import com.xeiam.proprioceptron.ActuationHandler;
import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.VarType;
import com.xeiam.proprioceptron.app.animate.Camera;
import com.xeiam.proprioceptron.app.animate.RoboticArmFrame;
import com.xeiam.proprioceptron.app.animate.RoboticArmPanel;
import com.xeiam.proprioceptron.states.Arm;
import com.xeiam.proprioceptron.states.Joint;
import com.xeiam.proprioceptron.states.State;

public class Demo1 {

  /**
   * @param args
   */
  public static void main(String[] args) {

    // 1. Create Arm
    List<Joint> joints = new ArrayList<Joint>();
    joints.add(new Joint(5.0, 1.0, null));

    // TODO hide this initialization inside Joint or Arm
    for (int i = 0; i < joints.size(); i++) {
      joints.get(i).initialize(0.0, 0.0);
    }
    Arm arm = new Arm(joints);
    arm.initialize();

    // 2. create a actuation sequence and logic objects needed for actuation sequence
    ActuationHandler handler = new ActuationHandler();
    Random rand = new Random();
    ActuationCommand clockwiseaccelerate = new ActuationCommand(new State[] { arm.angularvels }, new int[][] { { 0 } }, new FreeVar[][] { { new FreeVar(1e-9, VarType.ANGULARVELOCITY) } });
    ActuationCommand counterclockwiseaccelerate = new ActuationCommand(new State[] { arm.angularvels }, new int[][] { { 0 } }, new FreeVar[][] { { new FreeVar(-1e-9, VarType.ANGULARVELOCITY) } });
    handler.addActuator(arm.avactuator, false);
    handler.addActuator(arm.pactuator, true);

    // 3. Create a camera
    Camera armCamera = new Camera(arm);

    new RoboticArmFrame(new RoboticArmPanel(armCamera));

    while (true) {
      if (rand.nextInt(1000) == 5) {
        handler.addActuationCommand(clockwiseaccelerate);
      }
      if (rand.nextInt(1000) == 5) {
        handler.addActuationCommand(counterclockwiseaccelerate);
      }
      handler.getNextActuator().actuate();
    }

  }

}

