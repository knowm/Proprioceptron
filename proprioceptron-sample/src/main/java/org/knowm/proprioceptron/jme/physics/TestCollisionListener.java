/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://manc.com) and contributors.
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
package org.knowm.proprioceptron.jme.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;

/**
 * @author normenhansen
 */
public class TestCollisionListener extends SimpleApplication implements PhysicsCollisionListener {

  private BulletAppState bulletAppState;
  private Sphere bullet;
  private SphereCollisionShape bulletCollisionShape;

  public static void main(String[] args) {

    TestCollisionListener app = new TestCollisionListener();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
    bullet = new Sphere(32, 32, 0.4f, true, false);
    bullet.setTextureMode(TextureMode.Projected);
    bulletCollisionShape = new SphereCollisionShape(0.4f);

    PhysicsTestHelper.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());
    PhysicsTestHelper.createBallShooter(this, rootNode, bulletAppState.getPhysicsSpace());

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
