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
package com.xeiam.proprioceptron.roboticarm;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;


public class AIPlayerState extends AbstractAppState implements PlayerAppState {
  int numJoints;
  private final Queue<List<JointCommand>> commands;
  RoboticArm app;
  List<JointCommand> currentCommand;

  public AIPlayerState(int numJoints) {

    commands = new LinkedList<List<JointCommand>>();
    this.numJoints = numJoints;
  }

  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (RoboticArm) app;

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (keyPressed) {
      System.out.println("It is the computers turn");
    }

  }

  @Override
  public void update(float tpf) {

    boolean wasMovement = false;
    if (currentCommand != null) {
    for (JointCommand jointCommand : currentCommand) {

      if (FastMath.abs(jointCommand.distance) > tpf * RoboticArmConstants.SPEED) {
        app.pivots[jointCommand.getJointNumber()].rotate(0f, jointCommand.getDirection() * tpf * RoboticArmConstants.SPEED, 0f);
        jointCommand.distance -= tpf * RoboticArmConstants.SPEED;
        wasMovement = true;

      }
    }
    }
 else
      app.notifyListeners();
    if (!wasMovement) {
      app.movementOver = !app.isWaiting;
      app.isWaiting = app.isWaiting || app.movementOver;
      if (commands.peek() != null) {
        currentCommand = commands.poll();
        app.isWaiting = false;
      }
    }
  }



  /**
   * Adds a list of PlayerCommand to the controller, to be executed in FIFO order. Currently unused, and not logically supported very well.
   * 
   * @param commands
   */
  public void pushCommands(List<JointCommand> commands) {

    this.commands.add(commands);
  }
}
