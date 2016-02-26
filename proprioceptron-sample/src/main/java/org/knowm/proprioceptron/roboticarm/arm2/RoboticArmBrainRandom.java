/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://alexnugentconsulting.com/) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.proprioceptron.roboticarm.arm2;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.knowm.proprioceptron.roboticarm.AbstractRoboticArmBrain;
import org.knowm.proprioceptron.roboticarm.JointCommand;
import org.knowm.proprioceptron.roboticarm.RoboticArmGameState;

/**
 * @author timmolter
 */
public class RoboticArmBrainRandom extends AbstractRoboticArmBrain {

  private final Random random = new Random();

  /**
   * Constructor
   *
   * @param numJoints
   */
  public RoboticArmBrainRandom(int numJoints, int bufferLength, int numFibersPerMuscle) {

  }

  @Override
  public List<JointCommand> update(PropertyChangeEvent pce) {

    // RoboticArmGameState oldEnvState = (RoboticArmGameState) pce.getOldValue();
    RoboticArmGameState newEnvState = (RoboticArmGameState) pce.getNewValue();

    List<JointCommand> jointCommands = new ArrayList<JointCommand>();

    // simulate a pause
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int numJoints = newEnvState.getEnvState().getRelativePositions().length;
    for (int i = 0; i < numJoints; i++) {
      jointCommands.add(new JointCommand(i, random.nextDouble() > 0.5 ? 1 : -1, random.nextInt(100)));
    }

    return jointCommands;

  }

}
