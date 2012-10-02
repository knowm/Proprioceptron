package com.xeiam.proprioceptron.thematrix;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.renderer.RenderManager;

public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  int score;
  // hackety hack
  Matrix3f dirfacing;
  Matrix3f temp;
  float[] movescalar;// forward,backward,straferight,strafeleft, angularvelright, angularvelleft

  @Override
  public void simpleInitApp() {

    // stateManager.detach(stateManager.getState(FlyCamAppState.class));
    movescalar = new float[] { 0, 0, 0, 0, 0, 0 };
    dirfacing = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
    temp = new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0, 1);
    score = 0;
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    MatrixPhysicsObjectFactory.makeLevelEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.makeCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.makeBluePill(4, 3, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.makeRedPill(9, 13, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    setupKeys();
    // add ourselves as collision listener
    getPhysicsSpace().addCollisionListener(this);
  }

  private PhysicsSpace getPhysicsSpace() {

    return bulletAppState.getPhysicsSpace();
  }

  @Override
  public void simpleUpdate(float tpf) {

    rootNode.getChild("char").move(dirfacing.getColumn(0).x * tpf * movescalar[0], dirfacing.getColumn(0).y * tpf * movescalar[0], 0);
    rootNode.getChild("char").move(-dirfacing.getColumn(0).x * tpf * movescalar[1], -dirfacing.getColumn(0).y * tpf * movescalar[1], 0);
    rootNode.getChild("char").move(dirfacing.getColumn(1).x * tpf * movescalar[2], dirfacing.getColumn(1).y * tpf * movescalar[2], 0);
    rootNode.getChild("char").move(-dirfacing.getColumn(1).x * tpf * movescalar[3], -dirfacing.getColumn(1).y * tpf * movescalar[3], 0);
    temp.fromAngleAxis(1 * tpf, dirfacing.getColumn(2));
    dirfacing.mult(temp);
    rootNode.getChild("char").rotate(0, 0, movescalar[4] * tpf);
    temp.fromAngleAxis(-movescalar[5] * tpf, temp.getColumn(2));
    dirfacing.mult(temp);
    rootNode.getChild("char").rotate(0, 0, -movescalar[5] * tpf);

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf /* completelyunused */) {

    // if key is pressed, change velocity to .01f if key is unpressed, change back to 0
    if (name.equals("charforward")) {

      if (keyPressed)
        movescalar[0] = 1;
      else
        movescalar[0] = 0;
    }
    if (name.equals("charbackward")) {

      if (keyPressed)
        movescalar[1] = 1;
      else
        movescalar[1] = 0;
    }
    if (name.equals("charstraferight")) {

      if (keyPressed)
        movescalar[2] = 1;
      else
        movescalar[2] = 0;
    }
    if (name.equals("charstrafeleft")) {

      if (keyPressed)
        movescalar[3] = 1;
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

    inputManager.addMapping("charforward", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addMapping("charbackward", new KeyTrigger(KeyInput.KEY_G));
    inputManager.addMapping("charturnleft", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("charturnright", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("charstrafeleft", new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping("charstraferight", new KeyTrigger(KeyInput.KEY_E));
    inputManager.addListener(this, "charforward", "charbackward", "charturnleft", "charturnright", "charstrafeleft", "charstraferight");
  }

  @Override
  public void simpleRender(RenderManager rm) {

    // TODO: add render code
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    if (("red".equals(event.getNodeA().getName()) && "char".equals(event.getNodeB().getName())) || ("char".equals(event.getNodeA().getName()) && "red".equals(event.getNodeB().getName()))) {
      score -= 100;
    }
    if (("blue".equals(event.getNodeA().getName()) && "char".equals(event.getNodeB().getName())) || ("char".equals(event.getNodeA().getName()) && "blue".equals(event.getNodeB().getName()))) {
      score += 100;
    }
  }
}
