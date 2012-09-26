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
import com.jme3.scene.shape.Sphere;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV1 extends SimpleApplication implements AnalogListener {

  private final static int NUM_JOINTS = 3;

  Node[] pivots;
  Geometry[] sections;
  Geometry[] joints;
  Geometry head;

  /**
   * Constructor
   */
  public RoboticArmV1() {

  }

  @Override
  public void simpleInitApp() {

    pivots = new Node[NUM_JOINTS];
    sections = new Geometry[NUM_JOINTS];
    joints = new Geometry[NUM_JOINTS];

    // Material for Box
    Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    mat.setFloat("m_Shininess", 0.1f);
    mat.setBoolean("m_UseMaterialColors", true);
    mat.setColor("m_Ambient", ColorRGBA.LightGray);
    mat.setColor("m_Diffuse", new ColorRGBA(.5f, .5f, .5f, 1f));
    mat.setColor("m_Specular", ColorRGBA.White);
    mat.setReceivesShadows(true);

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, 1, 0), .1f, 1, .1f);
    Sphere sphereSmall = new Sphere(20, 20, .2f);

    for (int i = 0; i < NUM_JOINTS; i++) {

      // Create pivots
      Node pivot = new Node("pivot");
      pivots[i] = pivot;

      // Create sections
      Geometry section = new Geometry("Box", box);
      section.setMaterial(mat);
      sections[i] = section;

      // create joints
      Geometry sphere = new Geometry("joint", sphereSmall);
      sphere.setMaterial(mat);
      joints[i] = sphere;

    }

    // Create Head
    Sphere sphereBig = new Sphere(20, 20, .3f);
    head = new Geometry("head", sphereBig);
    head.setMaterial(mat);

    cam.setLocation(new Vector3f(0f, 5f, 15f));
    cam.setRotation(new Quaternion(0f, 1f, -.13f, 0f));

    // Create World
    RoboticArmUtils.createWorld(rootNode, assetManager);

    // Create robotic Arm
    rootNode.attachChild(pivots[0]);

    for (int i = 0; i < NUM_JOINTS; i++) {

      if (i > 0) {
        pivots[i].move(0, 2, 0);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < NUM_JOINTS - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    head.move(0, 2, 0);
    pivots[NUM_JOINTS - 1].attachChild(head);

    setupKeys();
  }

  private void setupKeys() {

    inputManager.addMapping("Left0", new KeyTrigger(KeyInput.KEY_L));
    inputManager.addMapping("Right0", new KeyTrigger(KeyInput.KEY_P));
    inputManager.addMapping("Left1", new KeyTrigger(KeyInput.KEY_K));
    inputManager.addMapping("Right1", new KeyTrigger(KeyInput.KEY_O));
    inputManager.addMapping("Left2", new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("Right2", new KeyTrigger(KeyInput.KEY_I));
    inputManager.addListener(this, "Left0", "Right0", "Left1", "Right1", "Left2", "Right2");
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    if (binding.equals("Left0")) {
      pivots[0].rotate(0, 0, value * speed);
    } else if (binding.equals("Right0")) {
      pivots[0].rotate(0, 0, -1 * value * speed);
    } else if (binding.equals("Left1")) {
      pivots[1].rotate(0, 0, value * speed);
    } else if (binding.equals("Right1")) {
      pivots[1].rotate(0, 0, -1 * value * speed);
    } else if (binding.equals("Left2")) {
      pivots[2].rotate(0, 0, value * speed);
    } else if (binding.equals("Right2")) {
      pivots[2].rotate(0, 0, -1 * value * speed);
    }
  }

  @Override
  public void simpleUpdate(float tpf) {

    // System.out.println(cam.getLocation());
    // System.out.println(cam.getRotation());

  }

  public static void main(String[] args) {

    RoboticArmV1 app = new RoboticArmV1();
    app.setShowSettings(false);
    app.start();
  }

}
