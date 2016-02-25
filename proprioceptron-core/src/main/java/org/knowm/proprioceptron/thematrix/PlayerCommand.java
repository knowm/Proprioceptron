/**
 * Copyright 2015-2016 Knowm Inc. (http://knowm.org) and contributors.
 * Copyright 2012-2015 MANC LLC (http://manc.com) and contributors.
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
package org.knowm.proprioceptron.thematrix;

/**
 * A simple wrapper for the AI to send commands to the AIAppState controller. The command can be reinterpreted as adding to the position the 2-vector specified by adding the current orientation to the rotation and the length and updating the
 * orientation by the orientation.
 * 
 * @author Moobear
 * @create Oct 12, 2012
 */
public class PlayerCommand {

  float rotation;
  float forwardmotion;

  public PlayerCommand(float rotation, float forwardmotion) {

    this.rotation = rotation;
    this.forwardmotion = forwardmotion;

  }

  public float getRotation() {

    return rotation;
  }

  public float getForwardMotion() {

    return forwardmotion;
  }
}
