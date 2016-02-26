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
package org.knowm.proprioceptron.roboticarm.arm1;

import java.util.ArrayList;
import java.util.List;

import org.knowm.proprioceptron.roboticarm.AbstractRoboticArmAppState;
import org.knowm.proprioceptron.roboticarm.AbstractRoboticArmJMEApp;

/**
 * The Main entry point class
 *
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmJMEApp1 extends AbstractRoboticArmJMEApp {

  /**
   * Constructor
   *
   * @param numJoints
   * @param startLevelId
   * @param numTargetsPerLevel
   */
  public RoboticArmJMEApp1(int numJoints, int startLevelId, int numTargetsPerLevel) {
    super(numJoints, startLevelId, numTargetsPerLevel);
  }

  @Override
  public List<AbstractRoboticArmAppState> initLevels() {

    List<AbstractRoboticArmAppState> levels = new ArrayList<AbstractRoboticArmAppState>();
    levels.add(new RoboticArmLevelAppState1(this, 0, numJoints, 0.0f));
    levels.add(new RoboticArmLevelAppState1(this, 1, numJoints, 6.0f));
    // levels.add(new RoboticArmLevelAppState(this, 2, numJoints, 9.0f));
    // levels.add(new RoboticArmLevelAppState(this, 3, numJoints, 12.0f));
    // levels.add(new RoboticArmLevelAppState(this, 4, numJoints, 15.0f));
    return levels;
  }

}
