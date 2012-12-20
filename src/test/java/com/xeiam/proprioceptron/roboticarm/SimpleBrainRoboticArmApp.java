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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.system.AppSettings;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesLineStyle;
import com.xeiam.xchart.SwingWrapper;

/**
 * Run the Robotic Arm game with a simple test brain completing the feedback loop
 * 
 * @author timmolter
 * @create Sep 28, 2012
 */
public class SimpleBrainRoboticArmApp implements PropertyChangeListener {

  private static final int NUM_JOINTS = 2;
  private static final int START_LEVEL_ID = 0;
  private static final int NUM_TARGETS_PER_LEVEL = 2;

  private final SimpleBrain simpleBrain;
  private final RoboticArm roboticArm;

  private final List<Score> scores = new ArrayList<Score>();

  /**
   * Constructor
   */
  public SimpleBrainRoboticArmApp() {

    // disable jme3 logging
    Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

    simpleBrain = new SimpleBrain();

    roboticArm = new RoboticArm(NUM_JOINTS, START_LEVEL_ID, NUM_TARGETS_PER_LEVEL);
    roboticArm.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(480, 480);
    settings.setTitle("Proprioceptron - Simple Test Brain");
    settings.setFrameRate(60);
    roboticArm.setSettings(settings);
    roboticArm.addChangeListener(this);
    roboticArm.start();

  }

  public static void main(String[] args) {

    new SimpleBrainRoboticArmApp();
  }

  @Override
  public void propertyChange(PropertyChangeEvent pce) {

    if (pce.getPropertyName().equalsIgnoreCase("STATE_CHANGE")) {
      List<JointCommand> jointCommands = simpleBrain.update(pce);
      roboticArm.moveJoints(jointCommands);
    } else if (pce.getPropertyName().equalsIgnoreCase("LEVEL_SCORE")) {
      Score score = (Score) pce.getNewValue();
      printScores(score);
    } else if (pce.getPropertyName().equalsIgnoreCase("GAME_OVER")) {
      plotResults();
    }

  }

  private class SimpleBrain {

    private final Random random = new Random();

    /**
     * @param pce
     */
    public List<JointCommand> update(PropertyChangeEvent pce) {

      // RoboticArmGameState oldEnvState = (RoboticArmGameState) pce.getOldValue();
      RoboticArmGameState newEnvState = (RoboticArmGameState) pce.getNewValue();

      List<JointCommand> jointCommands = new ArrayList<JointCommand>();

      // simulate a pause
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      int numJoints = newEnvState.getEnvState().getRelativePositions().length;
      for (int i = 0; i < numJoints; i++) {
        jointCommands.add(new JointCommand(i, random.nextDouble() > 0.5 ? 1 : -1, random.nextInt(100)));
      }

      return jointCommands;

    }
  }

  /**
   * Prints the scores for each level
   */
  private void printScores(Score score) {

    System.out.println(score.toString());
    scores.add(score);
  }

  private void plotResults() {

    List<Chart> charts = new ArrayList<Chart>();

    Collection<Number> xData = new ArrayList<Number>();
    Collection<Number> yData = new ArrayList<Number>();

    for (Score score : scores) {
      for (int i = 0; i < score.getActivationEnergiesRequired().length; i++) {
        xData.add(score.getLevelId());
        yData.add(score.getActivationEnergiesRequired()[i]);
      }
    }

    // Create Chart
    Chart chart = new Chart(800, 400);

    // Customize Chart
    chart.setTitle("Required Activation Energy");
    chart.setXAxisTitle("Level");
    chart.setYAxisTitle("Required Activation Energy");
    chart.setLegendVisible(false);

    // Series 1
    Series series1 = chart.addSeries("requiredActivationEnergy", xData, yData);
    series1.setLineStyle(SeriesLineStyle.NONE);
    charts.add(chart);

    // ////////////////

    xData = new ArrayList<Number>();
    yData = new ArrayList<Number>();

    for (Score score : scores) {
      for (int i = 0; i < score.getTimesElapsed().length; i++) {
        xData.add(score.getLevelId());
        yData.add(score.getTimesElapsed()[i]);
      }
    }

    // Create Chart
    chart = new Chart(800, 400);

    // Customize Chart
    chart.setTitle("Elapsed Time (s)");
    chart.setXAxisTitle("Level");
    chart.setYAxisTitle("Elapsed Time");
    chart.setLegendVisible(false);

    // Series 1
    series1 = chart.addSeries("elapsedTime", xData, yData);
    series1.setLineStyle(SeriesLineStyle.NONE);
    charts.add(chart);

    new SwingWrapper(charts, 2, 1).displayChartMatrix();

  }
}