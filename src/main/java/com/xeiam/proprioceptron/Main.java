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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Zackkenyon
 * @create Aug 21, 2012
 */
public class Main {

  public static void main(String[] args) {

    JFrame frame = new JFrame("test params");

    List<JointState> jointStates = new ArrayList<JointState>();
    jointStates.add(new JointState(5.0, 1.0, null));
    jointStates.add(new JointState(5.0, 1.0, jointStates.get(0)));
    jointStates.add(new JointState(5.0, 1.0, jointStates.get(1)));
    jointStates.add(new JointState(5.0, 1.0, jointStates.get(2)));
    jointStates.add(new JointState(5.0, 1.0, jointStates.get(3)));
    for (int i = 0; i < jointStates.size(); i++) {
      jointStates.get(i).initialize(i * .000001f, 0f);
    }
    ArmState armState = new ArmState(jointStates);
    armState.initialize();

    PositionActuator positionActuator = new PositionActuator();
    positionActuator.setDomain(armState.angles, armState.lengths);
    positionActuator.setRange(armState.posxs, armState.posys);

    AngularVelocityActuator angularVelocityActuator = new AngularVelocityActuator();
    angularVelocityActuator.setDomain(armState.angularvels);
    angularVelocityActuator.setRange(armState.angles);

    Mypanel panel = new Mypanel();
    panel.setDrawList(armState.posxs, armState.posys);
    frame.add(panel);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setVisible(true);

    while (true) {
      angularVelocityActuator.actuate();
      positionActuator.actuate();
      frame.repaint();
    }
  }

}

class Mypanel extends JPanel {

  PosXState drawx;
  PosYState drawy;

  public void setDrawList(PosXState xs, PosYState ys) {

    drawx = xs;
    drawy = ys;
  }

  @Override
  public void paint(Graphics g) {

    g.drawLine(300, 300, (int) (10 * drawx.posxs[0].var) + 300, (int) (10 * drawy.posys[0].var) + 300);
    for (int i = 0; i < drawx.posxs.length - 1; i++) { // draw the rods
      g.drawLine((int) (10 * drawx.posxs[i].var) + 300, (int) (10 * drawy.posys[i].var) + 300, (int) (10 * drawx.posxs[i + 1].var) + 300, (int) (10 * drawy.posys[i + 1].var) + 300);
    }
    for (int i = 0; i < drawx.posxs.length; i++) { // draw the joints
      g.fillOval((int) (10 * drawx.posxs[i].var) + 295, (int) (10 * drawy.posys[i].var) + 295, 10, 10);
    }

  }

}