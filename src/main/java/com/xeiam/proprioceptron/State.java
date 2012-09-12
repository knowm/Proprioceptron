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
 * States are containers for free variables of a program. They may also specify constant values which may include states. They are also the format of the domain and the codomain of any actuator.
 * 
 * @author zackkenyon
 * @create Sep 11, 2012
 */
public interface State {

  // a way of combining the free variables of a program.
  public String[] vectorDoc();

  public FreeVar[] toVector();

  public void addVars(FreeVar[] vars);

}
