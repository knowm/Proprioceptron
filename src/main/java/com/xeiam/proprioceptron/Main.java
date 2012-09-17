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

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Zackkenyon
 * @create Aug 21, 2012
 */
public class Main {

  public static void main(String[] args) {

    JFrame frame = new JFrame("test params");
    Mypanel panel = new Mypanel();
    ArmState arm;
    AngularVelocityActuator avactuator = new AngularVelocityActuator();
    PositionActuator pactuator = new PositionActuator();
    ArrayList<JointState> joints = new ArrayList<JointState>();
    joints.add(new JointState(5.0, 1.0, null));
    joints.add(new JointState(5.0, 1.0, joints.get(0)));
    joints.add(new JointState(5.0, 1.0, joints.get(1)));
    joints.add(new JointState(5.0, 1.0, joints.get(2)));
    joints.add(new JointState(5.0, 1.0, joints.get(3)));
    for (int i = 0; i < joints.size(); i++) {
      joints.get(i).initialize(i * .000001 + .000001, 0.0);
    }
    arm = new ArmState(joints);
    arm.initialize();
    pactuator.setDomain(arm.angles, arm.lengths);
    avactuator.setDomain(arm.angularvels);
    pactuator.setRange(arm.posxs, arm.posys);
    avactuator.setRange(arm.angles);

    panel.setDrawList(arm.posxs, arm.posys);
    frame.add(panel);
    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setVisible(true);

    while (true) {
      avactuator.actuate();
      pactuator.actuate();
      frame.repaint();
    }
  }

}

class Mypanel extends JPanel {

  private static final long serialVersionUID = 1L;
  PosXState drawx;
  PosYState drawy;

  public void setDrawList(PosXState xs, PosYState ys) {

    drawx = xs;
    drawy = ys;

  }

  @Override
  public void paint(Graphics g) {

    g.drawLine(300, 300, (int) (10 * drawx.posxs[0].var) + 300, (int) (10 * drawy.posys[0].var) + 300);
    for (int i = 0; i < drawx.posxs.length - 1; i++) {// draw the rods
      g.drawLine((int) (10 * drawx.posxs[i].var) + 300, (int) (10 * drawy.posys[i].var) + 300, (int) (10 * drawx.posxs[i + 1].var) + 300, (int) (10 * drawy.posys[i + 1].var) + 300);
    }
    for (int i = 0; i < drawx.posxs.length; i++) {
      g.fillOval((int) (10 * drawx.posxs[i].var) + 295, (int) (10 * drawy.posys[i].var) + 295, 10, 10);
    }
  }
}