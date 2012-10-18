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

import com.jme3.system.AppSettings;

/**
 * Run the Robotic Arm game with human input via keyboard keys PLOKIJ.
 * 
 * @author timmolter
 * @create Sep 28, 2012
 */
public class HumanBrainRoboticArmApp {

  private static final int NUM_JOINTS = 3;// max supported is 8.

  private final RoboticArm roboticArm;

  /**
   * Constructor
   */
  public HumanBrainRoboticArmApp() {

    roboticArm = new RoboticArm(NUM_JOINTS, new HumanPlayerState(NUM_JOINTS));
    roboticArm.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1300, 700);
    settings.setTitle("Proprioceptron - Human Input");
    roboticArm.setSettings(settings);
    roboticArm.start();

  }

  public static void main(String[] args) {

    @SuppressWarnings("unused")
    HumanBrainRoboticArmApp hb = new HumanBrainRoboticArmApp();
  }

}
