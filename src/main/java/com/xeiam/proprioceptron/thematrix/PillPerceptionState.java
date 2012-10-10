package com.xeiam.proprioceptron.thematrix;

import com.jme3.math.Vector3f;


public class PillPerceptionState {

  private final Vector3f relativePosition;
  private final float distLeftEye;
  private final float distRightEye;
  private final float distHead;
  private final boolean wasCollision;

  public PillPerceptionState(Vector3f relativePosition, float distLeftEye, float distRightEye, float distHead, boolean wasCollision) {

    this.relativePosition = relativePosition;
    this.distLeftEye = distLeftEye;
    this.distRightEye = distRightEye;
    this.distHead = distHead;
    this.wasCollision = wasCollision;
  }

  public Vector3f getRelativePosition() {

    return relativePosition;
  }

  public float getDistLeftEye() {

    return distLeftEye;
  }

  public float getDistRightEye() {

    return distRightEye;
  }

  public float getDistHead() {

    return distHead;
  }

  public boolean isWasCollision() {

    return wasCollision;
  }
}
