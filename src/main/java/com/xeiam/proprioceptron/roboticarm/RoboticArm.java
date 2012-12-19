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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.xeiam.proprioceptron.GameState;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SwingWrapper;

/**
 * The Main entry point class
 * 
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArm extends SimpleApplication implements ActionListener {

  protected final int numJoints;
  protected final int startLevelId;
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

  /** a queue for incoming joint movement commands */
  List<JointCommand> jointCommandsQueue = null;

  /**
   * Constructor
   * 
   * @param numJoints
   * @param startLevelId - zero-based
   * @param numTargetsPerLevel
   */
  public RoboticArm(int numJoints, int startLevelId, int numTargetsPerLevel) {

    this.numJoints = numJoints;
    this.startLevelId = startLevelId;
    currentLevelIndex = startLevelId;
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
    levels.add(new RoboticArmLevelAppState(this, numJoints, 0.0f));
    levels.add(new RoboticArmLevelAppState(this, numJoints, 6.0f));
    levels.add(new RoboticArmLevelAppState(this, numJoints, 9.0f));
    levels.add(new RoboticArmLevelAppState(this, numJoints, 12.0f));
    levels.add(new RoboticArmLevelAppState(this, numJoints, 15.0f));
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
    Trigger pauseTrigger = new KeyTrigger(KeyInput.KEY_SPACE);
    inputManager.addMapping("Game Pause Unpause", pauseTrigger);
    inputManager.addListener(this, new String[] { "Game Pause Unpause" });

    // init env state
    currentLevelAppState.score.initTime(timer.getTimeInSeconds());
    currentLevelAppState.onAction("go!", false, 1f);
  }

  @Override
  public void simpleUpdate(float tpf) {

    // move the joints
    if (jointCommandsQueue != null) {
      currentLevelAppState.moveJoints(jointCommandsQueue, tpf);
      jointCommandsQueue = null;
    }

    if (wasMovement && isRunning && !gameOver) {

      wasMovement = false;

      // update old state
      oldEnvState = newEnvState;

      // env state - arm positions
      EnvState roboticArmEnvState = currentLevelAppState.getEnvState();

      if (roboticArmEnvState.wasCollision()) {
        currentLevelAppState.score.incNumBluePills(timer.getTimeInSeconds());
        if (currentLevelAppState.score.getPillIdCounter() % 100 == 0) { // every 100 pills, reconstruct arm to prevent the joints drifting apart too far.
          currentLevelAppState.reconstructArm();
        }
        if (currentLevelAppState.score.getPillIdCounter() % numTargetsPerLevel == 0) { // new level
          currentLevelIndex++;

          if (currentLevelIndex >= levels.size()) { // game over
            currentLevelAppState.setEnabled(false);
            gameOver = true;
            scoreAppState.setEnabled(true);
            printScores();
            plotResults();
          } else {
            currentLevelAppState.setEnabled(false);
            currentLevelAppState = levels.get(currentLevelIndex);
            currentLevelAppState.setEnabled(true);
            currentLevelAppState.setHudText();
            currentLevelAppState.score.initTime(timer.getTimeInSeconds());
          }
        } else {
          currentLevelAppState.placePills();
          currentLevelAppState.setHudText();
        }
      }

      currentLevelAppState.movePills(tpf);

      newEnvState = new RoboticArmGameState(roboticArmEnvState, currentLevelAppState.score);

      // Notify the AI
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

    // add the joint command to be picked up by the simpleUpdate loop
    jointCommandsQueue = jointCommands;
    // currentLevelAppState.moveJoints(jointCommands, speed);
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

  /**
   * Prints the scores for each level
   */
  private void printScores() {

    for (RoboticArmLevelAppState roboticArmLevelAppState : levels) {
      System.out.println(roboticArmLevelAppState.score.toString());
    }
  }

  private void plotResults() {

    List<Chart> charts = new ArrayList<Chart>();

    Collection<Number> xData = new ArrayList<Number>();
    Collection<Number> yData = new ArrayList<Number>();

    int levelCounter = 0;
    for (RoboticArmLevelAppState roboticArmLevelAppState : levels) {
      for (int i = 0; i < roboticArmLevelAppState.score.getActivationEnergiesRequired().length; i++) {
        xData.add(levelCounter);
        yData.add(roboticArmLevelAppState.score.getActivationEnergiesRequired()[i]);
      }
      levelCounter++;
    }

    // Create Chart
    Chart chart = new Chart(800, 400);

    // Customize Chart
    chart.setTitle("Required Activation Energy");
    chart.setXAxisTitle("Level");
    chart.setYAxisTitle("Required Activation Energy");
    chart.setLegendVisible(false);

    // Series 1
    Series series1 = chart.addSeries("requiredActivateionEnergy", xData, yData);
    series1.setLineStyle(SeriesLineStyle.NONE);
    charts.add(chart);

    xData = new ArrayList<Number>();
    yData = new ArrayList<Number>();

    levelCounter = 0;
    for (RoboticArmLevelAppState roboticArmLevelAppState : levels) {
      for (int i = 0; i < roboticArmLevelAppState.score.getActivationEnergiesRequired().length; i++) {
        xData.add(levelCounter);
        yData.add(roboticArmLevelAppState.score.getTimesElapsed()[i]);
      }
      levelCounter++;
    }

    // Create Chart
    chart = new Chart(800, 400);

    // Customize Chart
    chart.setTitle("Elapsed Time");
    chart.setXAxisTitle("Level");
    chart.setYAxisTitle("Elapsed Time");
    chart.setLegendVisible(false);

    // Series 1
    series1 = chart.addSeries("elapsedTime", xData, yData);
    series1.setLineStyle(SeriesLineStyle.NONE);
    charts.add(chart);

    new SwingWrapper(charts, 2, 1).displayChartMatrix();

  }
}
