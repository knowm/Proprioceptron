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
package com.xeiam.proprioceptron;

/**
 * @author zackkenyon
 * @create Sep 11, 2012
 */
public class ArmState implements State {

  public String[] documentation;
  public float[] vector;

  /**
   * Constructor
   * 
   * @param arm
   */
  public ArmState(RobotArm arm) {

  }

  @Override
  public String[] VectorDoc() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public float[] toVector() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public State fromVector(float[] vec, String[] doc) {

    // TODO Auto-generated method stub
    return null;
  }

}
