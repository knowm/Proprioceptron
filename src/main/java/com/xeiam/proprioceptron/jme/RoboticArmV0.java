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

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV0 extends SimpleApplication {

  /** BulletAppState allows using bullet physics in an Application. */
  private BulletAppState bulletAppState;

  @Override
  public void simpleInitApp() {

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    RoboticArmUtils.createPhysicsTestWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());

  }

  public static void main(String[] args) {

    RoboticArmV0 app = new RoboticArmV0();
    app.start();
  }

}
