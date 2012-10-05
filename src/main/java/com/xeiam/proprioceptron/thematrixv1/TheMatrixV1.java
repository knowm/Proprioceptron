package com.xeiam.proprioceptron.thematrixv1;

import java.util.Random;

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

  // player
  private CharacterControl player;
  private final Vector3f walkDirection = new Vector3f(0, 0, 0);
  private Vector3f viewDirection = new Vector3f(0, 0, 0);

  // pills
  private Geometry bluePill;
  // private Geometry redPill;

  private boolean turnleft;
  private boolean turnright;
  private boolean goforward;
  private boolean gobackward;

  BitmapText hudText;

  Random random = new Random();

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

    // 2. make game environment
    ObjectFactoryV1.setupGameEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    // 3. make player
    player = ObjectFactoryV1.getCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

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

    // setup HUD
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
    hudText.setColor(ColorRGBA.Blue); // font color
    hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
    guiNode.attachChild(hudText);

    // setup camera
    setCam();
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

    if (name.equals("forward")) {
      goforward = keyPressed;
    }
    if (name.equals("backward")) {
      gobackward = keyPressed;
    }
    if (name.equals("turnright")) {
      turnright = keyPressed;
    }
    if (name.equals("turnleft")) {
      turnleft = keyPressed;
    }
    if (name.equals("toggleGameView") && !keyPressed) {
      gameView = gameView.getNext();
    }

  }

  @Override
  public void simpleUpdate(float tpf) {

    if (turnleft || turnright) {
      // rotation
      Quaternion quat = new Quaternion();
      quat.fromAngleAxis(FastMath.PI / 1000 * (turnleft ? 1.0f : -1.0f), Vector3f.UNIT_Y);
      Vector3f playerLeft = player.getViewDirection().clone();
      quat.mult(playerLeft, playerLeft);
      viewDirection = playerLeft;
      player.setViewDirection(playerLeft);
    }

    if (goforward || gobackward) {
      // forward direction
      Vector3f playerDir = player.getViewDirection().clone().multLocal((goforward ? 1.0f : -1.0f) * 0.25f);
      walkDirection.set(0, 0, 0);
      walkDirection.addLocal(playerDir);
      player.setWalkDirection(walkDirection); // THIS IS WHERE THE WALKING HAPPENS
    }

    else {
      walkDirection.set(0, 0, 0);
      player.setWalkDirection(walkDirection); // Stop walking
    }

    // handle collisions
    float bluePillDistance = player.getPhysicsLocation().distance(bluePill.getWorldTranslation()) - ObjectFactoryV1.PILL_RADIUS - ObjectFactoryV1.PLAYER_RADIUS;
    boolean wasCollision = bluePillDistance < 0.005f;
    if (wasCollision) {
      movePill(bluePill);
    }
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    // This is called back for collisions with all RigidBodyControls, even the floor!
    // System.out.println(event.getNodeA().toString());

  }

  @Override
  public void simpleRender(RenderManager rm) {

    setCam();

    // hudText.setText("score: " + score);
    // the text
  }

  private void setCam() {

    if (gameView == GameView.THIRD_PERSON_CENTER) {

      // put the camera in the center of the platform and look at the player
      cam.setLocation(new Vector3f(0, .5f, 0));
      cam.lookAt(player.getPhysicsLocation(), Vector3f.UNIT_Y);

    } else if (gameView == GameView.THIRD_PERSON_FOLLOW) {

      // TODO implement this, viewDirection may be helpful here

    } else if (gameView == GameView.FIRST_PERSON) {

      // TODO implement this, viewDirection may be helpful here

    } else {

      // hover above the platform and look down
      cam.setLocation(Vector3f.UNIT_Y.mult(66));
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
