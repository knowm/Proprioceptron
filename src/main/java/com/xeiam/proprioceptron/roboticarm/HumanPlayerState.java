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

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;


public class HumanPlayerState extends AbstractAppState implements PlayerAppState {

  int keysPressed;// keeps track of the number of active keys currently pressed. When reaches zero, players move is over.
  Command[] playerCommands;
  private RoboticArm app;

  public HumanPlayerState(int numJoints) {

    playerCommands = new Command[numJoints];
  }

  @Override
  public void initialize(AppStateManager asm, Application app) {

    keysPressed = 0;
    super.initialize(asm, app);
    this.app = (RoboticArm) app;
    for (int i = 0; i < playerCommands.length; i++) {
      playerCommands[i] = Command.ZERO;
    }
    setUpKeys();
  }

  public void setUpKeys(){

    int[] NaturalKeyOrder = new int[] {
 KeyInput.KEY_F, KeyInput.KEY_J, KeyInput.KEY_D, KeyInput.KEY_K, KeyInput.KEY_S, KeyInput.KEY_L, KeyInput.KEY_A, KeyInput.KEY_SEMICOLON, KeyInput.KEY_R, KeyInput.KEY_U,
        KeyInput.KEY_E, KeyInput.KEY_I, KeyInput.KEY_W, KeyInput.KEY_O, KeyInput.KEY_Q, KeyInput.KEY_P };
    for (int i = 0; i < NaturalKeyOrder.length && i < playerCommands.length * 2; i++) {
      app.getInputManager().addMapping((i % 2 == 1 ? "L" : "R") + (playerCommands.length - 1 - (i / 2)), new KeyTrigger(NaturalKeyOrder[i]));
      app.getInputManager().addListener(app, (i % 2 == 1 ? "L" : "R") + (playerCommands.length - 1 - (i / 2)));
    }
  }
  @Override
  /**
   * current bug, can only operate two joints in one direction at once.
   */
  public void onAction(String name, boolean keyPressed, float tpf) {

    app.isWaiting = false;
    keysPressed += (keyPressed ? 1 : -1);
    int index;
    Command c;
    String intparse;
    if (name.startsWith("L"))
      c = (keyPressed ? Command.LEFT : Command.RIGHT);
    else
      c = (keyPressed ? Command.RIGHT : Command.LEFT);
    intparse = name.substring(1);
    index = Integer.parseInt(intparse);
    playerCommands[index] = playerCommands[index].add(c);
  }

  @Override
  public void update(float tpf) {


    for (int i = 0; i < playerCommands.length; i++) {
      if (playerCommands[i] != Command.ZERO) {
        app.pivots[i].rotate(0, (playerCommands[i] == Command.LEFT ? -tpf : tpf) * RoboticArmConstants.SPEED, 0);
      }
    }
    app.movementOver = (keysPressed == 0) && !app.isWaiting;
    app.isWaiting = app.isWaiting || app.movementOver;
  }

}
