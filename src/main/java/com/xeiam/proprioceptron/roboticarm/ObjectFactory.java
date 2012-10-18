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
package com.xeiam.proprioceptron.roboticarm;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class ObjectFactory {

  public static Geometry getPill(AssetManager assetManager, ColorRGBA color) {

    Material matTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", color);
    matTarget.setFloat("Shininess", 128); // [1,128]

    Sphere sphere = new Sphere(20, 20, RoboticArmConstants.TARGET_RADIUS);
    sphere.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphere);
    Geometry Pill = new Geometry("pill", sphere);
    Pill.setMaterial(matTarget);

    return Pill;
  }

  public static void makeWorld(Node rootNode, AssetManager assetManager, int numJoints, Application app) {

    RoboticArm arm = (RoboticArm) app;
    // Create World
    /** Must add a light to make the lit object visible! */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, -5, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    // floor
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));

    float dimension = RoboticArmConstants.SECTION_LENGTH * numJoints * 2.3f;
    Box floorBox = new Box(dimension, .5f, dimension);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -1.0f * RoboticArmConstants.HEAD_RADIUS - .5f, 0);
    floorGeometry.addControl(new RigidBodyControl(0));
    rootNode.attachChild(floorGeometry);

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setBoolean("m_UseMaterialColors", true);
    matRoboticArm.setColor("m_Diffuse", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setColor("m_Specular", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setFloat("m_Shininess", 50); // [1,128] lower is shinier

    Box box = new Box(new Vector3f(0, 0, RoboticArmConstants.SECTION_LENGTH), RoboticArmConstants.SECTION_CROSS_DIM, RoboticArmConstants.SECTION_CROSS_DIM, RoboticArmConstants.SECTION_LENGTH);
    Sphere sphereJoint = new Sphere(20, 20, RoboticArmConstants.JOINT_RADIUS);
    sphereJoint.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereJoint);

    for (int i = 0; i < numJoints; i++) {

      // Create pivots
      Node pivot = new Node("pivot");
      arm.pivots[i] = pivot;

      // Create sections
      Geometry section = new Geometry("Box", box);
      section.setMaterial(matRoboticArm);
      arm.sections[i] = section;

      // create joints
      Geometry sphere = new Geometry("joint", sphereJoint);
      sphere.setMaterial(matRoboticArm);
      arm.joints[i] = sphere;

    }

    // Create Head
    arm.headNode = new Node("headNode");
    Sphere sphereHead = new Sphere(20, 20, RoboticArmConstants.HEAD_RADIUS);
    sphereHead.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereHead);
    arm.head = new Geometry("head", sphereHead);
    arm.head.setMaterial(matRoboticArm);

    // Create eyes
    Sphere sphereEye = new Sphere(20, 20, RoboticArmConstants.EYE_RADIUS);
    arm.leftEye = new Geometry("leftEye", sphereEye);
    arm.leftEye.setMaterial(matRoboticArm);
    arm.rightEye = new Geometry("rightEye", sphereEye);
    arm.rightEye.setMaterial(matRoboticArm);

    rootNode.attachChild(arm.pivots[0]);
    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        arm.pivots[i].move(0, 0, 2 * RoboticArmConstants.SECTION_LENGTH);
      }

      arm.pivots[i].attachChild(arm.sections[i]);
      arm.pivots[i].attachChild(arm.joints[i]);
      if (i < numJoints - 1) {
        arm.pivots[i].attachChild(arm.pivots[i + 1]);
      }
    }
    // place Head
    arm.headNode.move(0, 0, 2 * RoboticArmConstants.SECTION_LENGTH);
    arm.headNode.attachChild(arm.head);
    float shift = (float) Math.sqrt(RoboticArmConstants.HEAD_RADIUS * RoboticArmConstants.HEAD_RADIUS / 2.0);
    arm.leftEye.move(shift, 0, shift);
    arm.headNode.attachChild(arm.leftEye);
    arm.rightEye.move(-1.0f * shift, 0, shift);
    arm.headNode.attachChild(arm.rightEye);
    arm.pivots[numJoints - 1].attachChild(arm.headNode);

  }

}
