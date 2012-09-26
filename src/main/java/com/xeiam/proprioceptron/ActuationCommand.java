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
package com.xeiam.proprioceptron;

import com.xeiam.proprioceptron.actuators.Actuator;
import com.xeiam.proprioceptron.states.State;

/**
 * @author timmolter
 * @create Sep 20, 2012
 * @immutable
 */
public class ActuationCommand implements Actuator {

  // private final String servoId;
  final State[] toActuate;
  final int[][] indices;
  final FreeVar[][] values;


  // seems ram expensive
  /**
   * Constructor- takes a list of states to update on, which elements of these states to update on, and what values to give them.
   * 
   * @param toActuate
   * @param indices
   * @param values
   */
  public ActuationCommand(State[] toActuate, int[][] indices, FreeVar[][] values) {

    this.toActuate = toActuate;
    this.indices = indices;
    this.values = values;

  }
  @Override
  public void actuate() {

    for (int i = 0; i < toActuate.length; i++) {

      for (int j = 0; j < toActuate.length; j++) {
        toActuate[i].getVars()[indices[i][j]].plusequals(values[i][j]);
      }
    }
  }

  // public String getServoId() {
  //
  // return servoId;
  // }


  // @Override
  // public String toString() {
  //
  // return "ActuationCommand [servoId=" + servoId + ", position=" + angle + "]";

}
