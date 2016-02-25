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
package org.knowm.proprioceptron.thematrix;

import com.jme3.system.AppSettings;
import com.xeiam.proprioceptron.thematrix.HumanAppState;
import com.xeiam.proprioceptron.thematrix.TheMatrix;
import com.xeiam.proprioceptron.thematrix.ObjectFactory.GameView;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class HumanBrainTheMatrixApp {

  /**
   * Constructor
   */
  public HumanBrainTheMatrixApp() {

    // switching game logic between human and AI versions is too complicated to manage without the AIAppState and HumanAppState delegates.

    TheMatrix theMatrix = new TheMatrix(GameView.GOD_VIEW, new HumanAppState());
    theMatrix.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1300, 700);
    settings.setTitle("The Matrix");
    theMatrix.setSettings(settings);
    theMatrix.start();
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    new HumanBrainTheMatrixApp();
  }

}
