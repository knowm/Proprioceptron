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

import org.knowm.proprioceptron.GameState;

/**
 * Encapsulates the EnvState and the score
 *
 * @author timmolter
 * @create Sep 28, 2012
 */
public final class RoboticArmGameState implements GameState {

  private final EnvState envState;
  private final Score score;

  /**
   * Constructor
   *
   * @param roboticArmEnvState
   */
  public RoboticArmGameState(EnvState envState, Score score) {

    this.envState = envState;
    this.score = score;

  }

  public EnvState getEnvState() {

    return envState;
  }

  public Score getScore() {

    return score;
  }

}
