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
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.xeiam.proprioceptron.GameState;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArm extends SimpleApplication {

  protected final int numJoints;
  private final int numTargetsPerLevel;

  /** prevents calculation of state when there are no arm movements */
  protected boolean wasMovement = false;

  BitmapText hudText;

  /** Levels */
  private List<RoboticArmLevelAppState> levels;
  private RoboticArmLevelAppState currentLevelAppState;
  private int currentLevelIndex = 0;

  /** Listeners **/
  protected final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

  /** GameState */
  public GameState oldEnvState;
  public GameState newEnvState;

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
    levels.add(new RoboticArmLevelAppState(this, numJoints, 1, 0, false));
    levels.add(new RoboticArmLevelAppState(this, numJoints, 1, 0, true));
    currentLevelAppState = levels.get(currentLevelIndex);
    stateManager.attach(currentLevelAppState);

    /** Load the HUD */
    BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24);
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(10, hudText.getLineHeight(), 0);
    hudText.setText(getHUDText());
    getGuiNode().attachChild(hudText);

    // init env state
    simpleUpdate(0.0f);
  }

  @Override
  public void simpleUpdate(float tpf) {

    if (wasMovement) {

      wasMovement = false;

      // update old state
      oldEnvState = newEnvState;

      // env state - arm positions
      EnvState roboticArmEnvState = currentLevelAppState.getEnvState();

      if (roboticArmEnvState.wasCollision()) {
        currentLevelAppState.score.incNumCollisions();
        if (currentLevelAppState.score.getNumCollisions() % numTargetsPerLevel == 0) {
          currentLevelAppState.score = new Score();
          currentLevelIndex++;
          hudText.setText(getHUDText());

          if (currentLevelIndex >= levels.size()) { // game over
            hudText.setText("GAME OVER");
            stateManager.detach(currentLevelAppState);
          } else {
            stateManager.detach(currentLevelAppState);
            currentLevelAppState = levels.get(currentLevelIndex);
            stateManager.attach(currentLevelAppState);
          }
        } else {
          currentLevelAppState.moveTarget();
          hudText.setText(getHUDText());
        }
      }

      newEnvState = new RoboticArmGameState(roboticArmEnvState, currentLevelAppState.score);

      notifyListeners();
    }
  }

  private String getHUDText() {

    StringBuilder sb = new StringBuilder();
    sb.append("Level = ");
    sb.append(currentLevelIndex);
    sb.append("/");
    sb.append(levels.size());

    sb.append(", Blue Pills = ");
    sb.append(currentLevelAppState.score.getNumCollisions());
    sb.append("/");
    sb.append(numTargetsPerLevel);

    sb.append(", Score = ");
    sb.append(currentLevelAppState.score.getScore());

    return sb.toString();
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

}
