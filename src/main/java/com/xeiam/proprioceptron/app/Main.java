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
package com.xeiam.proprioceptron.app;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.xeiam.proprioceptron.FreeVar;
import com.xeiam.proprioceptron.actuator.AngularVelocityActuator;
import com.xeiam.proprioceptron.actuator.CentrifugalForceActuator;
import com.xeiam.proprioceptron.actuator.DirectionActuator;
import com.xeiam.proprioceptron.actuator.PositionActuator;
import com.xeiam.proprioceptron.actuator.TensionActuator;
import com.xeiam.proprioceptron.actuator.TorqueActuator;
import com.xeiam.proprioceptron.state.Arm;
import com.xeiam.proprioceptron.state.Joint;

/**
 * @author Zackkenyon
 * @create Aug 21, 2012
 */
public class Main {

  public static void main(String[] args) {

    JFrame frame = new JFrame("test params");
    Mypanel panel = new Mypanel();
    Arm arm;
    AngularVelocityActuator avactuator = new AngularVelocityActuator();
    PositionActuator pactuator = new PositionActuator();
    CentrifugalForceActuator cfactuator = new CentrifugalForceActuator();
    DirectionActuator dactuator = new DirectionActuator();
    TensionActuator tnactuator = new TensionActuator();
    TorqueActuator tqactuator = new TorqueActuator();
    ArrayList<Joint> joints = new ArrayList<Joint>();
    joints.add(new Joint(5.0, 1.0, null));
    joints.add(new Joint(5.0, 1.0, joints.get(0)));
    joints.add(new Joint(5.0, 1.0, joints.get(1)));
    joints.add(new Joint(5.0, 1.0, joints.get(2)));
    joints.add(new Joint(5.0, 1.0, joints.get(3)));
    for (int i = 0; i < joints.size(); i++) {
      joints.get(i).initialize(i * .000001 + .000001, 0.0);
    }
    arm = new Arm(joints);
    arm.initialize();

    pactuator.setDomain(arm.angles, arm.lengths);
    avactuator.setDomain(arm.angularvels);
    cfactuator.setDomain(arm.positions, arm.lengths, arm.densities, arm.angularvels, arm.directions);
    dactuator.setDomain(arm.angles, arm.lengths);
    tnactuator.setDomain(arm.tensions, arm.directions, arm.densities, arm.lengths);
    tqactuator.setDomain(arm.torques, arm.lengths, arm.densities, arm.directions, arm.angles);
    pactuator.setRange(arm.positions);
    avactuator.setRange(arm.angles);
    cfactuator.setRange(arm.tensions);
    dactuator.setRange(arm.directions);
    tnactuator.setRange(arm.torques);
    tqactuator.setRange(arm.angularvels, arm.tensions);

    panel.setDrawList(arm.positions.positions);
    frame.add(panel);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setVisible(true);

    while (true) {
      //

      avactuator.actuate();
      pactuator.actuate();
      dactuator.actuate();
      cfactuator.actuate();

      tnactuator.actuate();
      tqactuator.actuate();

      frame.repaint();

    }
  }
}

class Mypanel extends JPanel {

  private static final long serialVersionUID = 1L;
  FreeVar[] drawlist;

  public void setDrawList(FreeVar[] todraw) {

    drawlist = todraw.clone();
  }

  @Override
  public void paint(Graphics g) {

    g.drawLine(300, 300, (int) (10 * drawlist[0].getDimensional().toArray()[0]) + 300, (int) (10 * drawlist[0].getDimensional().toArray()[1]) + 300);
    for (int i = 0; i < drawlist.length - 1; i++) { // draw the rods
      g.drawLine((int) (10 * drawlist[i].getDimensional().toArray()[0]) + 300, (int) (10 * drawlist[i].getDimensional().toArray()[1]) + 300, (int) (10 * drawlist[i + 1].getDimensional().toArray()[0]) + 300,
          (int) (10 * drawlist[i + 1].getDimensional().toArray()[1]) + 300);
    }
    for (int i = 0; i < drawlist.length; i++) {
      g.fillOval((int) (10 * drawlist[i].getDimensional().toArray()[0]) + 295, (int) (10 * drawlist[i].getDimensional().toArray()[1]) + 295, 10, 10);
    }
  }
}