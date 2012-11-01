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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.xeiam.proprioceptron.GameState;

/**
 * The Main entry point class
 * 
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArm extends SimpleApplication implements ActionListener {

  protected final int numJoints;
  protected final int numTargetsPerLevel;

  private boolean isRunning = false; // starts paused
  protected boolean gameOver = false;

  /** prevents calculation of state when there are no arm movements */
  protected boolean wasMovement = false;

  /** Levels */
  protected List<RoboticArmLevelAppState> levels;
  protected RoboticArmLevelAppState currentLevelAppState;
  protected int currentLevelIndex = 0;

  /** ScoreAppState */
  private ScoreAppState scoreAppState;

  /** Listeners **/
  protected final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

  /** GameState */
  public GameState oldEnvState;
  public GameState newEnvState;

  private Trigger pauseTrigger = new KeyTrigger(KeyInput.KEY_SPACE);

  /**
   * Constructor
   */
  public RoboticArm(int numJoints, int numTargetsPerLevel) {

    this.numJoints = numJoints;
    this.numTargetsPerLevel = numTargetsPerLevel;
  }

  @Override
  public void simpleInitApp() {

    // let app run in background
    setPauseOnLostFocus(false);

    // hide scene graph statistics
    setDisplayStatView(false);
    setDisplayFps(false);

    // Change Camera position
    flyCam.setEnabled(false);
    cam.setLocation(new Vector3f(0f, numJoints * 6f, 0f));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);

    // Levels
    levels = new ArrayList<RoboticArmLevelAppState>();
    levels.add(new RoboticArmLevelAppState(this, numJoints, false, true));
    levels.add(new RoboticArmLevelAppState(this, numJoints, false, true));
    currentLevelAppState = levels.get(currentLevelIndex);
    for (RoboticArmLevelAppState roboticArmLevelAppState : levels) {
      roboticArmLevelAppState.initialize(getStateManager(), this);
    }
    // currentLevelAppState.setEnabled(false);
    // stateManager.attach(currentLevelAppState);

    // ScoreAppState
    scoreAppState = new ScoreAppState(this);
    scoreAppState.initialize(getStateManager(), this);
    scoreAppState.setEnabled(true);
    // stateManager.attach(scoreAppState);

    // pause unpause
    inputManager.addMapping("Game Pause Unpause", pauseTrigger);
    inputManager.addListener(this, new String[] { "Game Pause Unpause" });

    // init env state
    currentLevelAppState.onAction("go!", false, 1f);
  }

  @Override
  public void simpleUpdate(float tpf) {

    if (wasMovement && isRunning && !gameOver) {

      wasMovement = false;

      // update old state
      oldEnvState = newEnvState;

      // env state - arm positions
      EnvState roboticArmEnvState = currentLevelAppState.getEnvState();

      if (roboticArmEnvState.wasCollision()) {
        currentLevelAppState.score.incNumBluePills();
        if (currentLevelAppState.score.getNumBluePills() % numTargetsPerLevel == 0) {
          currentLevelIndex++;
          currentLevelAppState.setHudText();

          if (currentLevelIndex >= levels.size()) { // game over
            currentLevelAppState.setEnabled(false);
            gameOver = true;
            scoreAppState.setEnabled(true);
          } else {
            currentLevelAppState.setEnabled(false);
            currentLevelAppState = levels.get(currentLevelIndex);
            currentLevelAppState.setEnabled(true);
          }
        } else {
          currentLevelAppState.placePills();
          currentLevelAppState.setHudText();
        }
      }

      currentLevelAppState.movePills(tpf);

      newEnvState = new RoboticArmGameState(roboticArmEnvState, currentLevelAppState.score);

      notifyListeners();
    }
  }

  public void addChangeListener(PropertyChangeListener newListener) {

    listeners.add(newListener);
  }

  /**
   * Send PropertyChangeEvent to observers (listeners)
   */
  public void notifyListeners() {

    PropertyChangeEvent pce = new PropertyChangeEvent(this, "", oldEnvState, newEnvState);

    for (Iterator<PropertyChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PropertyChangeListener observer = iterator.next();
      observer.propertyChange(pce);
    }
  }

  /**
   * This is where JointCommands come in from an AI algorithm
   * 
   * @param jointCommands
   */
  public void moveJoints(List<JointCommand> jointCommands) {

    currentLevelAppState.moveJoints(jointCommands, speed);
  }

  @Override
  public void onAction(String name, boolean isPressed, float tpf) {

    if (name.equals("Game Pause Unpause") && !isPressed) {

      if (!gameOver) {
        if (isRunning) {
          currentLevelAppState.setEnabled(false);
          scoreAppState.setEnabled(true);
        } else {
          scoreAppState.setEnabled(false);
          currentLevelAppState.setEnabled(true);
        }
        isRunning = !isRunning;
      }
    }
  }

}
