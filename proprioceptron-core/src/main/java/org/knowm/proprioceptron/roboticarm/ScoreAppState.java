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

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * @author timmolter
 * @create Nov 1, 2012
 */
public class ScoreAppState extends AbstractAppState {

  BitmapText hudText;
  RoboticArm roboticArmApp;

  /** JME */
  protected ViewPort viewPort;
  protected Node rootNode;
  protected Node guiNode;
  protected InputManager inputManager;
  protected AppStateManager stateManager;
  protected AssetManager assetManager;
  protected Node localRootNode = new Node();
  protected Node localGuiNode = new Node();
  protected final ColorRGBA backgroundColor = ColorRGBA.DarkGray;

  /**
   * Constructor
   * 
   * @param app
   */
  public ScoreAppState(SimpleApplication app) {

    this.rootNode = app.getRootNode();
    this.viewPort = app.getViewPort();
    this.guiNode = app.getGuiNode();
    this.inputManager = app.getInputManager();
    this.stateManager = app.getStateManager();
    this.assetManager = app.getAssetManager();

    this.roboticArmApp = (RoboticArm) app;
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {

    super.initialize(stateManager, app);

    viewPort.setBackgroundColor(backgroundColor);

    // Load the HUD
    BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24);
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(10, app.getContext().getSettings().getHeight(), 0);
    hudText.setText(getHUDText());
    localGuiNode.attachChild(hudText);
  }

  private String getHUDText() {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < roboticArmApp.levels.size(); i++) {
      RoboticArmLevelAppState roboticArmLevelAppState = roboticArmApp.levels.get(i);
      sb.append("Level " + i + ": ");
      sb.append(roboticArmLevelAppState.score.getScore() + "\n");
    }
    sb.append("\n");
    if (roboticArmApp.gameOver) {
      sb.append("Game Over.");
    } else {
      sb.append("Press space bar to continue...");
    }
    return sb.toString();
  }

  @Override
  public void setEnabled(boolean enabled) {

    // Pause and unpause
    super.setEnabled(enabled);

    if (enabled) {
      rootNode.attachChild(localRootNode);
      guiNode.attachChild(localGuiNode);
      hudText.setText(getHUDText());
    } else {
      rootNode.detachChild(localRootNode);
      guiNode.detachChild(localGuiNode);
    }
  }

  @Override
  public void cleanup() {

    super.cleanup();
    rootNode.detachChild(localRootNode);
    guiNode.detachChild(localGuiNode);
  }

}
