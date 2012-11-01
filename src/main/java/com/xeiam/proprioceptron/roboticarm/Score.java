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

/**
 * The Score for a level
 * 
 * @author timmolter
 * @create Nov 1, 2012
 */
public class Score {

  private int numBluePills = 0;
  private int numRedPills = 0;
  private int actuationEnergy = 0;

  /**
   * Increment the number of collisions with blue pills by one
   */
  public void incNumBluePills() {

    numBluePills++;
  }

  /**
   * Increment the number of collisions with red pills by one
   */
  public void incNumRedPills() {

    numRedPills++;
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

    if (numBluePills > 0) {
      return (double) actuationEnergy / numBluePills;
    } else {
      return 0.0;
    }
  }

  public int getNumBluePills() {

    return numBluePills;
  }

  public int getNumRedPills() {

    return numRedPills;
  }

}
