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

  boolean hasRedPill;
  boolean pillsmoving;

  private int direction = 1;

  private Geometry bluePill;
  private Geometry redPill;

  // private Geometry target;

  /**
   * Constructor
   * 
   * @param app
   * @param numJoints
   * @param hasRedPill
   * @param pillsmoving
   */
  public RoboticArmLevelAppState(SimpleApplication app, int numJoints, boolean hasRedPill, boolean pillsmoving) {

    super(app, numJoints);
    this.hasRedPill = hasRedPill;
    this.pillsmoving = pillsmoving;
    this.score = new Score();

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
    if (hasRedPill) {
      placePill(redPill);
    }

  }

  /**
   * Move the target somewhere within the radius of reach
   */
  public void placePill(Geometry pill) {

    float arcRadius = (float) (Math.random() * 2 * SECTION_LENGTH * numJoints + TARGET_RADIUS + HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    pill.center();
    pill.move(x, 0, z);
  }

  public void movePills(float tpf) {

    System.out.println("tpf=" + tpf);
    if (pillsmoving) {
      float z = bluePill.getWorldTranslation().z;
      float arcRadius = SECTION_LENGTH * numJoints;
      float x = bluePill.getWorldTranslation().x + direction * tpf;

      if (Math.abs(x) > 2 * arcRadius) {
        direction *= -1;
      }
      System.out.println(x);
      bluePill.center();
      bluePill.move(x, 0, z);
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
      if (hasRedPill) {
        localRootNode.attachChild(redPill);
      }
    } else {
      localRootNode.detachChild(bluePill);
      if (hasRedPill) {
        localRootNode.detachChild(redPill);
      }
    }
    super.setEnabled(enabled);
  }

}
