package com.xeiam.proprioceptron.thematrix;

import java.util.Random;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
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
  private Random rand;
  // hackety hack
  Matrix3f dirfacing;
  Matrix3f temp;
  float[] movescalar;// forward,backward,straferight,strafeleft, angularvelright, angularvelleft

  BitmapText hudText;
  @Override
  public void simpleInitApp() {

    rand = new Random();
    movescalar = new float[] { 0, 0, 0, 0, 0, 0 };
    dirfacing = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
    temp = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);

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
    rootNode.getChild("char").move(dirfacing.getColumn(0).x * tpf * movescalar[0], 0, dirfacing.getColumn(0).z * tpf * movescalar[0]);
    rootNode.getChild("char").move(-dirfacing.getColumn(0).x * tpf * movescalar[1], 0, -dirfacing.getColumn(0).z * tpf * movescalar[1]);
    rootNode.getChild("char").move(dirfacing.getColumn(2).x * tpf * movescalar[2], 0, dirfacing.getColumn(2).z * tpf * movescalar[2]);
    rootNode.getChild("char").move(-dirfacing.getColumn(2).x * tpf * movescalar[3], 0, -dirfacing.getColumn(2).z * tpf * movescalar[3]);
    temp.fromAngleAxis(-movescalar[4] * tpf, temp.getColumn(1));
    dirfacing = dirfacing.mult(temp);
    rootNode.getChild("char").rotate(0, 0, -movescalar[4] * tpf);
    temp.fromAngleAxis(movescalar[5] * tpf, temp.getColumn(1));
    dirfacing = dirfacing.mult(temp);
    rootNode.getChild("char").rotate(0, 0, movescalar[5] * tpf);


  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf /* completelyunused */) {

    // if key is pressed, change velocity to .01f if key is unpressed, change back to 0
    if (name.equals("charforward")) {

      if (keyPressed)
        movescalar[0] = 4;
      else
        movescalar[0] = 0;
    }
    if (name.equals("charbackward")) {

      if (keyPressed)
        movescalar[1] = 4;
      else
        movescalar[1] = 0;
    }
    if (name.equals("charstraferight")) {

      if (keyPressed)
        movescalar[2] = 4;
      else
        movescalar[2] = 0;
    }
    if (name.equals("charstrafeleft")) {

      if (keyPressed)
        movescalar[3] = 4;
      else
        movescalar[3] = 0;

    }
    if (name.equals("charturnright")) {

      if (keyPressed)
        movescalar[4] = 1;
      else
        movescalar[4] = 0;
    }
    if (name.equals("charturnleft")) {

      if (keyPressed)
        movescalar[5] = 1;
      else
        movescalar[5] = 0;
    }
  }

  public void setupKeys() {

    inputManager.addMapping("charforward", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("charbackward", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("charturnleft", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("charturnright", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("charstrafeleft", new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping("charstraferight", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addListener(this, "charforward", "charbackward", "charturnleft", "charturnright", "charstrafeleft", "charstraferight");
  }

  @Override
  public void simpleRender(RenderManager rm) {

    cam.setLocation(((Geometry) rootNode.getChild("char")).getWorldTranslation().add(dirfacing.getColumn(0).negate().mult(10)).add(Vector3f.UNIT_Y.mult(5)));
    cam.lookAt(((Geometry) rootNode.getChild("char")).getWorldTranslation(), Vector3f.UNIT_Y);
    hudText.setText("score: " + score); // the text

    // TODO: add render code
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    if (("red".equals(event.getNodeA().getName()) && "char".equals(event.getNodeB().getName())) || ("char".equals(event.getNodeA().getName()) && "red".equals(event.getNodeB().getName()))) {
      score -= 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
      }
      MatrixPhysicsObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

    }
    if (("blue".equals(event.getNodeA().getName()) && "char".equals(event.getNodeB().getName())) || ("char".equals(event.getNodeA().getName()) && "blue".equals(event.getNodeB().getName()))) {
      score += 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
      }
      MatrixPhysicsObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

    }
  }
}
