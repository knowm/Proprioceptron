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
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV0 extends SimpleApplication {

  /** BulletAppState allows using bullet physics in an Application. */
  private BulletAppState bulletAppState;

  @Override
  public void simpleInitApp() {

    cam.setLocation(new Vector3f(0f, 0f, 15f));
    cam.setRotation(new Quaternion(0f, 1f, -.13f, 0f));

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    RoboticArmUtils.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());

  }

  @Override
  public void simpleUpdate(float tpf) {

    System.out.println(cam.getLocation());
    System.out.println(cam.getRotation());

  }

  public static void main(String[] args) {

    RoboticArmV0 app = new RoboticArmV0();
    app.start();
  }

}
