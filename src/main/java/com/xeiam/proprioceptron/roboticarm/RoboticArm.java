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

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.FlyCamAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.xeiam.proprioceptron.ProprioceptronApplication;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArm extends ProprioceptronApplication implements ActionListener {

  final int numJoints;

  /** prevents calculation of state when there are no arm movements */
  boolean movementOver = true;
  boolean isWaiting = true;
  private final PlayerAppState currentPlayer;
  BitmapText hudText;
  // BitmapText hudPositionText;
  List<ArmLevelState> levels;
  ArmLevelState currentLevel;
  int currentlevelindex = 0;
  int numBluePills = 0;
  protected Node[] pivots;
  protected Geometry[] sections;
  protected Geometry[] joints;
  Node headNode;
  Geometry head;
  Geometry leftEye;
  Geometry rightEye;

  int score = 0;
  /**
   * Constructor
   */
  public RoboticArm(int numJoints, PlayerAppState currentPlayer) {

    levels = new ArrayList<ArmLevelState>();
    levels.add(new ArmLevelState(PillSpeed.STOPPED, false));
    levels.add(new ArmLevelState(PillSpeed.SLOW, false));
    levels.add(new ArmLevelState(PillSpeed.FAST, false));
    levels.add(new ArmLevelState(PillSpeed.STOPPED, true));
    levels.add(new ArmLevelState(PillSpeed.SLOW, true));
    levels.add(new ArmLevelState(PillSpeed.FAST, true));
    currentLevel = levels.get(0);
    this.currentPlayer = currentPlayer;
    this.numJoints = numJoints;
  }

  @Override
  public Node getRootNode() {

    return rootNode;
  }
  @Override
  public void simpleInitApp() {

    super.simpleInitApp();
    currentPlayer.initialize(stateManager, this);
    for (ArmLevelState level : levels) {
      level.initialize(stateManager, this);
    }
    currentLevel.setEnabled(true);
    stateManager.detach(stateManager.getState(FlyCamAppState.class));
    pivots = new Node[numJoints];
    sections = new Geometry[numJoints];
    joints = new Geometry[numJoints];
    ObjectFactory.makeWorld(rootNode, assetManager, numJoints, this);

    // Change Camera position
    cam.setLocation(new Vector3f(0f, numJoints * 6f, 0f));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z);


    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24); // font size
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(0, settings.getHeight(), 0); // position
    guiNode.attachChild(hudText);


    // init env state
    // newEnvState = new RoboticArmEnvState(new ArrayList<PillPerceptionState>(),new ProprioceptionState(new Vector3f[]{Vector3f.ZERO}));

  }

  private void setcurrentlevel(int index) {

    currentLevel.setEnabled(false);
    currentLevel = levels.get(index);
    currentLevel.setEnabled(true);
    currentlevelindex = index;
  }
  @Override
  public void simpleRender(RenderManager rm) {

    if (newEnvState != null)
      setHudText(newEnvState.toString() + "\nBlue pills collected: " + numBluePills + "\nscore: " + score);

  }
  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    currentPlayer.onAction(name, keyPressed, tpf);


  }

  private void setHudText(String s) {
    hudText.setText(s);
  }
  @Override
  public void simpleUpdate(float tpf) {

    currentPlayer.update(tpf);
    currentLevel.update(tpf);
    if (numBluePills > RoboticArmConstants.PILLS_PER_LEVEL * (currentlevelindex + 1)) {
      setcurrentlevel(currentlevelindex + 1);
    }
  }

  public void moveTarget(Geometry target) {

    float arcRadius = (float) (Math.random() * 2 * RoboticArmConstants.SECTION_LENGTH * numJoints + RoboticArmConstants.TARGET_RADIUS + RoboticArmConstants.HEAD_RADIUS);
    float x = (float) (Math.random() * arcRadius * (Math.random() > 0.5 ? 1 : -1));
    float z = (float) (Math.sqrt(arcRadius * arcRadius - x * x)) * (Math.random() > 0.5 ? 1 : -1);
    target.center();
    target.move(x, 0, z);
  }

  public void pushCommandsToAI(List<JointCommand> jointCommands) {

    ((AIPlayerState) currentPlayer).pushCommands(jointCommands);

  }

}
