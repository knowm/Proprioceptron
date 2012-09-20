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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.xeiam.proprioceptron.ActuationSequence;
import com.xeiam.proprioceptron.state.Arm;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public class RoboticArmPanel extends JPanel implements Runnable {

  private Thread animatorThread;
  private static final int FRAME_RATE = 30;

  /** Arm Variables **/
  private Arm arm;

  /** Animation Variables **/
  private int counter = 0;
  private AnimationSequence animationSequence;

  /**
   * Constructor
   */
  public RoboticArmPanel(Arm arm, ActuationSequence actuationSequence) {

    setPreferredSize(new Dimension(500, 500));

    this.arm = arm;
    this.animationSequence = new AnimationSequence(arm, actuationSequence);

  }

  @Override
  public void paint(Graphics g) {

    super.paint(g);

    if (counter >= animationSequence.getArmStateSnapshots().size()) {
      counter = 0;
    }

    ArmStateSnapshot armStateSnapshot = animationSequence.getArmStateSnapshots().get(counter++);
    armStateSnapshot.paint((Graphics2D) g);

    Toolkit.getDefaultToolkit().sync();
    g.dispose();
  }

  @Override
  public void run() {

    long beforeTime, timeDiff, sleep;

    beforeTime = System.currentTimeMillis();

    while (true) {

      repaint();

      timeDiff = System.currentTimeMillis() - beforeTime;
      sleep = FRAME_RATE - timeDiff;

      if (sleep < 0)
        sleep = 2;
      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        System.out.println("interrupted");
      }

      beforeTime = System.currentTimeMillis();
    }
  }

  @Override
  public void addNotify() {

    super.addNotify();
    animatorThread = new Thread(this);
    animatorThread.start();
  }

}
