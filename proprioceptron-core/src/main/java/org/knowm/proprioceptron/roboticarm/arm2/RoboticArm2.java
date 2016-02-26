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

import java.util.ArrayList;
import java.util.List;

import org.knowm.proprioceptron.roboticarm.AbstractRoboticArm;
import org.knowm.proprioceptron.roboticarm.RoboticArmLevelAppState;

/**
 * The Main entry point class
 *
 * @author timmolter
 */
public class RoboticArm2 extends AbstractRoboticArm {
  /**
   * Constructor
   *
   * @param numJoints
   * @param startLevelId
   * @param numTargetsPerLevel
   */
  public RoboticArm2(int numJoints, int startLevelId, int numTargetsPerLevel) {
    super(numJoints, startLevelId, numTargetsPerLevel);
  }

  @Override
  public List<RoboticArmLevelAppState> initLevels() {

    List<RoboticArmLevelAppState> levels = new ArrayList<RoboticArmLevelAppState>();
    levels.add(new RoboticArmLevelAppState(this, 0, numJoints, 0.0f));
    levels.add(new RoboticArmLevelAppState(this, 1, numJoints, 1.0f));
    return levels;
  }

}
