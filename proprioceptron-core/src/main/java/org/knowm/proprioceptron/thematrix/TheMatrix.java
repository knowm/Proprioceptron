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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.knowm.proprioceptron.GameState;
import org.knowm.proprioceptron.thematrix.ObjectFactory.GameView;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

/**
 * @author Zackkenyon
 * @create Oct 5, 2012
 */
public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

  private static final int numBluePillsPerLevel = 10;

  /** default physics handler */
  private BulletAppState bulletAppState;

  /** specifies the type of camera */
  private GameView gameView = GameView.GOD_VIEW;

  /** prevents calculation of state during movement transitions */
  protected boolean movementOver = true;

  /** prevents calculation of state during resting periods */
  protected boolean nowWaiting = true;

  /** we attach this control to the character geometry, it implements physical interactions with rigid bodies by default, and gives us some minimal controls over movement. */
  protected CharacterControl player;

  /** the List of levels to be used in the game. */
  List<LevelAppState> levels;

  /** the current level, grabbed from the list of levels. according to the currentlevelindex */
  LevelAppState currentLevel;

  /** the index of the current level in the list of levels. */
  int currentlevelindex = 0;

  /** the character controller, can be told to update(tpf), to initialize(AppStateManager,Application) itself and to do something onAction(name,keyPressed,tpf) */
  PlayerAppState currentPlayer;

  /** the Orientation of the player as a unit vector. */
  final Vector3f viewDirection = new Vector3f(0, 0, 1);

  /** displays diagnostic information on the game window. */
  private BitmapText hudText;

  Random random = new Random();

  /** Specifies how well the computer is doing at the game. +10 for blue pills, -10 for red pills, -1 per movement. */
  public float score;

  /** the number of blue pills that have been collected so far. */
  public int numBluePills;

  /** GameState */
  public GameState oldEnvState;
  public GameState newEnvState;

  /** Listeners **/
  protected final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

  /**
   * Constructor
   * 
   * @param gameView the initial state of the camera.
   * @param controldelegate because the way the computer and the human interact with the game is so fundamentally different, the only design solution that makes sense, is to delegate the update and onAction methods to AIAppState and HumanAppState.
   */
  public TheMatrix(GameView gameView, PlayerAppState controldelegate) {

    currentPlayer = controldelegate;
    this.gameView = gameView;
  }

  @Override
  public void simpleInitApp() {

    // 1. Activate Physics
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    numBluePills = 0;

    // 2. make game environment and levels
    ObjectFactory.setupGameEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    levels = new ArrayList<LevelAppState>();
    levels.add(new LevelAppState(1, 0, false));
    levels.add(new LevelAppState(1, 1, false));
    levels.add(new LevelAppState(3, 0, false));
    levels.add(new LevelAppState(3, 3, false));
    levels.add(new LevelAppState(1, 0, true));
    levels.add(new LevelAppState(1, 1, true));
    levels.add(new LevelAppState(3, 0, true));
    levels.add(new LevelAppState(3, 3, true));
    for (LevelAppState s : levels) {
      s.initialize(getStateManager(), this);
    }
    currentLevel = levels.get(currentlevelindex);
    currentLevel.setEnabled(true);

    // 3. make player and player controllers
    player = ObjectFactory.getPlayer(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    currentPlayer.initialize(getStateManager(), this);
    // 4. setup keys
    setupKeys();

    // add ourselves as collision listener
    bulletAppState.getPhysicsSpace().addCollisionListener(this);

    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24); // font size
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setText("D="); // the text
    hudText.setLocalTranslation(10, settings.getHeight() - 10, 0); // position
    guiNode.attachChild(hudText);

    // setup camera
    stateManager.detach(stateManager.getState(FlyCamAppState.class));
    setCam();
    newEnvState = new TheMatrixEnvState(new ArrayList<PillPerceptionState>());
    oldEnvState = new TheMatrixEnvState(new ArrayList<PillPerceptionState>());
  }

  public void setcurrentlevel(int levelindex) {

    currentLevel.setEnabled(false);
    currentLevel = levels.get(levelindex);
    currentLevel.setEnabled(true);
  }

  public void setcurrentlevel(LevelAppState level) {

    currentLevel = level;
  }

  public void setupKeys() {

    inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_I));
    inputManager.addMapping("backward", new KeyTrigger(KeyInput.KEY_K));
    inputManager.addMapping("turnleft", new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("turnright", new KeyTrigger(KeyInput.KEY_L));
    inputManager.addMapping("toggleGameView", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(this, "forward", "backward", "turnleft", "turnright", "givecontrol", "toggleGameView");
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    // detect when buttons were released

    if (name.equals("toggleGameView")) {
      if (!keyPressed) {
        gameView = gameView.getNext();
        setCam();
      }
    } else {
      currentPlayer.onAction(name, keyPressed, tpf);
    }
  }

  /**
 * 
 */
  @Override
  public void simpleUpdate(float tpf) {

    // according to specs, the AI chooses to arbitrarily be moved forward or turned in one timestep, but not both.
    // this version of Update is for the player. and does not require that.
    currentPlayer.update(tpf);
    currentLevel.update(tpf);
    if (numBluePills > numBluePillsPerLevel * currentlevelindex) {

      setcurrentlevel(currentlevelindex++);

    }

  }

  public void rotateEpsilon(float epsilon) {

    Quaternion quat = new Quaternion();
    // seems silly to make this over and over.
    quat.fromAngleAxis(epsilon, Vector3f.UNIT_Y);
    quat.mult(viewDirection, viewDirection);
    player.setViewDirection(viewDirection);
  }

  public void forwardEpsilon(float epsilon) {

    player.setPhysicsLocation(player.getPhysicsLocation().add(viewDirection.mult(epsilon)));
  }

  /**
   * notify the AI of new commands.
   * 
   * @param commands
   */
  public void addAICommands(List<PlayerCommand> commands) {

    ((AIAppState) currentPlayer).pushCommand(commands);
  }

  /**
   * notify the AI of a single new command.
   * 
   * @param command
   */
  public void addAICommands(PlayerCommand command) {

    ((AIAppState) currentPlayer).pushCommand(command);
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    // This is called back for collisions with all RigidBodyControls, even the floor!
    // System.out.println(event.getNodeA().toString());

  }

  /**
   * any code which only needs to be called once every time the scene is drawn should go here.
   */
  @Override
  public void simpleRender(RenderManager rm) {

    setCam();

    hudText.setText("number of blue pills collected: " + numBluePills + "\nscore: " + score + newEnvState.toString());

  }

  /**
   * controls camera logic which needs to be updated when render is called.
   */
  public void setCam() {

    flyCam.setEnabled(false);

    // probably better practice to make camera objects with their own update methods and call them in render,
    if (gameView == GameView.THIRD_PERSON_CENTER) {
      cam.setLocation(new Vector3f(0, 5f, 0));
      cam.lookAt(player.getPhysicsLocation(), Vector3f.UNIT_Y);
    } else if (gameView == GameView.THIRD_PERSON_FOLLOW) {
      cam.setLocation(viewDirection.mult(-20f).add(player.getPhysicsLocation()).add(Vector3f.UNIT_Y.mult(5f)));
      cam.lookAt(player.getPhysicsLocation(), Vector3f.UNIT_Y);
    } else if (gameView == GameView.FIRST_PERSON) {
      cam.setLocation(player.getPhysicsLocation().add(Vector3f.UNIT_Y));
      cam.setAxes(Vector3f.UNIT_Y.cross(viewDirection), Vector3f.UNIT_Y, viewDirection);
    } else { // god view
      cam.setLocation(Vector3f.UNIT_Y.mult(62));
      cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
    }

  }

  /**
   * moves the pill to a random location within the bounds of the level.
   * 
   * @param pill
   */
  public void movePill(Geometry pill) {

    float x = ObjectFactory.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactory.PLATFORM_DIMENSION);
    float z = ObjectFactory.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactory.PLATFORM_DIMENSION);
    pill.center();
    pill.move(x, ObjectFactory.PILL_RADIUS, z);
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

}