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
package com.xeiam.proprioceptron.thematrixv1;

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
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.xeiam.proprioceptron.ProprioceptronApplication;
import com.xeiam.proprioceptron.thematrixv1.ObjectFactoryV1.GameView;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class TheMatrixV1 extends ProprioceptronApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  private GameView gameView = GameView.GOD_VIEW;

  /** prevents calculation of state during movement transitions */
  private boolean movementOver = true;
  private boolean nowWaiting = true;

  // player
  boolean playerIsHuman = false;
  private CharacterControl player;
  // AI
  private float toBeRotated;
  private float toBeMoved;
  // private final Vector3f walkDirection = new Vector3f(0, 0, 0);
  private final Vector3f viewDirection = new Vector3f(0, 0, 1);
  private boolean isGoingForward = false;
  private boolean isGoingBackward = false;
  private boolean isTurningLeft = false;
  private boolean isTurningRight = false;
  // pills
  private Geometry bluePill;
  // private Geometry redPill;
  
  BitmapText hudDistanceText;

  Random random = new Random();
  private boolean wasCollision;
  private float score;

  private BitmapText hudText;

  /**
   * the number of times simpleUpdate has been called.
   */
  private int count = 0;
  /**
   * the time in Milliseconds when the program was initialized.
   */
  private long starttime;

  /**
   * Constructor
   * 
   * @param gameView
   */
  public TheMatrixV1(GameView gameView) {

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

    // 2. make game environment
    ObjectFactoryV1.setupGameEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    // 3. make player
    player = ObjectFactoryV1.getPlayer(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    // pills
    bluePill = ObjectFactoryV1.getPill(assetManager, ColorRGBA.Blue);
    movePill(bluePill);
    rootNode.attachChild(bluePill);
    // redPill = MatrixPhysicsObjectFactoryV1.getPill(assetManager, ColorRGBA.Red);
    // movePill(redPill);
    // rootNode.attachChild(redPill);

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

    nowWaiting = false;
    // detect when buttons were released
    if (!keyPressed) {
      if (name.equals("toggleGameView")) {
        gameView = gameView.getNext();
        setCam();
      }
    }
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
 * 
 */
  @Override
  public void simpleUpdate(float tpf) {

    count++;
    // according to specs, the AI choose to arbitrarily be moved forward or turned in one timestep, but not both.
    // this version of Update is for the player. and does not require that.
    if (playerIsHuman) {
      humanUpdate(tpf);
    } else {

      AIUpdate(tpf);
    }
    if (movementOver) {
      score -= 1;
      // 1. Stop walking

      // 2. set old state because we're about to create a new one
      oldEnvState = newEnvState;

      // 3. handle collisions
      float bluePillDistance = player.getPhysicsLocation().distance(bluePill.getWorldTranslation()) - ObjectFactoryV1.PILL_RADIUS - ObjectFactoryV1.PLAYER_RADIUS;
      wasCollision = (bluePillDistance < 0.005f);
      if (wasCollision) {
        movePill(bluePill);
        score += 10;

      }

      // 4. calculate state

      Vector3f relativePosition = player.getPhysicsLocation().subtract(bluePill.getWorldTranslation());

      // more accurately modeling nostrils
      // calculate eye positions.
      Vector3f righteyelocation = player.getPhysicsLocation().add(viewDirection).add(viewDirection.cross(Vector3f.UNIT_Y));
      Vector3f lefteyelocation = player.getPhysicsLocation().add(viewDirection).add(Vector3f.UNIT_Y.cross(viewDirection));
      // calculate eye distances.
      float righteyedistance = righteyelocation.distance(bluePill.getWorldTranslation());
      float lefteyedistance = lefteyelocation.distance(bluePill.getWorldTranslation());

      // 2. notify listeners
      // TODO pass in right and left eye distance
      newEnvState = new TheMatrixV1EnvState(relativePosition, righteyedistance, lefteyedistance, bluePillDistance, wasCollision);
      notifyListeners();
    }

  }

  /**
   * AIUpdate depends on the values from getAIrotation() and getAImotion(). will execute a rotation, if one is needed. If not it will execute a motion if one is needed. If not, it will poll these methods again.
   * 
   * @param tpf
   */
  private void AIUpdate(float tpf) {

    if (FastMath.abs(toBeRotated) > tpf) {
      movementOver = false;
      nowWaiting = false;
      toBeRotated -= AIrotatestep(toBeRotated, tpf);
    } else if (FastMath.abs(toBeMoved) > 5 * tpf) {
      toBeMoved -= AIforwardstep(5 * toBeMoved, tpf);
    } else {
      movementOver = !nowWaiting;
      nowWaiting = movementOver || nowWaiting;
      toBeRotated = getAIrotation();
      toBeMoved = getAImotion();
    }
  }

  private float getAIrotation() {

    return random.nextFloat() * FastMath.PI * (random.nextBoolean() ? 1 : -1);
  }

  private float getAImotion() {

    return random.nextFloat() * 10 - 5;
  }

  /**
   * humanUpdate depends on the values set in onAction().
   * 
   * @param tpf
   */
  public void humanUpdate(float tpf) {

    movementOver = !(nowWaiting || isGoingForward || isGoingBackward || isTurningLeft || isTurningRight);
    nowWaiting = nowWaiting || movementOver;
    if (isGoingForward && !isGoingBackward)
      forwardEpsilon(5 * tpf);
    else if (!isGoingForward && isGoingBackward)
      forwardEpsilon(-5 * tpf);
    if (isTurningLeft && !isTurningRight)
      rotateEpsilon(tpf);
    else if (!isTurningLeft && isTurningRight)
      rotateEpsilon(-tpf);

  }
  public float AIrotatestep(float rotation, float tpf) {

    rotateEpsilon(tpf * Math.signum(rotation));
    return (tpf * Math.signum(rotation));
  }

  public float AIforwardstep(float distance, float tpf) {

    // should not use walk direction for this, since cannot determine the number of steps taken.
    forwardEpsilon(tpf * Math.signum(distance));
    return tpf * Math.signum(distance);
  }
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

  @Override
  public void collision(PhysicsCollisionEvent event) {

    // This is called back for collisions with all RigidBodyControls, even the floor!
    // System.out.println(event.getNodeA().toString());

  }

  @Override
  public void simpleRender(RenderManager rm) {

    setCam();

    hudText.setText("FPmS" + ((System.currentTimeMillis() - starttime) / (double) count) + "\nscore: " + score);
    // this number drops in exactly the same way when you comment out all of the update loop.

  }

  private void setCam() {

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

  private void movePill(Geometry pill) {

    float x = ObjectFactoryV1.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactoryV1.PLATFORM_DIMENSION);
    float z = ObjectFactoryV1.PLATFORM_DIMENSION / 2 - random.nextInt(ObjectFactoryV1.PLATFORM_DIMENSION);
    pill.center();
    pill.move(x, ObjectFactoryV1.PILL_RADIUS, z);
  }
}