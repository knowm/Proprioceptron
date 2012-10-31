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
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * @author timmolter
 * @create Oct 31, 2012
 */
public class RoboticArmLevelAppState extends AbstractAppState {

  int numJoints;
  int numbluepills;
  int numredpills;
  boolean pillsmoving;

  private RoboticArm app;
  private Geometry target;

  /**
   * Constructor
   * 
   * @param numbluepills
   * @param numredpills
   * @param pillsmoving
   */
  public RoboticArmLevelAppState(int numJoints, int numbluepills, int numredpills, boolean pillsmoving) {

    this.numJoints = numJoints;
    this.numbluepills = numbluepills;
    this.numredpills = numredpills;
    this.pillsmoving = pillsmoving;
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {

    super.initialize(stateManager, app);
    this.app = (RoboticArm) app;

    // Create Target
    Material matTarget = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("m_Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", ColorRGBA.Red);
    matTarget.setFloat("Shininess", 128); // [1,128]

    Sphere sphereTarget = new Sphere(30, 30, ObjectFactory.TARGET_RADIUS);
    sphereTarget.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereTarget);
    target = new Geometry("head", sphereTarget);
    target.setMaterial(matTarget);

    // Place target
    moveTarget();
  }

  /**
   * Move the target somewhere within the radius of reach
   */
  public void moveTarget() {

    float arcRadius = (float) (Math.random() * 2 * ObjectFactory.SECTION_LENGTH * numJoints + ObjectFactory.TARGET_RADIUS + ObjectFactory.HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    target.center();
    target.move(x, 0, z);
  }

  public Vector3f getTargetWorldTranslation() {

    return target.getWorldTranslation();
  }

  @Override
  public void setEnabled(boolean enabled) {

    if (enabled) {
      app.getRootNode().attachChild(target);
    } else {
      app.getRootNode().detachChild(target);
    }
  }

}
