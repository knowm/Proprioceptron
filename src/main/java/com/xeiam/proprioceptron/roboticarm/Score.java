/**
 * Copyright 2012 Xeiam LLC.
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

import java.util.Arrays;

/**
 * The Score for a level
 * 
 * @author timmolter
 * @create Nov 1, 2012
 */
public class Score {

  private int[] pillIDs;
  private int pillIdCounter = 0;

  private float[] timesElapsed;
  private float lastTime;

  private double[] activationEnergiesRequired;
  private int actuationEnergy = 0;

  /**
   * Constructor
   */
  public Score(int numTargetsPerLevel) {

    pillIDs = new int[numTargetsPerLevel];
    timesElapsed = new float[numTargetsPerLevel];
    activationEnergiesRequired = new double[numTargetsPerLevel];

  }

  /**
   * @param time
   */
  public void initTime(float time) {

    lastTime = time;
  }

  /**
   * Increment the number of collisions with blue pills by one
   */
  public void incNumBluePills(float time) {

    pillIDs[pillIdCounter] = pillIdCounter;
    timesElapsed[pillIdCounter] = time - lastTime;
    activationEnergiesRequired[pillIdCounter] = actuationEnergy;

    // reset everything
    pillIdCounter++;
    lastTime = time;
    actuationEnergy = 0;

    System.out.println(this.toString());
  }

  /**
   * @param energy
   */
  public void incActuationEnergy(int energy) {

    actuationEnergy += energy;
  }

  /**
   * @return
   */
  public double getScore() {

    if (pillIdCounter > 0) {

      int sumActivationEnergy = 0;
      for (int i = 0; i < activationEnergiesRequired.length; i++) {
        if (activationEnergiesRequired[i] > 0) {
          sumActivationEnergy += activationEnergiesRequired[i];
        } else {
          break;
        }
      }
      return (double) sumActivationEnergy / pillIdCounter;
    } else {
      return 0.0;
    }
  }

  public int getNumBluePills() {

    return pillIdCounter;
  }

  @Override
  public String toString() {

    String returnValue = Arrays.toString(pillIDs) + ";" + Arrays.toString(timesElapsed) + ";" + Arrays.toString(activationEnergiesRequired);
    return returnValue.replaceAll("\\[", " ").replaceAll("\\]", " ");
  }
}
