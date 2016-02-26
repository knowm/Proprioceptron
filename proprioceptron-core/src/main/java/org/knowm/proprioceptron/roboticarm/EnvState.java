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
package org.knowm.proprioceptron.roboticarm;

import java.util.Arrays;

import com.jme3.math.Vector3f;

/**
 * Stores environment state
 *
 * @author timmolter
 * @create Sep 28, 2012
 */
public final class EnvState {

  private final float distLeftEye;
  private final float distRightEye;
  private final float distHead;

  private final Vector3f[] relativePositions;

  private final boolean wasCollision;

  /**
   * Constructor
   *
   * @param distLeftEye
   * @param distRightEye
   * @param distHead
   * @param relativePositions
   * @param wasCollision
   */
  public EnvState(float distLeftEye, float distRightEye, float distHead, Vector3f[] relativePositions, boolean wasCollision) {

    this.distLeftEye = distLeftEye;
    this.distRightEye = distRightEye;
    this.distHead = distHead;
    this.relativePositions = relativePositions;
    this.wasCollision = wasCollision;
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

  public Vector3f[] getRelativePositions() {

    return relativePositions;
  }

  /**
   * @return the wasCollision
   */
  public boolean wasCollision() {

    return wasCollision;
  }

  @Override
  public String toString() {

    return "EnvState [distLeftEye=" + distLeftEye + ", distRightEye=" + distRightEye + ", distHead=" + distHead + ", relativePositions="
        + Arrays.toString(relativePositions) + ", wasCollision=" + wasCollision + "]";
  }

}
