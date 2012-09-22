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
package com.xeiam.proprioceptron.app.animate;

import java.util.ArrayList;
import java.util.List;

import com.xeiam.proprioceptron.ActuationCommand;
import com.xeiam.proprioceptron.ActuationSequence;
import com.xeiam.proprioceptron.states.Arm;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public class AnimationSequence {

  /** the list of interpolated ArmStateSnapshots to be rendered during an animation */
  private List<ArmStateSnapshot> armStateSnapshots;

  /**
   * Constructor
   * 
   * @param arm
   * @param actuationSequence
   */
  public AnimationSequence(Arm arm, ActuationSequence actuationSequence) {

    armStateSnapshots = new ArrayList<ArmStateSnapshot>();

    ActuationCommand previousActuationCommand = actuationSequence.getActuationCommands().get(0);

    for (int i = 1; i < actuationSequence.getActuationCommands().size(); i++) {

      ActuationCommand actuationCommand = actuationSequence.getActuationCommands().get(i);

      int numberOfInBetweenFrames = 10; // TODO intelligently set this, base it on distance from actuationCommand_I and actuationCommand_I+1

      for (int j = 0; j < numberOfInBetweenFrames; j++) {

        // TODO given the arm and the actuationCommand, create the in-between steps

        // Dummy interpolation example
        double angle = (previousActuationCommand.getAngle() + (actuationCommand.getAngle() - previousActuationCommand.getAngle()) * (j + 1) / numberOfInBetweenFrames) * 2.0 * Math.PI / 360.0;
        int xPosition = 125 + (int) (Math.sin(angle) * 50);
        int yPosition = 125 + (int) (Math.cos(angle) * 50);
        ArmStateSnapshot armStateSnapshot = new ArmStateSnapshot(xPosition, yPosition);
        armStateSnapshots.add(armStateSnapshot);
      }

      previousActuationCommand = actuationCommand;
    }
  }

  /**
   * @return the armStateSnapshots
   */
  public List<ArmStateSnapshot> getArmStateSnapshots() {

    return armStateSnapshots;
  }

}
