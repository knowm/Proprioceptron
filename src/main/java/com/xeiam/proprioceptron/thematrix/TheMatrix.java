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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme3.app.FlyCamAppState;
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
import com.xeiam.proprioceptron.ProprioceptronApplication;
import com.xeiam.proprioceptron.thematrix.ObjectFactory.GameView;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class TheMatrix extends ProprioceptronApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  private GameView gameView = GameView.GOD_VIEW;

  /** prevents calculation of state during movement transitions */
  protected boolean movementOver = true;
  protected boolean nowWaiting = true;
  // movement
  protected CharacterControl player;
  // AppStates
  List<LevelAppState> levels;
  HumanAppState human;
  AIAppState ai;
  LevelAppState currentLevel;
  int currentlevelindex = 0;
  PlayerAppState currentPlayer;
  // private final Vector3f walkDirection = new Vector3f(0, 0, 0);
  final Vector3f viewDirection = new Vector3f(0, 0, 1);

  // pills
  BitmapText hudDistanceText;

  Random random = new Random();
  private boolean wasCollision;
  public float score;

  private BitmapText hudText;

  /**
   * the number of times simpleUpdate has been called.
   */
  private final int count = 0;
  /**
   * the time in Milliseconds when the program was initialized.
   */
  private long starttime;

  /**
   * Constructor
   * 
   * @param gameView
   */
  public TheMatrix(GameView gameView) {

    this.gameView = gameView;
  }

  @Override
  public void simpleInitApp() {

    super.simpleInitApp();

    // 1. Activate Physics
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    wasCollision = false;

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
    ai = new AIAppState();
    ai.initialize(getStateManager(), this);
    human = new HumanAppState();
    human.initialize(getStateManager(), this);
    currentPlayer = human;

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
    starttime = System.currentTimeMillis();
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
    inputManager.addListener(this, "forward", "backward", "turnleft", "turnright", "toggleGameView");
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {


    // detect when buttons were released
    if (!keyPressed) {
      if (name.equals("toggleGameView")) {
        gameView = gameView.getNext();
        setCam();
      }
    }
    currentPlayer.onAction(name, keyPressed, tpf);

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
    if (score > 50 * currentlevelindex) {
      currentLevel.setEnabled(false);
      currentLevel = levels.get(currentlevelindex++);
      currentLevel.setEnabled(true);
    }


  }

  /**
   * AIUpdate depends on the values from getAIrotation() and getAImotion(). will execute a rotation, if one is needed. If not it will execute a motion if one is needed. If not, it will poll these methods again.
   * 
   * @param tpf
   */

  public void rotateEpsilon(float epsilon){

    Quaternion quat = new Quaternion();
    // seems silly to make this over and over.
    quat.fromAngleAxis(epsilon, Vector3f.UNIT_Y);
    quat.mult(viewDirection, viewDirection);
    player.setViewDirection(viewDirection);
  }
  public void forwardEpsilon(float epsilon){

    player.setPhysicsLocation(player.getPhysicsLocation().add(viewDirection.mult(epsilon)));
  }

  // notify AIcontroller of new AI commands from simplebrain
  public void addAICommands(List<PlayerCommand> commands) {

    ai.pushCommand(commands);
  }

  public void addAICommands(PlayerCommand command) {

    ai.pushCommand(command);
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    // This is called back for collisions with all RigidBodyControls, even the floor!
    // System.out.println(event.getNodeA().toString());

  }

  @Override
  public void simpleRender(RenderManager rm) {

    setCam();

    hudText.setText("score: " + score);

  }

  public void setCam() {

    if (gameView == GameView.THIRD_PERSON_CENTER) {
      cam.setLocation(new Vector3f(0, 5f, 0));
      cam.lookAt(player.getPhysicsLocation(), Vector3f.UNIT_Y);
    } else if (gameView == GameView.THIRD_PERSON_FOLLOW) {
      cam.setLocation(viewDirection.clone().multLocal(-20f).add(player.getPhysicsLocation()).add(Vector3f.UNIT_Y.mult(5f)));
      cam.lookAt(player.getPhysicsLocation(), Vector3f.UNIT_Y);
    } else if (gameView == GameView.FIRST_PERSON) {// some problems with rotation are visible from this view.
      cam.setLocation(player.getPhysicsLocation().add(Vector3f.UNIT_Y));
      cam.setAxes(Vector3f.UNIT_Y.cross(viewDirection), Vector3f.UNIT_Y, viewDirection);
    } else { // god view
      cam.setLocation(Vector3f.UNIT_Y.mult(62));
      cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);
    }

  }

  public void movePill(Geometry pill) {

    float x = ObjectFactory.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactory.PLATFORM_DIMENSION);
    float z = ObjectFactory.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactory.PLATFORM_DIMENSION);
    pill.center();
    pill.move(x, ObjectFactory.PILL_RADIUS, z);
  }
}