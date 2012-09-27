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
import com.jme3.system.AppSettings;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmV1 extends SimpleApplication implements AnalogListener {

  private int numJoints;

  BitmapText hudDistanceText;
  BitmapText hudPositionText;

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

    // scene background color
    // viewPort.setBackgroundColor(new ColorRGBA(.5f, .8f, .99f, 1.0f));

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setBoolean("m_UseMaterialColors", true);
    matRoboticArm.setColor("m_Diffuse", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setColor("m_Specular", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setFloat("m_Shininess", 50f); // [1,128] lower is shinier

    Material matTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Red);
    matTarget.setFloat("Shininess", 128f); // [1,128]

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, 0, Constants.SECTION_LENGTH), Constants.SECTION_CROSS_DIM, Constants.SECTION_CROSS_DIM, Constants.SECTION_LENGTH);
    Sphere sphereJoint = new Sphere(20, 20, Constants.JOINT_RADIUS);
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
    Sphere sphereHead = new Sphere(20, 20, Constants.HEAD_RADIUS);
    sphereHead.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereHead);
    head = new Geometry("head", sphereHead);
    head.setMaterial(matRoboticArm);

    // eyes
    Sphere sphereEye = new Sphere(20, 20, Constants.EYE_RADIUS);
    leftEye = new Geometry("leftEye", sphereEye);
    leftEye.setMaterial(matRoboticArm);
    rightEye = new Geometry("rightEye", sphereEye);
    rightEye.setMaterial(matRoboticArm);

    // Create Target
    Sphere sphereTarget = new Sphere(30, 30, Constants.TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    target = new Geometry("head", sphereTarget);
    target.setMaterial(matTarget);

    // Change Camera position
    cam.setLocation(new Vector3f(0f, 20f, 0f));
    cam.setRotation(new Quaternion(0f, .707f, -.707f, 0f));

    // Create World
    RoboticArmUtils.createWorld(rootNode, assetManager);

    // Place target
    moveTarget();
    rootNode.attachChild(target);

    // Create robotic Arm
    rootNode.attachChild(pivots[0]);

    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        pivots[i].move(0, 0, 2 * Constants.SECTION_LENGTH);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < numJoints - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    // place Head
    headNode.move(0, 0, 2 * Constants.SECTION_LENGTH);
    headNode.attachChild(head);
    float shift = (float) Math.sqrt(Constants.HEAD_RADIUS * Constants.HEAD_RADIUS / 2.0);
    leftEye.move(-1.0f * shift, 0, shift);
    headNode.attachChild(leftEye);
    rightEye.move(shift, 0, shift);
    headNode.attachChild(rightEye);
    pivots[numJoints - 1].attachChild(headNode);

    setupKeys();

    hudDistanceText = new BitmapText(guiFont, false);
    hudDistanceText.setSize(24); // font size
    hudDistanceText.setColor(ColorRGBA.White); // font color
    hudDistanceText.setText("D="); // the text
    hudDistanceText.setLocalTranslation(10, settings.getHeight() - 10, 0); // position
    guiNode.attachChild(hudDistanceText);

    hudPositionText = new BitmapText(guiFont, false);
    hudPositionText.setSize(16); // font size
    hudPositionText.setColor(ColorRGBA.White); // font color
    hudPositionText.setLocalTranslation(10, hudPositionText.getLineHeight(), 0); // position
    guiNode.attachChild(hudPositionText);

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
      pivots[0].rotate(0, value * speed, 0);
    } else if (binding.equals("Right0")) {
      pivots[0].rotate(0, -1 * value * speed, 0);
    } else if (binding.equals("Left1")) {
      if (pivots.length > 1) {
        pivots[1].rotate(0, value * speed, 0);
      }
    } else if (binding.equals("Right1")) {
      if (pivots.length > 1) {
        pivots[1].rotate(0, -1 * value * speed, 0);
      }
    } else if (binding.equals("Left2")) {
      if (pivots.length > 2) {
        pivots[2].rotate(0, value * speed, 0);
      }
    } else if (binding.equals("Right2")) {
      if (pivots.length > 2) {
        pivots[2].rotate(0, -1 * value * speed, 0);
      }
    }
  }

  @Override
  public void simpleUpdate(float tpf) {

    // hudPositionText
    Vector3f[] relativePositions = new Vector3f[numJoints];
    for (int i = 0; i < numJoints; i++) {
      if (i == (numJoints - 1)) { // head relative to last joint
        relativePositions[numJoints - 1] = head.getWorldTranslation().subtract(joints[numJoints - 1].getWorldTranslation()).divide(2 * Constants.SECTION_LENGTH);
      } else {
        relativePositions[i] = joints[i + 1].getWorldTranslation().subtract(joints[i].getWorldTranslation()).divide(2 * Constants.SECTION_LENGTH);
      }
    }
    String positionString = "";
    for (int i = 0; i < relativePositions.length; i++) {
      positionString += relativePositions[i].toString() + " ";
    }
    hudPositionText.setText(positionString);

    // hudDistanceText
    Vector3f targetCoords = target.getWorldTranslation();
    Vector3f leftEyeCoords = leftEye.getWorldTranslation();
    float distL = leftEyeCoords.distance(targetCoords) - Constants.TARGET_RADIUS;
    Vector3f rightEyeCoords = rightEye.getWorldTranslation();
    float distR = rightEyeCoords.distance(targetCoords) - Constants.TARGET_RADIUS;
    // pin distance
    Vector3f headCoords = head.getWorldTranslation();
    float headDistance = headCoords.distance(targetCoords) - Constants.TARGET_RADIUS - Constants.HEAD_RADIUS;
    // float distAve = (distL + distR) / 2;
    hudDistanceText.setText(" DL= " + distL + " DR= " + distR + " D= " + headDistance);

    // System.out.println(cam.getLocation());
    // System.out.println(cam.getRotation());

    if (headDistance < 0.005f) {
      moveTarget();
    }

  }

  private void moveTarget() {

    float arcRadius = (float) (Math.random() * 2 * Constants.SECTION_LENGTH * numJoints + Constants.TARGET_RADIUS + Constants.HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    target.center();
    target.move(x, 0, z);
  }

  public static void main(String[] args) {

    RoboticArmV1 app = new RoboticArmV1(3);
    app.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(480, 480);
    settings.setTitle("Proprioceptron");
    app.setSettings(settings);
    app.start();
  }
}
