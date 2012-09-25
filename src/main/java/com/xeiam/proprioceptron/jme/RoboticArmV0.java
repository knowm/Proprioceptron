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
package com.xeiam.proprioceptron.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV0 extends SimpleApplication {

  /** BulletAppState allows using bullet physics in an Application. */
  private BulletAppState bulletAppState;

  private HingeJoint joint;

  @Override
  public void simpleInitApp() {

    cam.setLocation(new Vector3f(0f, 5f, 15f));
    cam.setRotation(new Quaternion(0f, 1f, -.13f, 0f));

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    RoboticArmUtils.createWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());

    setupJoint();
  }

  private void setupJoint() {

    Node baseNode = createPhysicsNode(assetManager, new BoxCollisionShape(new Vector3f(.3f, .3f, .3f)), 0);
    baseNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f, 0, 0f));
    rootNode.attachChild(baseNode);
    bulletAppState.getPhysicsSpace().add(baseNode);

    // Node hammerNode = PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f(.3f, .3f, .3f)), 1);
    Node hammerNode = createPhysicsNode(assetManager, new SphereCollisionShape(.3f), 0);
    hammerNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f, 1f, 0f));
    rootNode.attachChild(hammerNode);
    bulletAppState.getPhysicsSpace().add(hammerNode);

    joint = new HingeJoint(baseNode.getControl(RigidBodyControl.class), hammerNode.getControl(RigidBodyControl.class), Vector3f.ZERO, new Vector3f(0f, -1f, 0f), Vector3f.UNIT_Z, Vector3f.UNIT_Z);
    bulletAppState.getPhysicsSpace().add(joint);
  }

  /**
   * creates an empty node with a RigidBodyControl
   * 
   * @param manager
   * @param shape
   * @param mass
   * @return
   */
  private Node createPhysicsNode(AssetManager manager, CollisionShape shape, float mass) {

    Node node = new Node("PhysicsNode");
    RigidBodyControl control = new RigidBodyControl(shape, mass);
    node.addControl(control);
    return node;
  }

  @Override
  public void simpleUpdate(float tpf) {

    // System.out.println(cam.getLocation());
    // System.out.println(cam.getRotation());

  }

  public static void main(String[] args) {

    RoboticArmV0 app = new RoboticArmV0();
    app.start();
  }

}
