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

import java.util.ArrayList;
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
  private final int numTargetsPerLevel;

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

  /** Score */
  private final Score score = new Score();

  /** Levels */
  private List<RoboticArmLevelAppState> levels;
  private RoboticArmLevelAppState currentLevelAppState;
  private int currentLevelIndex = 0;

  /**
   * Constructor
   */
  public RoboticArm(int numJoints, int numTargetsPerLevel) {

    this.numJoints = numJoints;
    this.numTargetsPerLevel = numTargetsPerLevel;
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

    levels = new ArrayList<RoboticArmLevelAppState>();
    levels.add(new RoboticArmLevelAppState(numJoints, 1, 0, false));
    levels.add(new RoboticArmLevelAppState(numJoints, 1, 0, false));
    for (RoboticArmLevelAppState level : levels) {
      level.initialize(getStateManager(), this);
    }
    currentLevelAppState = levels.get(currentLevelIndex);
    currentLevelAppState.setEnabled(true);

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

      wasMovement = false;

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

      // envstate
      Vector3f targetCoords = currentLevelAppState.getTargetWorldTranslation();
      Vector3f leftEyeCoords = leftEye.getWorldTranslation();
      float distL = leftEyeCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS;
      Vector3f rightEyeCoords = rightEye.getWorldTranslation();
      float distR = rightEyeCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS;
      Vector3f headCoords = head.getWorldTranslation();
      float headDistance = headCoords.distance(targetCoords) - ObjectFactory.TARGET_RADIUS - ObjectFactory.HEAD_RADIUS;
      boolean wasCollision = headDistance < 0.005f;
      System.out.println(headDistance);
      if (wasCollision) {
        score.incNumCollisions();
        if (score.getNumCollisions() % numTargetsPerLevel == 0) {
          currentLevelAppState.setEnabled(false);
          currentLevelIndex++;
          if (currentLevelIndex >= levels.size()) { // game over
            hudText.setText("GAME OVER");
            System.out.println("GAME OVER");
            // TODO disable game
          } else {
            currentLevelAppState = levels.get(currentLevelIndex);
            currentLevelAppState.setEnabled(true);
          }
        } else {
          currentLevelAppState.moveTarget();
        }
        hudText.setText("Level = " + currentLevelIndex + ", Score = " + score.getScore());
      }
      EnvState roboticArmEnvState = new EnvState(distL, distR, headDistance, relativePositions, wasCollision);
      newEnvState = new RoboticArmGameState(roboticArmEnvState, score);

      notifyListeners();
    }

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
