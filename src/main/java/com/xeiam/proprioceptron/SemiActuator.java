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
abstract class SemiActuator extends Actuator {

  int[] branch;

  // specifies a branch, for example, the number of times a wheel has spun, this allows SemiActuators to be treated as injective.
  public SemiActuator(State domain, State codomain, int[] branch) {

    super(domain, codomain);
    this.branch = branch;
  }
}