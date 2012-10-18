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

import com.jme3.math.Vector3f;

/**
 * A snapshot of the static information about the arm.
 * 
 * @author Zackkenyon
 * @create Oct 17, 2012
 */
public class ProprioceptionState {

  private final Vector3f[] relativePositions;

  public ProprioceptionState(Vector3f[] relativePositions) {

    this.relativePositions = relativePositions;
  }

  public Vector3f[] getRelativePositions() {

    return relativePositions;
  }

  @Override
  public String toString() {

    String s = "";
    for (Vector3f v : relativePositions) {
      s += "\n" + v.toString();
    }
    return s;
  }
}
