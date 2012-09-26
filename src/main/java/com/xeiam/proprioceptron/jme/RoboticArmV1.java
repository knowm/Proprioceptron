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
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV1 extends SimpleApplication implements AnalogListener {

  /** BulletAppState allows using bullet physics in an Application. */
  private BulletAppState bulletAppState;

  Geometry section0;
  Node pivot0;

  Geometry section1;
  Node pivot1;

  @Override
  public void simpleInitApp() {

    cam.setLocation(new Vector3f(0f, 5f, 15f));
    cam.setRotation(new Quaternion(0f, 1f, -.13f, 0f));

    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    RoboticArmUtils.createWorld(rootNode, assetManager, bulletAppState.getPhysicsSpace());

    Box box = new Box(new Vector3f(0, 1, 0), .1f, 1, .1f);

    // ///////////////////////////////////

    section0 = new Geometry("Box", box);
    Material mat0 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat0.setColor("Color", ColorRGBA.Blue);
    section0.setMaterial(mat0);

    pivot0 = new Node("pivot");

    // ///////////////////////////////////

    section1 = new Geometry("Box", box);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Green);
    section1.setMaterial(mat1);

    pivot1 = new Node("pivot");

    // ///////////////////////////////////
    // pivot0.move(0, 0, 0);
    //
    // section1.move(0, 2, 0);
    // pivot1.attachChild(section0);

    rootNode.attachChild(pivot0);
    pivot1.move(0, 2, 0);
    pivot0.attachChild(pivot1);

    pivot0.attachChild(section0);
    pivot1.attachChild(section1);

    setupKeys();
  }

  private void setupKeys() {

    inputManager.addMapping("Left0", new KeyTrigger(KeyInput.KEY_L));
    inputManager.addMapping("Right0", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("Left1", new KeyTrigger(KeyInput.KEY_K));
    inputManager.addMapping("Right1", new KeyTrigger(KeyInput.KEY_O));
    inputManager.addListener(this, "Left0", "Right0", "Left1", "Right1");
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    if (binding.equals("Left0")) {
      pivot0.rotate(0, 0, value * speed);
    } else if (binding.equals("Right0")) {
      pivot0.rotate(0, 0, -1 * value * speed);
    } else if (binding.equals("Left1")) {
      pivot1.rotate(0, 0, value * speed);
    } else if (binding.equals("Right1")) {
      pivot1.rotate(0, 0, -1 * value * speed);
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
