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

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

enum PillSpeed {
  STOPPED, SLOW, FAST
}

public class ArmLevelState extends AbstractAppState {

  Geometry bluePill;
  Geometry redPill;
  PillSpeed speed;
  boolean hasRedPill;
  RoboticArm app;

  public ArmLevelState(PillSpeed speed, boolean hasRedPill) {

    this.speed = speed;
    this.hasRedPill = hasRedPill;
  }

  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (RoboticArm) app;

  }

  @Override
  public void setEnabled(boolean enabled) {

    if (enabled) {
      bluePill = ObjectFactory.getPill(app.getAssetManager(), ColorRGBA.Blue);
      app.moveTarget(bluePill);
      app.getRootNode().attachChild(bluePill);
      if (hasRedPill) {
        redPill = ObjectFactory.getPill(app.getAssetManager(), ColorRGBA.Red);
        app.moveTarget(redPill);
        app.getRootNode().attachChild(redPill);
      }

    } else {
      app.getRootNode().detachChild(bluePill);
      if (hasRedPill) {
        app.getRootNode().detachChild(redPill);
      }
    }

  }

  @Override
  public void update(float tpf) {

    if (app.movementOver) {

      // update old state
      app.oldEnvState = app.newEnvState;

      //calculates proprioception.
      Vector3f[] relativePositions = new Vector3f[app.numJoints];
      for (int i = 0; i < app.numJoints; i++) {
        if (i == (app.numJoints - 1)) { // head relative to last joint
          relativePositions[app.numJoints - 1] = app.head.getWorldTranslation().subtract(app.joints[app.numJoints - 1].getWorldTranslation());
        } else {
          relativePositions[i] = app.joints[i + 1].getWorldTranslation().subtract(app.joints[i].getWorldTranslation());
        }
      }
      ProprioceptionState proprioception = new ProprioceptionState(relativePositions);

      //calculates pill perception.
      List<PillPerceptionState> pills = new ArrayList<PillPerceptionState>();
      Vector3f targetCoords = bluePill.getWorldTranslation();
      Vector3f leftEyeCoords = app.leftEye.getWorldTranslation();
      float distL = leftEyeCoords.distance(targetCoords) - RoboticArmConstants.TARGET_RADIUS;
      Vector3f rightEyeCoords = app.rightEye.getWorldTranslation();
      float distR = rightEyeCoords.distance(targetCoords) - RoboticArmConstants.TARGET_RADIUS;
      Vector3f headCoords = app.head.getWorldTranslation();
      float headDistance = headCoords.distance(targetCoords) - RoboticArmConstants.TARGET_RADIUS - RoboticArmConstants.HEAD_RADIUS;

      boolean wasCollision = headDistance < .05f;
      if (wasCollision) {
        app.moveTarget(bluePill);
        app.numBluePills++;
        app.score += 10;
      }
      pills.add(new PillPerceptionState(distR, distL, headDistance, true, wasCollision));
      if (hasRedPill) {
        Vector3f redtargetCoords = bluePill.getWorldTranslation();
        float reddistL = leftEyeCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS;
        float reddistR = rightEyeCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS;
        float redheadDistance = headCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS - RoboticArmConstants.HEAD_RADIUS;

        boolean redwasCollision = headDistance < .05f;
        if (redwasCollision) {
          app.moveTarget(redPill);
          app.score -= 10;
        }
        pills.add(new PillPerceptionState(reddistR, reddistL, redheadDistance, false, redwasCollision));
      }
      
      app.newEnvState = new RoboticArmEnvState(pills, proprioception);
      app.notifyListeners();
    }
  }

  public void movepills(float tpf) {

  }
}
