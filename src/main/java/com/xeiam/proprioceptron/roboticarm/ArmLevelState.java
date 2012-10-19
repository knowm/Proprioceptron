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
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

enum PillSpeed {
  STOPPED(0), SLOW(1), FAST(2);

  public int factor;

  private PillSpeed(int x) {

    factor = x;
  }
}

public class ArmLevelState extends AbstractAppState {

  Geometry bluePill;
  Geometry redPill;
  PillSpeed speed;
  boolean hasRedPill;
  RoboticArm app;
  Vector3f blueDirection;
  Vector3f redDirection;

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
      // this code generates a random (uniform in direction) inward pointed vector. I have no idea why I wanted to do this, but I'm keeping the code here for reference
      // blueDirection = bluePill.getWorldTranslation().negate().normalize();
      // blueDirection.addLocal(Vector3f.UNIT_Z.cross(blueDirection).mult(FastMath.tan(FastMath.rand.nextFloat()*FastMath.HALF_PI)));
      // blueDirection.normalizeLocal();
      blueDirection = Vector3f.UNIT_X.mult(FastMath.tan((FastMath.rand.nextFloat() - .5f) * FastMath.HALF_PI)).add(Vector3f.UNIT_Z);
      blueDirection.normalizeLocal();
      app.getRootNode().attachChild(bluePill);
      if (hasRedPill) {
        redPill = ObjectFactory.getPill(app.getAssetManager(), ColorRGBA.Red);
        app.moveTarget(redPill);
        redDirection = Vector3f.UNIT_X.mult(FastMath.tan((FastMath.rand.nextFloat() - .5f) * FastMath.HALF_PI)).add(Vector3f.UNIT_Z);
        redDirection.normalizeLocal();
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

    movepills(tpf);
    if (app.movementOver) {
      app.score--;
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
        blueDirection = Vector3f.UNIT_Z.mult(FastMath.tan((FastMath.rand.nextFloat() - .5f) * FastMath.HALF_PI)).add(Vector3f.UNIT_X);
        blueDirection.normalizeLocal();
        app.numBluePills++;
        app.score += 10;
      }
      pills.add(new PillPerceptionState(distR, distL, headDistance, true, wasCollision));
      if (hasRedPill) {
        Vector3f redtargetCoords = redPill.getWorldTranslation();
        float reddistL = leftEyeCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS;
        float reddistR = rightEyeCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS;
        float redheadDistance = headCoords.distance(redtargetCoords) - RoboticArmConstants.TARGET_RADIUS - RoboticArmConstants.HEAD_RADIUS;

        boolean redwasCollision = headDistance < .05f;
        if (redwasCollision) {
          app.moveTarget(redPill);
          redDirection = Vector3f.UNIT_Z.mult(FastMath.tan((FastMath.rand.nextFloat() - .5f) * FastMath.HALF_PI)).add(Vector3f.UNIT_X);
          redDirection.normalizeLocal();
          app.score -= 10;
        }
        pills.add(new PillPerceptionState(reddistR, reddistL, redheadDistance, false, redwasCollision));
      }
      
      app.newEnvState = new RoboticArmEnvState(pills, proprioception);
      app.notifyListeners();
    }
  }

  public void movepills(float tpf) {

    if (speed != PillSpeed.STOPPED) {
      bluePill.move(blueDirection.mult(tpf * speed.factor));
      if (bluePill.getWorldTranslation().length() > 2 * RoboticArmConstants.SECTION_LENGTH * app.numJoints + RoboticArmConstants.TARGET_RADIUS + RoboticArmConstants.HEAD_RADIUS)
        blueDirection.negateLocal();
      if (hasRedPill) {
        redPill.move(redDirection.mult(tpf * speed.factor));
        if (redPill.getWorldTranslation().length() > 2 * RoboticArmConstants.SECTION_LENGTH * app.numJoints + RoboticArmConstants.TARGET_RADIUS + RoboticArmConstants.HEAD_RADIUS)
          redDirection.negateLocal();
      }
    }
  }
}
