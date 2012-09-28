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
package com.xeiam.proprioceptron.roboticarm;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author timmolter
 * @create Sep 28, 2012
 */
public class SimpleBrain {

  private Random random = new Random();

  /**
   * @param pce
   */
  public List<JointCommand> update(PropertyChangeEvent pce) {

    EnvState oldEnvState = (EnvState) pce.getOldValue();
    EnvState newEnvState = (EnvState) pce.getNewValue();

    List<JointCommand> jointCommands = new ArrayList<JointCommand>();

    // simulate a pause
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int numJoints = newEnvState.getRelativePositions().length;
    for (int i = 0; i < numJoints; i++) {
      jointCommands.add(new JointCommand(i, random.nextDouble() > 0.5 ? 1 : -1, random.nextInt(50)));
    }

    return jointCommands;

  }

}
