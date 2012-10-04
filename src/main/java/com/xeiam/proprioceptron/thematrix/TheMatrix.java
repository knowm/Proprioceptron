package com.xeiam.proprioceptron.thematrix;

import java.util.Random;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
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
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  float score;
  private final Random rand;
  // hackety hack
  private final Matrix3f rightepsilon;
  private final Matrix3f leftepsilon;

  // there is no native support for angular velocity so we set these flags in triggers and have angular velocity modeled in update.
  private boolean turnleft;
  private boolean turnright;
  private boolean goforward;
  private boolean gobackward;

  BitmapText hudText;

  public TheMatrix() {

    super();
    Matrix3f temp;

    temp = Matrix3f.IDENTITY;
    temp.fromAngleAxis(-.001f, Vector3f.UNIT_Y);
    rightepsilon = temp.clone();

    temp = Matrix3f.IDENTITY;
    temp.fromAngleAxis(.001f, Vector3f.UNIT_Y);
    leftepsilon = temp.clone();

    rand = new Random();

  }

  @Override
  public void simpleInitApp() {

    score = 50;
    bulletAppState = new BulletAppState();
    stateManager.detach(stateManager.getState(FlyCamAppState.class));

    stateManager.attach(bulletAppState);

    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    MatrixPhysicsObjectFactory.makeLevelEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.makeCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    MatrixPhysicsObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    setupKeys();
    // add ourselves as collision listener
    getPhysicsSpace().addCollisionListener(this);
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
    hudText.setColor(ColorRGBA.Blue); // font color
    hudText.setText("score: " + score); // the text
    hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
    guiNode.attachChild(hudText);

  }

  private PhysicsSpace getPhysicsSpace() {

    return bulletAppState.getPhysicsSpace();
  }

  @Override
  public void simpleUpdate(float tpf) {
    score -= tpf;
    ((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().normalize();
    if (turnright != turnleft) {
      if (turnright) {
        ((CharacterControl) rootNode.getChild("char").getControl(0)).setViewDirection(rightepsilon.mult(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection()));

      } else {
        ((CharacterControl) rootNode.getChild("char").getControl(0)).setViewDirection(leftepsilon.mult(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection()));

      }
      if (goforward != gobackward) {
        if (goforward)
          ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().mult(.1f));
        else
          ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().mult(-.1f));
      } else
        ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(Vector3f.ZERO);

    }
    cam.setLocation(((Geometry) rootNode.getChild("char")).getWorldTranslation().add(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().negate().mult(10)).add(Vector3f.UNIT_Y.mult(5)));
    cam.lookAt(((Geometry) rootNode.getChild("char")).getWorldTranslation(), Vector3f.UNIT_Y);
    hudText.setText("score: " + score);

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf /* completelyunused */) {

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
        ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().mult(.1f));
      else
        ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(((CharacterControl) rootNode.getChild("char").getControl(0)).getViewDirection().mult(-.1f));
    } else
      ((CharacterControl) rootNode.getChild("char").getControl(0)).setWalkDirection(Vector3f.ZERO);

  }

  public void setupKeys() {

    inputManager.addMapping("charforward", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("charbackward", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("charturnleft", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("charturnright", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addListener(this, "charforward", "charbackward", "charturnleft", "charturnright");
  }

  @Override
  public void simpleRender(RenderManager rm) {

    // the text

    // TODO: add render code
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    hudText.setText("collision");
    if ("char".equals(event.getNodeA().getName()) && "red".equals(event.getNodeB().getName())) {
      score -= 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
        MatrixPhysicsObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
        MatrixPhysicsObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

      }

    }
    if ("char".equals(event.getNodeA().getName()) && "blue".equals(event.getNodeB().getName())) {
      score += 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
        MatrixPhysicsObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);
      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
        MatrixPhysicsObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);
      }
    }
  }
}
