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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.xeiam.proprioceptron.actuators.Actuator;

/**
 * @author Zackkenyon
 * @create Sep 20, 2012
 */
public class ActuationHandler {

  private final Queue<ActuationCommand> actuationCommands;
  private final CycleQueue<Actuator> actuators;

  /**
   * Constructor
   */
  public ActuationHandler() {

    actuationCommands = new LinkedList<ActuationCommand>();
    actuators = new CycleQueue<Actuator>();

  }

  /**
   * Add an ActuationCommand to the ActuationSequence
   * 
   * @param actuationCommand
   */
  public void addActuationCommand(ActuationCommand actuationCommand) {

    actuationCommands.add(actuationCommand);
  }

  public void addActuator(Actuator actuator, boolean isstable) {

    actuators.add(actuator, isstable);
  }

  public void addActuator(Actuator actuator) {

    actuators.add(actuator, false);
  }

  public void initialize() {

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
   * @return the next actuator in the loop.
   */
  public Actuator getNextActuator() {

    if (actuationCommands.peek() != null && actuators.isStable())
      return actuationCommands.poll();
    else
      return actuators.next();
  }

}
