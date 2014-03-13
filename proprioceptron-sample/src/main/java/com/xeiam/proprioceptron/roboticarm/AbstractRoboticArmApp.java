/**
 * Copyright (c) 2013 M. Alexander Nugent Consulting <i@alexnugent.name>
 *
 * M. Alexander Nugent Consulting Research License Agreement
 * Non-Commercial Academic Use Only
 *
 * This Software is proprietary. By installing, copying, or otherwise using this
 * Software, you agree to be bound by the terms of this license. If you do not agree,
 * do not install, copy, or use the Software. The Software is protected by copyright
 * and other intellectual property laws.
 *
 * You may use the Software for non-commercial academic purpose, subject to the following
 * restrictions. You may copy and use the Software for peer-review and methods verification
 * only. You may not create derivative works of the Software. You may not use or distribute
 * the Software or any derivative works in any form for commercial or non-commercial purposes.
 *
 * Violators will be prosecuted to the full extent of the law.
 *
 * All rights reserved. No warranty, explicit or implicit, provided.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRßANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.proprioceptron.roboticarm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.system.AppSettings;

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
