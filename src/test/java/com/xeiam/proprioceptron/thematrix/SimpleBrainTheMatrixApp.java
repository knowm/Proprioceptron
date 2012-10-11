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
package com.xeiam.proprioceptron.thematrix;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.xeiam.proprioceptron.thematrix.ObjectFactory.GameView;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class SimpleBrainTheMatrixApp implements PropertyChangeListener {

  private final SimpleBrain simpleBrain;
  TheMatrix theMatrix;
  /**
   * Constructor
   */
  public SimpleBrainTheMatrixApp() {

    simpleBrain = new SimpleBrain();

    theMatrix = new TheMatrix(GameView.GOD_VIEW);

    theMatrix.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(600, 480);
    settings.setTitle("The Matrix");
    theMatrix.setSettings(settings);
    theMatrix.addChangeListener(this);
    theMatrix.start();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    new SimpleBrainTheMatrixApp();
  }

  @Override
  public void propertyChange(PropertyChangeEvent pce) {

    theMatrix.addAICommands(simpleBrain.update(pce));
  }

  private class SimpleBrain {

    private final Random random = new Random();

    /**
     * @param pce
     */
    public PlayerCommand update(PropertyChangeEvent pce) {

      TheMatrixEnvState oldEnvState = (TheMatrixEnvState) pce.getOldValue();
      TheMatrixEnvState newEnvState = (TheMatrixEnvState) pce.getNewValue();

      // System.out.println(newEnvState.toString());

      return new PlayerCommand(random.nextFloat() * FastMath.PI * (random.nextBoolean() ? 1 : -1), random.nextFloat() * 10);

    }
  }

}
