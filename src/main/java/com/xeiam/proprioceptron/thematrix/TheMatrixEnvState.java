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
package com.xeiam.proprioceptron.thematrix;

import java.util.List;

import com.xeiam.proprioceptron.EnvState;

/**
 * Hold the environment state of TheMatrix game
 * 
 * @author timmolter
 * @create Oct 8, 2012
 */
public final class TheMatrixEnvState implements EnvState {

  private final List<PillPerceptionState> perception;

  public TheMatrixEnvState(List<PillPerceptionState> snapshot) {

    perception = snapshot;
  }

  public List<PillPerceptionState> getPerception() {

    return perception;
  }

  /**
   * returns a formatted concatenated string from each of the PillPerceptionState toString() methods.
   */
  @Override
  public String toString() {

    String result = "";
    for (PillPerceptionState pps : perception)
      result += pps.toString();
    return result;
  }
}
