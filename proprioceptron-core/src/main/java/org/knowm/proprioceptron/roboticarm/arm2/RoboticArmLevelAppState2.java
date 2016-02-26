/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://alexnugentconsulting.com/) and contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.knowm.proprioceptron.roboticarm.arm2;

import org.knowm.proprioceptron.roboticarm.AbstractRoboticArmAppState;
import org.knowm.proprioceptron.roboticarm.AbstractRoboticArmJMEApp;
import org.knowm.proprioceptron.roboticarm.EnvState;
import org.knowm.proprioceptron.roboticarm.Score;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * A level
 *
 * @author timmolter
 * @create Oct 31, 2012
 */
public class RoboticArmLevelAppState2 extends AbstractRoboticArmAppState {

  private final int levelId;

  public static final float JOINT_RADIUS = 0.3f;
  public static final float HEAD_RADIUS = 1.3f;
  public static final float EYE_RADIUS = 0.1f;
  public static final float TARGET_RADIUS = 1.3f;

  public static final float SECTION_CROSS_DIM = 0.1f;

  /** Arm */
  private Geometry[] sections;
  private Geometry[] joints;
  private Node headNode;
  private Geometry head;

  /** Pills */
  private Geometry bluePill;
  //  private Geometry redPill;
  private boolean movingLeft = true;

  private final float pillSpeed;
  private final float leftBounds;
  private final float rightBounds;
  private final float radius;

  /**
   * Constructor
   *
   * @param app
   * @param levelId
   * @param numJoints
   * @param pillSpeed
   */
  public RoboticArmLevelAppState2(SimpleApplication app, int levelId, int numJoints, float pillSpeed) {

    super(app, numJoints);
    this.levelId = levelId;
    this.pillSpeed = pillSpeed;
    this.score = new Score(levelId, ((AbstractRoboticArmJMEApp) app).getNumTargetsPerLevel());

    leftBounds = SECTION_LENGTH * numJoints * 2.3f;
    rightBounds = -1 * SECTION_LENGTH * numJoints * 2.3f;
    radius = .65f;
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {

    super.initialize(stateManager, app);

    // Arm

    pivots = new Node[numJoints];
    sections = new Geometry[numJoints];
    joints = new Geometry[numJoints];
    // Create robotic Arm
    constructArm();

    // Create BluePill
    Material matTarget = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Blue);
    matTarget.setFloat("Shininess", 128); // [1,128]

    Sphere sphereTarget = new Sphere(30, 30, TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    bluePill = new Geometry("head", sphereTarget);
    bluePill.setMaterial(matTarget);

    //    // Create RedPill
    //    matTarget = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    //    matTarget.setBoolean("UseMaterialColors", true);
    //    matTarget.setColor("Specular", ColorRGBA.White);
    //    matTarget.setColor("Diffuse", ColorRGBA.Red);
    //    matTarget.setFloat("Shininess", 128); // [1,128]
    //
    //    sphereTarget = new Sphere(30, 30, TARGET_RADIUS);
    //    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    //    TangentBinormalGenerator.generate(sphereTarget);
    //    redPill = new Geometry("head", sphereTarget);
    //    redPill.setMaterial(matTarget);

    // Place pills
    placePills();

  }

  @Override
  public void constructArm() {

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setBoolean("UseMaterialColors", true);
    matRoboticArm.setColor("Diffuse", new ColorRGBA(.1f, 1.0f, .7f, 1f));
    matRoboticArm.setColor("Specular", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setFloat("Shininess", 50); // [1,128] lower is shinier

    Material matRoboticArmHead = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArmHead.setBoolean("UseMaterialColors", true);
    matRoboticArmHead.setColor("Diffuse", new ColorRGBA(1f, 1.0f, .7f, 1f));
    matRoboticArmHead.setColor("Specular", new ColorRGBA(1f, 1.0f, .7f, 1f));
    matRoboticArmHead.setFloat("Shininess", 50); // [1,128] lower is shinier

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, 0, SECTION_LENGTH), SECTION_CROSS_DIM, SECTION_CROSS_DIM, SECTION_LENGTH);
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
    head.setMaterial(matRoboticArmHead);

    localRootNode.attachChild(pivots[0]);
    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        pivots[i].move(0, 0, 2 * SECTION_LENGTH);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < numJoints - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    // place Head
    headNode.move(0, 0, 2 * SECTION_LENGTH);
    headNode.attachChild(head);
    float shift = (float) Math.sqrt(HEAD_RADIUS * HEAD_RADIUS / 2.0);
    pivots[numJoints - 1].attachChild(headNode);
  }

  @Override
  public void placePills() {

    placePill(bluePill);
  }

  /**
   * Move the target somewhere within the radius of reach
   */
  public void placePill(Geometry pill) {

    float arcRadius = (float) (Math.random() * 2 * SECTION_LENGTH * numJoints + TARGET_RADIUS + HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    pill.center();
    pill.move(radius * x, 0, radius * z); // some radius to prevent the pill from being stuck at exactly the arm's maximum reach.
  }

  @Override
  public void movePills(float tpf) {

    if (pillSpeed > 0.0f) { // Speed set to 0, if it should be stationary.

      // blue pill
      float z = bluePill.getWorldTranslation().z;
      float x = bluePill.getWorldTranslation().x;
      if (movingLeft && x > leftBounds) {
        movingLeft = false;
      } else if (!movingLeft && x < rightBounds) {
        movingLeft = true;
      }
      bluePill.center();
      bluePill.move(x + (movingLeft ? 1 : -1) * tpf * pillSpeed, 0, z);

    }
  }

  @Override
  public Vector3f getBluePillWorldTranslation() {

    return bluePill.getWorldTranslation();
  }

  @Override
  public Vector3f getRedPillWorldTranslation() {

    return null;
  }

  @Override
  public void setEnabled(boolean enabled) {

    if (enabled) {
      localRootNode.attachChild(bluePill);

    } else {
      localRootNode.detachChild(bluePill);

    }
    super.setEnabled(enabled);
  }

  @Override
  public EnvState getEnvState() {

    Vector3f[] relativePositions = new Vector3f[numJoints];
    for (int i = 0; i < numJoints; i++) {
      if (i == (numJoints - 1)) { // head relative to last joint
        relativePositions[numJoints - 1] = head.getWorldTranslation().subtract(joints[numJoints - 1].getWorldTranslation())
            .divide(2 * SECTION_LENGTH);
      } else {
        relativePositions[i] = joints[i + 1].getWorldTranslation().subtract(joints[i].getWorldTranslation()).divide(2 * SECTION_LENGTH);
      }
    }

    // env state - target
    Vector3f targetCoords = getBluePillWorldTranslation();
    float distL = 0.0f;
    float distR = 0.0f;
    Vector3f headCoords = head.getWorldTranslation();
    float headDistance = headCoords.distance(targetCoords);
    boolean wasCollision = headDistance < 0.1f;

    EnvState roboticArmEnvState = new EnvState(headCoords, targetCoords, distL, distR, headDistance, relativePositions, wasCollision);
    return roboticArmEnvState;
  }
}
