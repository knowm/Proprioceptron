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

import java.util.ArrayList;
import java.util.List;

/**
 * @author timmolter
 * @create Sep 20, 2012
 */
public class ActuationSequence {

  private List<ActuationCommand> actuationCommands;

  /**
   * Constructor
   */
  public ActuationSequence() {

    actuationCommands = new ArrayList<ActuationCommand>();
  }

  /**
   * Add an ActuationCommand to the ActuationSequence
   * 
   * @param actuationCommand
   */
  public void addActuationCommand(ActuationCommand actuationCommand) {

    actuationCommands.add(actuationCommand);
  }

  /**
   * Add a List of ActuationCommand to the ActuationSequence
   * 
   * @param actuationCommands
   */
  public void addActuationCommands(List<ActuationCommand> actuationCommands) {

    actuationCommands.addAll(actuationCommands);
  }

  /**
   * @return the actuationCommands
   */
  public List<ActuationCommand> getActuationCommands() {

    return actuationCommands;
  }

}
