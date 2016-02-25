/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://manc.com) and contributors.
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

/**
 * Defines a Joint's movement
 * 
 * @author timmolter
 * @create Sep 28, 2012
 */
public final class JointCommand {

  private final int jointNumber;
  private final int direction;
  private final int steps;

  /**
   * Constructor
   * 
   * @param jointNumber
   * @param direction
   * @param steps
   */
  public JointCommand(int jointNumber, int direction, int steps) {

    this.jointNumber = jointNumber;
    this.direction = direction;
    this.steps = steps;
  }

  /**
   * @return the jointNumber
   */
  public int getJointNumber() {

    return jointNumber;
  }

  /**
   * @return the direction
   */
  public int getDirection() {

    return direction;
  }

  /**
   * @return the steps
   */
  public int getSteps() {

    return steps;
  }

}
