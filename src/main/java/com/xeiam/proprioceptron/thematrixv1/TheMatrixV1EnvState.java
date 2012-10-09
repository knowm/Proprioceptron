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
package com.xeiam.proprioceptron.thematrixv1;

import com.jme3.math.Vector3f;
import com.xeiam.proprioceptron.EnvState;

/**
 * Hold the environment state of TheMatrix game
 * 
 * @author timmolter
 * @create Oct 8, 2012
 */
public final class TheMatrixV1EnvState implements EnvState {

  private final Vector3f relativePosition;
  private final float distLeftEye;
  private final float distRightEye;
  private final float distHead;
  private final boolean wasCollision;

  /**
   * Constructor
   * 
   * @param relativePosition
   * @param distLeftEye
   * @param distRightEye
   * @param distHead
   * @param wasCollision
   */
  public TheMatrixV1EnvState(Vector3f relativePosition, float distLeftEye, float distRightEye, float distHead, boolean wasCollision) {

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

  @Override
  public String toString() {

    return "TheMatrixEnvState [relativePosition=" + relativePosition + ", distLeftEye=" + distLeftEye + ", distRightEye=" + distRightEye + ", distHead=" + distHead + ", wasCollision=" + wasCollision + "]";
  }

}
