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

import java.util.List;

import com.xeiam.proprioceptron.EnvState;

/**
 * @author timmolter
 * @create Sep 28, 2012
 */
public final class RoboticArmEnvState implements EnvState {

  private final List<PillPerceptionState> pills;
  private final ProprioceptionState proprioception;

  /**
   * Constructor
   * 
   */
  public RoboticArmEnvState(List<PillPerceptionState> pills, ProprioceptionState proprioception) {

    this.pills = pills;
    this.proprioception = proprioception;
  }

  @Override
  public String toString() {

    String s = "";
    for (PillPerceptionState pps : pills) {
      s += pps.toString();
    }
    s += proprioception.toString();
    return s;
  }

}
