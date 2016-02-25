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
package org.knowm.proprioceptron.roboticarm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.system.AppSettings;
import com.xeiam.proprioceptron.roboticarm.JointCommand;
import com.xeiam.proprioceptron.roboticarm.RoboticArm;
import com.xeiam.proprioceptron.roboticarm.Score;

/**
 * @author timmolter
 */
public abstract class AbstractRoboticArmApp implements PropertyChangeListener {

  protected RoboticArm roboticArm;
  protected final List<Score> scores = new ArrayList<Score>();

  private int numJoints;

  /**
   * Constructor
   */
  protected void init(int numJoints) {

    this.numJoints = numJoints;

    // jme3 logging level
    Logger.getLogger("com.jme3").setLevel(Level.FINEST);

    roboticArm.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(500, 500);
    settings.setTitle("Robotic Arm App - " + numJoints + " Joints");
    roboticArm.setSettings(settings);
    roboticArm.addChangeListener(this);

  }

  @Override
  public void propertyChange(PropertyChangeEvent pce) {

    if (pce.getPropertyName().equalsIgnoreCase("STATE_CHANGE")) {
      List<JointCommand> jointCommands = getRoboticArmBrain().update(pce);
      roboticArm.moveJoints(jointCommands);
    }
    else if (pce.getPropertyName().equalsIgnoreCase("LEVEL_SCORE")) {
      Score score = (Score) pce.getNewValue();
      printScores(score);
    }
    else if (pce.getPropertyName().equalsIgnoreCase("GAME_OVER")) {

    }
  }

  /**
   * Prints the scores for each level
   */
  protected void printScores(Score score) {

    System.out.println(score.toString());
    scores.add(score);

  }

  protected abstract AbstractRoboticArmBrain getRoboticArmBrain();

}
