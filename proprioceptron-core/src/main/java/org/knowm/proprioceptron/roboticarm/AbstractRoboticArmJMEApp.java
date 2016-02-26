package org.knowm.proprioceptron.roboticarm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.knowm.proprioceptron.GameState;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;

/**
 * @author timmolter
 */
public abstract class AbstractRoboticArmJMEApp extends SimpleApplication implements ActionListener {

  public abstract List<AbstractRoboticArmAppState> initLevels();

  protected final int numJoints;
  protected final int startLevelId;
  protected final int numTargetsPerLevel;

  private boolean isRunning = false; // starts paused
  protected boolean gameOver = false;

  /** prevents calculation of state when there are no arm movements */
  protected boolean wasMovement = false;

  /** Levels */
  protected List<AbstractRoboticArmAppState> levels;
  protected AbstractRoboticArmAppState currentLevelAppState;
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
  public AbstractRoboticArmJMEApp(int numJoints, int startLevelId, int numTargetsPerLevel) {

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
    this.levels = initLevels();
    currentLevelAppState = levels.get(currentLevelIndex);
    for (AbstractRoboticArmAppState roboticArmLevelAppState : levels) {
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
          // send level score
          notifyListeners(new PropertyChangeEvent(this, "LEVEL_SCORE", null, currentLevelAppState.score));

          currentLevelIndex++;

          if (currentLevelIndex >= levels.size()) { // game over
            currentLevelIndex = 0;
            // currentLevelAppState.setEnabled(false);
            // gameOver = true;
            // scoreAppState.setEnabled(true);
            // notifyListeners(new PropertyChangeEvent(this, "GAME_OVER", null, null));
            currentLevelAppState.setEnabled(false);
            currentLevelAppState = levels.get(currentLevelIndex);
            currentLevelAppState.setEnabled(true);
            currentLevelAppState.setHudText();
            currentLevelAppState.score.initTime(timer.getTimeInSeconds());

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
      notifyListeners(new PropertyChangeEvent(this, "STATE_CHANGE", oldEnvState, newEnvState));
    }
  }

  public void addChangeListener(PropertyChangeListener newListener) {

    listeners.add(newListener);
  }

  /**
   * Send PropertyChangeEvent to observers (listeners)
   */
  public void notifyListeners(PropertyChangeEvent pce) {

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

  public int getCurrentLevelIndex() {
    return currentLevelIndex;
  }

  public int getNumTargetsPerLevel() {
    return numTargetsPerLevel;
  }

  public boolean isWasMovement() {
    return wasMovement;
  }

  public void setWasMovement(boolean wasMovement) {
    this.wasMovement = wasMovement;
  }

  public List<AbstractRoboticArmAppState> getLevels() {
    return levels;
  }
}
