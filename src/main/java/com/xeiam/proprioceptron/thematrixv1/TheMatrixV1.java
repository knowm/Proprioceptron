package com.xeiam.proprioceptron.thematrixv1;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.xeiam.proprioceptron.ProprioceptronApplication;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class TheMatrixV1 extends ProprioceptronApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;

  private CharacterControl player;

  // // hackety hack
  private final Matrix3f rightepsilon;
  private final Matrix3f leftepsilon;

  private boolean turnleft;
  private boolean turnright;
  private boolean goforward;
  private boolean gobackward;

  BitmapText hudText;

  public TheMatrixV1() {

    super();
    Matrix3f temp;

    temp = Matrix3f.IDENTITY;
    temp.fromAngleAxis(-.001f, Vector3f.UNIT_Y);
    rightepsilon = temp.clone();

    temp = Matrix3f.IDENTITY;
    temp.fromAngleAxis(.001f, Vector3f.UNIT_Y);
    leftepsilon = temp.clone();

  }

  @Override
  public void simpleInitApp() {

    super.simpleInitApp();

    // 1. Activate Physics
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    // 2. make game env
    MatrixPhysicsObjectFactoryV1.makeGameEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    // 3. make plyer
    player = MatrixPhysicsObjectFactoryV1.makeCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

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

  }

  @Override
  public void simpleUpdate(float tpf) {

    player.getViewDirection().normalize();
    if (turnright != turnleft) {
      if (turnright) {
        player.setViewDirection(rightepsilon.mult(player.getViewDirection()));

      } else {
        player.setViewDirection(leftepsilon.mult(player.getViewDirection()));

      }
      if (goforward != gobackward) {
        if (goforward)
          player.setWalkDirection(player.getViewDirection().mult(.1f));
        else
          player.setWalkDirection(player.getViewDirection().mult(-.1f));
      } else
        player.setWalkDirection(Vector3f.ZERO);

    }
    cam.setLocation(((Geometry) rootNode.getChild("player")).getWorldTranslation().add(player.getViewDirection().negate().mult(10)).add(Vector3f.UNIT_Y.mult(5)));
    cam.lookAt(((Geometry) rootNode.getChild("player")).getWorldTranslation(), Vector3f.UNIT_Y);

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    // if key is pressed, change velocity to .01f if key is unpressed, change back to 0
    if (name.equals("charforward")) {

      goforward = keyPressed;
    }
    if (name.equals("charbackward")) {

      gobackward = keyPressed;
    }
    if (name.equals("charturnright")) {
      turnright = keyPressed;
    }
    if (name.equals("charturnleft")) {
      turnleft = keyPressed;
    }
    if (goforward != gobackward) {
      if (goforward)
        player.setWalkDirection(player.getViewDirection().mult(.1f));
      else
        player.setWalkDirection(player.getViewDirection().mult(-.1f));
    } else
      player.setWalkDirection(Vector3f.ZERO);

  }

  public void setupKeys() {

    inputManager.addMapping("charforward", new KeyTrigger(KeyInput.KEY_UP));
    inputManager.addMapping("charbackward", new KeyTrigger(KeyInput.KEY_DOWN));
    inputManager.addMapping("charturnleft", new KeyTrigger(KeyInput.KEY_LEFT));
    inputManager.addMapping("charturnright", new KeyTrigger(KeyInput.KEY_RIGHT));
    inputManager.addListener(this, "charforward", "charbackward", "charturnleft", "charturnright");
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    // This is called back for collisions with all RigidBodyControls, even the floor!
    // System.out.println(event.getNodeA().toString());

  }
}
