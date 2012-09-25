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
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV1 extends SimpleApplication implements AnalogListener {

  /** BulletAppState allows using bullet physics in an Application. */
  private BulletAppState bulletAppState;

  Geometry blue;

  @Override
  public void simpleInitApp() {

    cam.setLocation(new Vector3f(0f, 5f, 15f));
    cam.setRotation(new Quaternion(0f, 1f, -.13f, 0f));

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    RoboticArmUtils.createWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());

    /** create a blue box at coordinates (1,-1,1) */
    Box box1 = new Box(new Vector3f(0, 1, 0), .1f, 1, .1f);
    blue = new Geometry("Box", box1);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Blue);
    blue.setMaterial(mat1);
    blue.move(0, 0, 0);

    rootNode.attachChild(blue);

    setupKeys();
  }

  private void setupKeys() {

    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_O));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addListener(this, "Left", "Right");
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    if (binding.equals("Left")) {
      blue.rotate(0, 0, value * speed);
    } else if (binding.equals("Right")) {
      blue.rotate(0, 0, -1 * value * speed);
    }
  }

  @Override
  public void simpleUpdate(float tpf) {

    // System.out.println(cam.getLocation());
    // System.out.println(cam.getRotation());

  }

  public static void main(String[] args) {

    RoboticArmV1 app = new RoboticArmV1();
    app.start();
  }

}
