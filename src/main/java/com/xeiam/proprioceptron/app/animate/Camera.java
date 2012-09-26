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
package com.xeiam.proprioceptron.app.animate;

import java.awt.Graphics2D;

import com.xeiam.proprioceptron.states.Arm;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public class Camera {

  /** the list of interpolated ArmStateSnapshots to be rendered during an animation */
  private ArmStateSnapshot snapshot;
  protected boolean isneeded;
  private final Arm arm;
  /**
   * Constructor
   * 
   * @param arm
   * @param actuationSequence
   */
  public Camera(Arm arm) {

    this.arm = arm;
    validate();
  }

  public void paint(Graphics2D g2d) {

    snapshot.paint(g2d);
    validate();
  }
  public void validate() {

    isneeded = true;
  }

  public void invalidate() {

    isneeded = false;
  }

  public void takeSnapshot() {

    if (isneeded) {
      snapshot = new ArmStateSnapshot(300, 300, (int) (10 * arm.positions.vars[0].getDimensional().toArray()[0]) + 300, (int) (10 * arm.positions.vars[0].getDimensional().toArray()[1]) + 300);
      invalidate();
    }
  }

  /**
   * @return the armStateSnapshots
   */
  public ArmStateSnapshot getNextSnapshot() {

    return snapshot;
  }

}
