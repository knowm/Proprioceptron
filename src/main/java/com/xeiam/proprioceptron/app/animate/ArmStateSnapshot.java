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
import java.awt.geom.Ellipse2D;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public final class ArmStateSnapshot {

  // Dummy variables
  private final int xPosition;
  private final int yPosition;

  /**
   * Constructor
   * 
   * @param xPosition
   * @param yPosition
   */
  public ArmStateSnapshot(int xPosition, int yPosition) {

    this.xPosition = xPosition;
    this.yPosition = yPosition;
  }

  /**
   * @param g
   */
  public void paint(Graphics2D g2d) {

    // TODO Make this method actually paint the Arm

    // Dummy paint
    Ellipse2D nodeBackground = new Ellipse2D.Double(xPosition, yPosition, 11, 11);
    g2d.fill(nodeBackground);

  }

}
