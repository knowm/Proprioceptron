/**
 * Copyright 2012 Xeiam LLC.
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
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * A level
 * 
 * @author timmolter
 * @create Oct 31, 2012
 */
public class RoboticArmLevelAppState extends MainAppState {

  private final int levelId;
  private final float pillSpeed;

  private boolean movingLeft = true;

  private Geometry bluePill;
  private Geometry redPill;

  private final float leftBounds = SECTION_LENGTH * numJoints * 2.3f;
  private final float rightBounds = -1 * SECTION_LENGTH * numJoints * 2.3f;

  /**
   * Constructor
   * 
   * @param app
   * @param numJoints
   * @param speed
   */
  public RoboticArmLevelAppState(SimpleApplication app, int levelId, int numJoints, float pillSpeed) {

    super(app, numJoints);
    this.levelId = levelId;
    this.pillSpeed = pillSpeed;
    this.score = new Score(levelId, ((RoboticArm) app).numTargetsPerLevel);
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {

    super.initialize(stateManager, app);

    // Create BluePill
    Material matTarget = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Blue);
    matTarget.setFloat("Shininess", 128); // [1,128]

    Sphere sphereTarget = new Sphere(30, 30, TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    bluePill = new Geometry("head", sphereTarget);
    bluePill.setMaterial(matTarget);

    // Create RedPill
    matTarget = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Red);
    matTarget.setFloat("Shininess", 128); // [1,128]

    sphereTarget = new Sphere(30, 30, TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    redPill = new Geometry("head", sphereTarget);
    redPill.setMaterial(matTarget);

    // Place pills
    placePills();

  }

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
    pill.move(0.5f * x, 0, 0.5f * z); // 0.5 to prevent the pill from being stuck at exactly the arm's maximum reach.
  }

  public void movePills(float tpf) {

    if (pillSpeed > 0) { // Speed set to 0, if it should be stationary.

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

    return redPill.getWorldTranslation();
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

}
