package com.xeiam.proprioceptron.thematrix;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  int score;
  // hackety hack
  Vector3f dirfacing;

  @Override
  public void simpleInitApp() {

    // stateManager.detach(stateManager.getState(FlyCamAppState.class));

    dirfacing = Vector3f.UNIT_X;
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

  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("charforward")) {
      ((RigidBodyControl) rootNode.getChild("char").getControl(0)).setLinearVelocity(dirfacing.mult(.25f));
    }
    if (name.equals("charbackward")) {
      ((RigidBodyControl) rootNode.getChild("char").getControl(0)).setLinearVelocity(dirfacing.mult(-.25f));
    }
    if (name.equals("charstrafeleft")) {

      ((RigidBodyControl) rootNode.getChild("char").getControl(0)).setLinearVelocity(dirfacing.cross(Vector3f.UNIT_Z).mult(.25f));
    }
    if (name.equals("charstraferight")) {
      ((RigidBodyControl) rootNode.getChild("char").getControl(0)).setLinearVelocity(dirfacing.cross(Vector3f.UNIT_Z).mult(-.25f));
    }
    if (name.equals("charturnleft")) {
      dirfacing.add(dirfacing.cross(Vector3f.UNIT_Z).mult(.01f));// lazy but effective.
      dirfacing.normalize();
    }
    if (name.equals("charturnright")) {
      dirfacing.add(dirfacing.cross(Vector3f.UNIT_Z).mult(-.01f));
      dirfacing.normalize();
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
