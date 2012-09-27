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
import com.jme3.font.BitmapText;
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
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV1 extends SimpleApplication implements AnalogListener {

  private static final float JOINT_RADIUS = 0.2f;
  private static final float HEAD_RADIUS = 0.3f;
  private static final float EYE_RADIUS = 0.1f;
  private static final float TARGET_RADIUS = 0.8f;

  private static final float SECTION_LENGTH = 1.0f;
  private static final float SECTION_CROSS_DIM = 0.1f;

  private int numJoints;

  BitmapText hudText;

  private Node[] pivots;
  private Geometry[] sections;
  private Geometry[] joints;
  private Node headNode;
  private Geometry head;
  private Geometry leftEye;
  private Geometry rightEye;

  private Geometry target;

  /**
   * Constructor
   */
  public RoboticArmV1(int numJoints) {

    if (numJoints > 3) {
      throw new IllegalArgumentException("Only 3 or less joints are acceptable at this time");
    }

    this.numJoints = numJoints;

  }

  @Override
  public void simpleInitApp() {

    pivots = new Node[numJoints];
    sections = new Geometry[numJoints];
    joints = new Geometry[numJoints];

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setFloat("m_Shininess", .5f);
    matRoboticArm.setBoolean("m_UseMaterialColors", true);
    matRoboticArm.setColor("m_Ambient", ColorRGBA.White.mult(1.3f));
    matRoboticArm.setColor("m_Diffuse", new ColorRGBA(.5f, .5f, .5f, 1f));
    matRoboticArm.setColor("m_Specular", ColorRGBA.White);

    Material matTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
    matTarget.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.White);
    matTarget.setFloat("Shininess", 5f); // [1,128]

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, SECTION_LENGTH, 0), SECTION_CROSS_DIM, SECTION_LENGTH, SECTION_CROSS_DIM);
    Sphere sphereJoint = new Sphere(20, 20, JOINT_RADIUS);
    sphereJoint.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereJoint);

    for (int i = 0; i < numJoints; i++) {

      // Create pivots
      Node pivot = new Node("pivot");
      pivots[i] = pivot;

      // Create sections
      Geometry section = new Geometry("Box", box);
      section.setMaterial(matRoboticArm);
      sections[i] = section;

      // create joints
      Geometry sphere = new Geometry("joint", sphereJoint);
      sphere.setMaterial(matRoboticArm);
      joints[i] = sphere;

    }

    // Create Head
    headNode = new Node("headNode");
    Sphere sphereHead = new Sphere(20, 20, HEAD_RADIUS);
    sphereHead.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereHead);
    head = new Geometry("head", sphereHead);
    head.setMaterial(matRoboticArm);
    // eyes
    Sphere sphereEye = new Sphere(20, 20, EYE_RADIUS);
    leftEye = new Geometry("leftEye", sphereEye);
    leftEye.setMaterial(matRoboticArm);
    rightEye = new Geometry("rightEye", sphereEye);
    rightEye.setMaterial(matRoboticArm);

    // Create Target
    Sphere sphereTarget = new Sphere(30, 30, TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    target = new Geometry("head", sphereTarget);
    target.setMaterial(matTarget);

    // Change Camera position
    cam.setLocation(new Vector3f(0f, 5f, 17f));
    cam.setRotation(new Quaternion(0f, 1f, -.08f, 0f));

    // Create World
    RoboticArmUtils.createWorld(rootNode, assetManager);

    // Place target
    float arcRadius = 2 * SECTION_LENGTH * numJoints + TARGET_RADIUS + HEAD_RADIUS;
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float y = (float) Math.sqrt(arcRadius * arcRadius - x * x);
    target.move(x, y, 0);
    rootNode.attachChild(target);

    // Create robotic Arm
    rootNode.attachChild(pivots[0]);

    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        pivots[i].move(0, 2 * SECTION_LENGTH, 0);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < numJoints - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    // place Head
    headNode.move(0, 2 * SECTION_LENGTH, 0);
    headNode.attachChild(head);
    float shift = (float) Math.sqrt(HEAD_RADIUS * HEAD_RADIUS / 2.0);
    leftEye.move(-1.0f * shift, shift, 0);
    headNode.attachChild(leftEye);
    rightEye.move(shift, shift, 0);
    headNode.attachChild(rightEye);
    pivots[numJoints - 1].attachChild(headNode);

    setupKeys();

    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24); // font size
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setText("D="); // the text
    hudText.setLocalTranslation(10, settings.getHeight() - 10, 0); // position
    guiNode.attachChild(hudText);

    // hide scene graph statistics
    setDisplayStatView(false);
    setDisplayFps(false);
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

    Vector3f targetCoords = target.getWorldTranslation();

    // Vector3f headCoords = head.getWorldTranslation();
    // float dist = headCoords.distance(targetCoords);

    Vector3f leftEyeCoords = leftEye.getWorldTranslation();
    float distL = leftEyeCoords.distance(targetCoords) - TARGET_RADIUS;

    Vector3f rightEyeCoords = rightEye.getWorldTranslation();
    float distR = rightEyeCoords.distance(targetCoords) - TARGET_RADIUS;

    float distAve = (distL + distR) / 2;

    hudText.setText(" DL= " + distL + " DR= " + distR + " D= " + distAve);
    // System.out.println(cam.getLocation());
    // System.out.println(cam.getRotation());

  }

  public static void main(String[] args) {

    RoboticArmV1 app = new RoboticArmV1(1);
    app.setShowSettings(false);
    app.start();
  }

}
