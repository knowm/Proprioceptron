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

import java.util.List;

import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
import com.xeiam.proprioceptron.ProprioceptronApplication;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArm extends ProprioceptronApplication implements AnalogListener, ActionListener {

  private final int numJoints;

  /** prevents calculation of state when there are no arm movements */
  private boolean wasMovement = false;

  /** HUD */
  private BitmapText hudText;

  /** Arm */
  private Node[] pivots;
  private Geometry[] sections;
  private Geometry[] joints;
  private Node headNode;
  private Geometry head;
  private Geometry leftEye;
  private Geometry rightEye;

  /** target */
  private Geometry target;

  /** Score */
  private final Score score = new Score();

  /**
   * Constructor
   */
  public RoboticArm(int numJoints) {

    this.numJoints = numJoints;
  }

  @Override
  public void simpleInitApp() {

    super.simpleInitApp();

    pivots = new Node[numJoints];
    sections = new Geometry[numJoints];
    joints = new Geometry[numJoints];

    ObjectFactory.setupGameEnvironment(rootNode, assetManager, numJoints);

    // Change Camera position
    flyCam.setEnabled(false);
    cam.setLocation(new Vector3f(0f, numJoints * 6f, 0f));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);

    // Create World

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setBoolean("m_UseMaterialColors", true);
    matRoboticArm.setColor("m_Diffuse", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setColor("m_Specular", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setFloat("m_Shininess", 50); // [1,128] lower is shinier

    Material matTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Red);
    matTarget.setFloat("Shininess", 128); // [1,128]

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, 0, ObjectFactory.SECTION_LENGTH), ObjectFactory.SECTION_CROSS_DIM, ObjectFactory.SECTION_CROSS_DIM, ObjectFactory.SECTION_LENGTH);
    Sphere sphereJoint = new Sphere(20, 20, ObjectFactory.JOINT_RADIUS);
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
    Sphere sphereHead = new Sphere(20, 20, ObjectFactory.HEAD_RADIUS);
    sphereHead.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereHead);
    head = new Geometry("head", sphereHead);
    head.setMaterial(matRoboticArm);

    // Create eyes
    Sphere sphereEye = new Sphere(20, 20, ObjectFactory.EYE_RADIUS);
    leftEye = new Geometry("leftEye", sphereEye);
    leftEye.setMaterial(matRoboticArm);
    rightEye = new Geometry("rightEye", sphereEye);
    rightEye.setMaterial(matRoboticArm);

    // Create Target
    Sphere sphereTarget = new Sphere(30, 30, ObjectFactory.TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    target = new Geometry("head", sphereTarget);
    target.setMaterial(matTarget);

    // Place target
    moveTarget();
    rootNode.attachChild(target);

    // Create robotic Arm
    rootNode.attachChild(pivots[0]);

    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        pivots[i].move(0, 0, 2 * ObjectFactory.SECTION_LENGTH);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < numJoints - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    // place Head
    headNode.move(0, 0, 2 * ObjectFactory.SECTION_LENGTH);
    headNode.attachChild(head);
    float shift = (float) Math.sqrt(ObjectFactory.HEAD_RADIUS * ObjectFactory.HEAD_RADIUS / 2.0);
    leftEye.move(shift, 0, shift);
    headNode.attachChild(leftEye);
    rightEye.move(-1.0f * shift, 0, shift);
    headNode.attachChild(rightEye);
    pivots[numJoints - 1].attachChild(headNode);

    ObjectFactory.setupKeys(inputManager, this, numJoints);

    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24); // font size
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(10, settings.getHeight() - 10, 0); // position
    guiNode.attachChild(hudText);

    // init env state
    simpleUpdate(0.0f);

  }

  @Override
  public void onAction(String binding, boolean keyPressed, float tpf) {

    // detect when buttons were released
    if (!keyPressed) {
      wasMovement = true;
    }
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    String[] keyCommands = binding.split("_");

    int jointNum = Integer.parseInt(keyCommands[1]);
    float direction = keyCommands[0].equals("Left") ? 1.0f : -1.0f;

    score.incActuationEnergy(1);
    pivots[jointNum].rotate(0, direction * value * speed, 0);

  }

  @Override
  public void simpleUpdate(float tpf) {

    if (wasMovement) {

      // update old state
      oldEnvState = newEnvState;

      // hudPositionText
      Vector3f[] relativePositions = new Vector3f[numJoints];
      for (int i = 0; i < numJoints; i++) {
        if (i == (numJoints - 1)) { // head relative to last joint
          relativePositions[numJoints - 1] = head.getWorldTranslation().subtract(joints[numJoints - 1].getWorldTranslation()).divide(2 * ObjectFactory.SECTION_LENGTH);
        } else {
          relativePositions[i] = joints[i + 1].getWorldTranslation().subtract(joints[i].getWorldTranslation()).divide(2 * ObjectFactory.SECTION_LENGTH);
        }
      }

      // hudDistanceText
      Vector3f targetCoords = target.getWorldTranslation();
      Vector3f leftEyeCoords = leftEye.getWorldTranslation();
      float distL = leftEyeCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS;
      Vector3f rightEyeCoords = rightEye.getWorldTranslation();
      float distR = rightEyeCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS;
      // pin distance
      Vector3f headCoords = head.getWorldTranslation();
      float headDistance = headCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS - ObjectFactory.HEAD_RADIUS;

      boolean wasCollision = headDistance < 0.005f;
      if (wasCollision) {
        moveTarget();
        score.incNumCollisions();
        hudText.setText("Score = " + score.getScore());
      }

      EnvState roboticArmEnvState = new EnvState(distL, distR, headDistance, relativePositions, wasCollision);
      newEnvState = new RoboticArmGameState(roboticArmEnvState, score);
      wasMovement = false;

      notifyListeners();
    }

  }

  /**
   * Move the target somewhere within the radius of reach
   */
  private void moveTarget() {

    float arcRadius = (float) (Math.random() * 2 * ObjectFactory.SECTION_LENGTH * numJoints + ObjectFactory.TARGET_RADIUS + ObjectFactory.HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    target.center();
    target.move(x, 0, z);
  }

  /**
   * This is where JointCommands come in from an AI algorithm
   * 
   * @param jointCommands
   */
  public void moveJoints(List<JointCommand> jointCommands) {

    for (JointCommand jointCommand : jointCommands) {

      score.incActuationEnergy(jointCommand.getSteps());
      for (int i = 0; i < jointCommand.getSteps(); i++) {

        pivots[jointCommand.getJointNumber()].rotate(0f, jointCommand.getDirection() * .005f * speed, 0f);
      }
    }

    onAction("", false, 0); // tell it movement is done
  }

}
