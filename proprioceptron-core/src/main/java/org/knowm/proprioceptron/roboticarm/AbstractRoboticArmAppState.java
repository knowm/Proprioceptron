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
package org.knowm.proprioceptron.roboticarm;

import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Defines the platform and the robotic arm.
 *
 * @author timmolter
 * @create Nov 1, 2012
 */
public abstract class AbstractRoboticArmAppState extends AbstractAppState implements AnalogListener, ActionListener {

  public abstract Vector3f getBluePillWorldTranslation();

  public abstract Vector3f getRedPillWorldTranslation();

  public abstract EnvState getEnvState();

  public abstract void constructArm();

  public abstract void placePills();

  public abstract void movePills(float tpf);

  public static final float SECTION_LENGTH = 1.0f;

  /** Robotic Arm */
  AbstractRoboticArmJMEApp roboticArmApp;
  public int numJoints;
  public Node[] pivots;

  /** Score */
  protected Score score;

  BitmapText hudText;

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
   * @param numJoints
   */
  public AbstractRoboticArmAppState(SimpleApplication app, int numJoints) {

    this.rootNode = app.getRootNode();
    this.viewPort = app.getViewPort();
    this.guiNode = app.getGuiNode();
    this.inputManager = app.getInputManager();
    this.stateManager = app.getStateManager();
    this.assetManager = app.getAssetManager();

    this.roboticArmApp = (AbstractRoboticArmJMEApp) app;
    this.numJoints = numJoints;
  }

  @Override
  public void initialize(AppStateManager stateManager, Application app) {

    super.initialize(stateManager, app);

    viewPort.setBackgroundColor(backgroundColor);

    // Must add a light to make the lit object visible!
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, -5, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    localRootNode.addLight(sun);

    // create floor
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setColor("Color", ColorRGBA.Gray);
    // material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    float dimension = SECTION_LENGTH * numJoints * 2.3f;
    Box floorBox = new Box(dimension, .5f, dimension);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -.5f, 0);
    floorGeometry.addControl(new RigidBodyControl(0));
    localRootNode.attachChild(floorGeometry);

    // Load the HUD
    BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24);
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(10, hudText.getLineHeight(), 0);
    hudText.setText(getHUDText());
    localGuiNode.attachChild(hudText);

  }

  public void reconstructArm() {

    localRootNode.detachChild(pivots[0]);
    constructArm();
  }

  protected void setHudText() {

    hudText.setText(getHUDText());
  }

  private String getHUDText() {

    StringBuilder sb = new StringBuilder();
    sb.append("Level = ");
    sb.append(roboticArmApp.getCurrentLevelIndex());
    sb.append("/");
    sb.append(roboticArmApp.getLevels().size() - 1);

    sb.append(", Blue Pills = ");
    sb.append(score.getPillIdCounter());
    sb.append("/");
    sb.append(roboticArmApp.getNumTargetsPerLevel());

    sb.append(", Score = ");
    sb.append(score.getScore());

    return sb.toString();
  }

  protected void setupKeys() {

    int[] keyArray = new int[] { KeyInput.KEY_P, KeyInput.KEY_L, KeyInput.KEY_O, KeyInput.KEY_K, KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_U,
        KeyInput.KEY_H, KeyInput.KEY_Y, KeyInput.KEY_G, KeyInput.KEY_T, KeyInput.KEY_F, KeyInput.KEY_R, KeyInput.KEY_D, KeyInput.KEY_E,
        KeyInput.KEY_S, KeyInput.KEY_W, KeyInput.KEY_A, };

    for (int i = 0; i < numJoints; i++) {
      if (i < 9) { // only up to 9 joints (18 keys) bounded
        inputManager.addMapping("Right_" + i, new KeyTrigger(keyArray[2 * i]));
        inputManager.addListener(this, ("Right_" + i));
        inputManager.addMapping("Left_" + i, new KeyTrigger(keyArray[2 * i + 1]));
        inputManager.addListener(this, ("Left_" + i));
      }
    }
  }

  protected void clearKeyMappings() {

    for (int i = 0; i < numJoints; i++) {
      if (i < 9) { // only up to 9 joints (18 keys) bounded
        inputManager.deleteMapping("Right_" + i);
        inputManager.deleteMapping("Left_" + i);
      }
    }
  }

  @Override
  public void onAction(String binding, boolean keyPressed, float tpf) {

    // detect when buttons were released
    if (!keyPressed) {
      roboticArmApp.setWasMovement(true);
    }
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    String[] keyCommands = binding.split("_");

    int jointNum = Integer.parseInt(keyCommands[1]);
    float direction = keyCommands[0].equals("Left") ? 1.0f : -1.0f;

    score.incActuationEnergy(1);
    pivots[jointNum].rotate(0f, direction * 3f * tpf, 0f);
  }

  /**
   * This is where JointCommands come in from an AI algorithm
   *
   * @param jointCommands
   */
  public void moveJoints(List<JointCommand> jointCommands, float tpf) {

    for (JointCommand jointCommand : jointCommands) {
      score.incActuationEnergy(jointCommand.getSteps());
      for (int i = 0; i < jointCommand.getSteps(); i++) {
        pivots[jointCommand.getJointNumber()].rotate(0f, jointCommand.getDirection() * 0.2f * tpf, 0f);
        // pivots[jointCommand.getJointNumber()].rotate(0f, jointCommand.getDirection() * 1f * tpf, 0f);
      }
    }

    onAction("", false, 0); // tell it movement is done
  }

  @Override
  public void setEnabled(boolean enabled) {

    // Pause and unpause
    super.setEnabled(enabled);
    if (enabled) {
      rootNode.attachChild(localRootNode);
      guiNode.attachChild(localGuiNode);
      viewPort.setBackgroundColor(backgroundColor);
      setupKeys();
    } else {
      rootNode.detachChild(localRootNode);
      guiNode.detachChild(localGuiNode);
      clearKeyMappings();
    }
  }

  @Override
  public void cleanup() {

    super.cleanup();
    rootNode.detachChild(localRootNode);
    guiNode.detachChild(localGuiNode);
    clearKeyMappings();
  }

}
