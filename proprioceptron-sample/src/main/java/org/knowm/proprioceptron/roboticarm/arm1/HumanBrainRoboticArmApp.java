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

import com.jme3.system.AppSettings;

/**
 * Run the Robotic Arm game with human input via keyboard keys PLOKIJUHYGTFRDESWA.
 *
 * @author timmolter
 * @create Sep 28, 2012
 */
public class HumanBrainRoboticArmApp {

  private static final int NUM_JOINTS = 2;
  private static final int START_LEVEL_ID = 1;
  private static final int NUM_TARGETS_PER_LEVEL = 2;

  private final RoboticArmJMEApp1 roboticArm;

  /**
   * Constructor
   */
  public HumanBrainRoboticArmApp() {

    roboticArm = new RoboticArmJMEApp1(NUM_JOINTS, START_LEVEL_ID, NUM_TARGETS_PER_LEVEL);
    roboticArm.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(480, 480);
    settings.setTitle("Proprioceptron - Human Input");
    settings.setFrameRate(60);
    roboticArm.setSettings(settings);
    roboticArm.start();

  }

  public static void main(String[] args) {

    new HumanBrainRoboticArmApp();
  }

}
