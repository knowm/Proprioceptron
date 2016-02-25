/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://alexnugentconsulting.com/) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.proprioceptron.thematrix;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;

/**
 * AIAppState is a class of methods for handling the very different inputs that the computer gives versus the human player. It is a delegate for currentPlayer in TheMatrix.java.
 * 
 * @author Zackkenyon
 * @create Oct 12, 2012
 */
public class AIAppState extends AbstractAppState implements PlayerAppState {

  /**
   * The list of yet to be completed commands. As of current version, expects to only have one command in it at a time.
   */
  Queue<PlayerCommand> commands;
  /**
   * local state variable, is decremented during rotation, is reassigned when zero.
   */
  float toBeRotated = 0;
  /**
   * local state variable, is decremented during translation, is reassigned when zero.
   */
  float toBeMoved = 0;
  TheMatrix app;

  /**
   * Constructor
   */
  public AIAppState() {

    super();
    commands = new LinkedList<PlayerCommand>();
  }

  /**
   * adds the app to the AppStateManager, casts the app as a TheMatrix object, and tells the AI to update it's perception state.
   */
  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (TheMatrix) app;
    this.app.notifyListeners();
  }

  /**
   * rotate the character if it still needs rotating, if not, move the character forward if it still needs translating, otherwise, end the movement and get a new command from the AI.
   * 
   * @param tpf time per frame- normalizes the motion in the viewgraph so that the velocity appears constant.
   */
  @Override
  public void update(float tpf) {

    if (FastMath.abs(toBeRotated) > tpf) {
      app.movementOver = false;
      app.nowWaiting = false;
      toBeRotated -= AIrotatestep(toBeRotated, tpf);
    } else if (FastMath.abs(toBeMoved) > 5 * tpf) {
      toBeMoved -= AIforwardstep(5 * toBeMoved, tpf);
    } else {
      app.movementOver = !app.nowWaiting;
      app.nowWaiting = app.movementOver || app.nowWaiting;
      if (commands.peek() != null) {
        toBeRotated = commands.peek().getRotation();
        toBeMoved = commands.poll().getForwardMotion();
      }
    }
  }

  /** for internal use */
  private float AIrotatestep(float rotation, float tpf) {

    app.rotateEpsilon(tpf * Math.signum(rotation));
    return (tpf * Math.signum(rotation));
  }

  /** for internal use */
  private float AIforwardstep(float distance, float tpf) {

    // should not use walk direction for this, since cannot determine the number of steps taken.
    app.forwardEpsilon(tpf * Math.signum(distance));
    return tpf * Math.signum(distance);
  }

  /**
   * Adds a list of PlayerCommand to the controller, to be executed in FIFO order. Currently unused, and not logically supported very well.
   * 
   * @param commands
   */
  public void pushCommand(List<PlayerCommand> commands) {

    this.commands.addAll(commands);
  }

  /**
   * Adds a PlayerCommand to the controller. Is called by the method in TheMatrix addAICommand(PlayerCommand command), which just rescopes it.
   * 
   * @param command
   */
  public void pushCommand(PlayerCommand command) {

    commands.add(command);
  }

  /**
   * Currently does nothing, may be used with customized triggers in the future to handle AI inputs.
   * 
   * @param name the name of the trigger which called this
   * @param keyPressed whether the trigger was called by pushing or releasing the button
   * @param tpf time per frame (update) in seconds, expressed as a float
   */
  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (keyPressed) {
      System.out.println("it's the computer's turn right now.");
    }
  }
}
