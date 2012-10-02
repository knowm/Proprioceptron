package com.xeiam.proprioceptron.game;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.renderer.RenderManager;

public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener {

  private BulletAppState bulletAppState;


  public static void main(String[] args) {

    TheMatrix app = new TheMatrix();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);


    MatrixPhysicsObjectFactory.MakeLevelEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.MakeCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.MakeBluePill(4, 3, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    MatrixPhysicsObjectFactory.MakeRedPill(9, 13, rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    // add ourselves as collision listener
    getPhysicsSpace().addCollisionListener(this);
  }

  private PhysicsSpace getPhysicsSpace() {

    return bulletAppState.getPhysicsSpace();
  }

  @Override
  public void simpleUpdate(float tpf) {

    // TODO: add update code
  }

  @Override
  public void simpleRender(RenderManager rm) {

    // TODO: add render code
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    if ("Box".equals(event.getNodeA().getName()) || "Box".equals(event.getNodeB().getName())) {
      if ("bullet".equals(event.getNodeA().getName()) || "bullet".equals(event.getNodeB().getName())) {
        fpsText.setText("You hit the box!");
      }
    }
  }
}
