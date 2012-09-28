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
import java.beans.PropertyChangeListener;

import com.jme3.system.AppSettings;

/**
 * Run the Robotic Arm game with a simple test brain completing the feedback loop
 * 
 * @author timmolter
 * @create Sep 28, 2012
 */
public class SimpleTestBrain implements PropertyChangeListener {

  private static final int NUM_JOINTS = 2;

  private RoboticArm roboticArm;

  /**
   * Constructor
   */
  public SimpleTestBrain() {

    roboticArm = new RoboticArm(NUM_JOINTS);
    roboticArm.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(480, 480);
    settings.setTitle("Proprioceptron - Simple Test Brain");
    roboticArm.setSettings(settings);
    roboticArm.setEnableKeys(true);
    roboticArm.addChangeListener(this);
    roboticArm.start();

  }

  public static void main(String[] args) {

    SimpleTestBrain stm = new SimpleTestBrain();
  }

  @Override
  public void propertyChange(PropertyChangeEvent arg0) {

    System.out.println(arg0.getOldValue() + " : " + arg0.getNewValue().toString());

  }
  // 1. Fire up Game Environment

  // 2. Plugin Brain

}
