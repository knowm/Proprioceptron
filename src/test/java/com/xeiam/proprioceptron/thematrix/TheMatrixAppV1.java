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
import java.util.List;
import java.util.Random;

import com.jme3.system.AppSettings;
import com.xeiam.proprioceptron.roboticarm.JointCommand;
import com.xeiam.proprioceptron.thematrixv1.ObjectFactoryV1.GameView;
import com.xeiam.proprioceptron.thematrixv1.TheMatrixV1;
import com.xeiam.proprioceptron.thematrixv1.TheMatrixV1EnvState;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class TheMatrixAppV1 implements PropertyChangeListener {

  private final SimpleBrain simpleBrain;

  /**
   * Constructor
   */
  public TheMatrixAppV1() {

    simpleBrain = new SimpleBrain();

    TheMatrixV1 theMatrix = new TheMatrixV1(GameView.GOD_VIEW);
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

    new TheMatrixAppV1();
  }

  @Override
  public void propertyChange(PropertyChangeEvent pce) {

    simpleBrain.update(pce);
  }

  private class SimpleBrain {

    private final Random random = new Random();

    /**
     * @param pce
     */
    public List<JointCommand> update(PropertyChangeEvent pce) {

      TheMatrixV1EnvState oldEnvState = (TheMatrixV1EnvState) pce.getOldValue();
      TheMatrixV1EnvState newEnvState = (TheMatrixV1EnvState) pce.getNewValue();

      // System.out.println(newEnvState.toString());

      return null;

    }
  }

}
