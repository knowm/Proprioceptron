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
package org.knowm.proprioceptron.roboticarm;

import com.xeiam.proprioceptron.roboticarm.RoboticArm;

/**
 * Run the Robotic Arm game with only the Classifier.
 * 
 * @author alexnugent
 */
public class RoboticArmRandomApp extends AbstractRoboticArmApp {

  private static final int NUM_JOINTS = 4;
  private static final int START_LEVEL_ID = 0;
  private static final int NUM_TARGETS_PER_LEVEL = 50;

  private static final int NUM_FIBERS_PER_MUSCLE = 100;
  private static final int BUFFER_LENGTH = 1;

  private final RoboticArmBrainRandom roboticArmBrainRandom;

  /**
   * Constructor
   */
  public RoboticArmRandomApp() {

    roboticArmBrainRandom = new RoboticArmBrainRandom(NUM_JOINTS, BUFFER_LENGTH, NUM_FIBERS_PER_MUSCLE);

    roboticArm = new RoboticArm(NUM_JOINTS, START_LEVEL_ID, NUM_TARGETS_PER_LEVEL);

    init(NUM_JOINTS);

    roboticArm.start();
  }

  public static void main(String[] args) {

    new RoboticArmRandomApp();
  }

  @Override
  protected AbstractRoboticArmBrain getRoboticArmBrain() {

    return roboticArmBrainRandom;
  }
}