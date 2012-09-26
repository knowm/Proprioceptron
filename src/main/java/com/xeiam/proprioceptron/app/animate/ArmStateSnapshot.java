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
import java.awt.geom.Line2D;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public final class ArmStateSnapshot {

  // Dummy variables
  private final int x1Position;
  private final int x2Position;
  private final int y1Position;
  private final int y2Position;

  /**
   * Constructor
   * 
   * @param x1Position
   * @param y1Position
   * @param x2Position
   * @param y2Position
   */
  public ArmStateSnapshot(int x1Position, int y1Position, int x2Position, int y2Position) {

    this.x1Position = x1Position;
    this.y1Position = y1Position;
    this.x2Position = x2Position;
    this.y2Position = y2Position;
  }

  /**
   * @param g
   */
  public void paint(Graphics2D g2d) {

    // TODO Make this method actually paint the Arm

    // Dummy paint
    // Ellipse2D nodeBackgroundTail = new Ellipse2D.Double(x1Position - 5.5, y1Position - 5.5, 11, 11);
    Ellipse2D nodeBackgroundHead = new Ellipse2D.Double(x2Position - 5.5, y2Position - 5.5, 11, 11);
    Line2D segment = new Line2D.Double(x1Position, y1Position, x2Position, y2Position);
    // g2d.fill(nodeBackgroundTail);
    g2d.fill(nodeBackgroundHead);
    g2d.draw(segment);

  }

}
