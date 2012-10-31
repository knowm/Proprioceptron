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
package com.xeiam.proprioceptron.thematrix;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

/**
 * A class of delegate methods for handling translation of human inputs into physical changes in the game environment.
 * 
 * @author Zackkenyon
 * @create Oct 12, 2012
 */
public class HumanAppState extends AbstractAppState implements PlayerAppState {

  /**
   * internal state variable. records information from onAction.
   */
  private boolean isGoingForward = false;
  /**
   * internal state variable. records information from onAction.
   */
  private boolean isGoingBackward = false;
  /**
   * internal state variable. records information from onAction.
   */
  private boolean isTurningLeft = false;
  /**
   * internal state variable. records information from onAction.
   */
  private boolean isTurningRight = false;

  private TheMatrix app;


  /**
   * adds the state to the StateManager, casts the Application to a TheMatrix
   */
  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (TheMatrix) app;
  }

  /**
   * resets the nowWaiting condition to false, which allows the movementOver condition to be triggered in update.
   * 
   * @param name the name of the trigger which called this
   * @param keyPressed whether the trigger was called by pressing or releasing the button
   * @param tpf time per frame (update) in seconds, expressed as a float
   */
  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    app.nowWaiting = false;
    if (name.equals("forward")) {
      // forward direction
      isGoingForward = keyPressed;
    } else if (name.equals("backward")) {
      // backward direction
      isGoingBackward = keyPressed;
    } else if (name.equals("turnright")) {
      isTurningRight = keyPressed;
    } else if (name.equals("turnleft")) {
      isTurningLeft = keyPressed;
    }
  }

  /**
   * checks to see if the movement has ended, then tells the app to move the character in every direction specified by the boolean is*ing* fields
   * 
   * @param tpf time per frame (update) in seconds, expressed as a float.
   */
  @Override
  public void update(float tpf) {

    app.movementOver = !(app.nowWaiting || isGoingForward || isGoingBackward || isTurningLeft || isTurningRight);
    app.nowWaiting = app.nowWaiting || app.movementOver;
    if (isGoingForward && !isGoingBackward)
      app.forwardEpsilon(5 * tpf);
    else if (!isGoingForward && isGoingBackward)
      app.forwardEpsilon(-5 * tpf);
    if (isTurningLeft && !isTurningRight)
      app.rotateEpsilon(tpf);
    else if (!isTurningLeft && isTurningRight)
      app.rotateEpsilon(-tpf);
  }
}
