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

import com.xeiam.proprioceptron.RobotArm;
import com.xeiam.proprioceptron.Vector;

/**
 * @author Zackkenyon
 * @create Aug 21, 2012
 */
public class Main {

  public static void main(String[] args) {

    JFrame frame = new JFrame("test params");
    Mypanel panel = new Mypanel();

    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setVisible(true);
    RobotArm myarm = new RobotArm();
    myarm.initialize();
    int[] somespikes = { -1, -3, -1, -7, 0 };
    int[] wait = { 0, 0, 0, 0, 0 };
    myarm.updateRoboticArm(somespikes);
    panel.setDrawList(myarm.getDrawList());
    while (true) {
      panel.setDrawList(myarm.getDrawList());
      myarm.updateRoboticArm(wait);
      frame.repaint();
    }
  }
}

class Mypanel extends JPanel {

  ArrayList<Vector> toDraw;

  public void setDrawList(ArrayList<Vector> toDraw) {

    this.toDraw = toDraw;
  }

  @Override
  public void paint(Graphics g) {

    for (int i = 0; i < toDraw.size() - 1; i++) {// draw the rods
      g.drawLine((int) (50 * toDraw.get(i).x) + 300, (int) (50 * toDraw.get(i).y) + 300, (int) (50 * toDraw.get(i + 1).x) + 300, (int) (50 * toDraw.get(i + 1).y) + 300);
    }
    for (int i = 0; i < toDraw.size(); i++) {
      g.fillOval((int) (50 * toDraw.get(i).x) + 295, (int) (50 * toDraw.get(i).y) + 295, 10, 10);
    }
  }
}