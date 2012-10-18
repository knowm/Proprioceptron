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


/**
 * a wrapper class for all of the variables that the the robotarm should be able to sense about a particular pill.
 * 
 * @author Moobear
 * @create Oct 12, 2012
 */
public class PillPerceptionState {

  private final float distLeftEye;
  private final float distRightEye;
  private final float distHead;
  private final boolean wasCollision;
  private final boolean isBlue;

  public PillPerceptionState(float distLeftEye, float distRightEye, float distHead, boolean isBlue, boolean wasCollision) {

    this.distLeftEye = distLeftEye;
    this.distRightEye = distRightEye;
    this.distHead = distHead;
    this.wasCollision = wasCollision;
    this.isBlue = isBlue;
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

  @Override
  public String toString() {

    return (isBlue ? "\nBluePill" : "\nRedPill") + "\nLeft eye distance: " + distLeftEye + "\nRight eye distance: " + distRightEye + "\ndistance to forehead: " + distHead + "\nCollision? " + wasCollision;
  }
}