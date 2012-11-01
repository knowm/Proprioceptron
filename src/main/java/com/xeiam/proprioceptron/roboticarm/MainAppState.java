/**
 * Copyright 2012 Xeiam LLC.
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
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * Defines the platform and the robotic arm.
 * 
 * @author timmolter
 * @create Nov 1, 2012
 */
public abstract class MainAppState extends AbstractAppState implements AnalogListener, ActionListener {

  public static final float JOINT_RADIUS = 0.3f;
  public static final float HEAD_RADIUS = 0.3f;
  public static final float EYE_RADIUS = 0.1f;
  public static final float TARGET_RADIUS = 0.3f;

  public static final float SECTION_LENGTH = 1.0f;
  public static final float SECTION_CROSS_DIM = 0.1f;

  /** Robotic Arm */
  RoboticArm roboticArmApp;
  int numJoints;

  /** Arm */
  private Node[] pivots;
  private Geometry[] sections;
  private Geometry[] joints;
  private Node headNode;
  private Geometry head;
  private Geometry leftEye;
  private Geometry rightEye;

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
   */
  public MainAppState(SimpleApplication app, int numJoints) {

    this.rootNode = app.getRootNode();
    this.viewPort = app.getViewPort();
    this.guiNode = app.getGuiNode();
    this.inputManager = app.getInputManager();
    this.stateManager = app.getStateManager();
    this.assetManager = app.getAssetManager();

    this.roboticArmApp = (RoboticArm) app;
    this.numJoints = numJoints;
  }

  public abstract Vector3f getBluePillWorldTranslation();

  public abstract Vector3f getRedPillWorldTranslation();

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
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    float dimension = SECTION_LENGTH * numJoints * 2.3f;
    Box floorBox = new Box(dimension, .5f, dimension);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -1.0f * HEAD_RADIUS - .5f, 0);
    floorGeometry.addControl(new RigidBodyControl(0));
    localRootNode.attachChild(floorGeometry);

    pivots = new Node[numJoints];
    sections = new Geometry[numJoints];
    joints = new Geometry[numJoints];

    // Material for Robotic Arm
    Material matRoboticArm = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matRoboticArm.setBoolean("m_UseMaterialColors", true);
    matRoboticArm.setColor("m_Diffuse", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setColor("m_Specular", new ColorRGBA(.7f, 1.0f, .7f, 1f));
    matRoboticArm.setFloat("m_Shininess", 50); // [1,128] lower is shinier

    // elongated box for arm sections
    Box box = new Box(new Vector3f(0, 0, SECTION_LENGTH), SECTION_CROSS_DIM, SECTION_CROSS_DIM, SECTION_LENGTH);
    Sphere sphereJoint = new Sphere(20, 20, JOINT_RADIUS);
    sphereJoint.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereJoint);

    for (int i = 0; i < numJoints; i++) {

      // Create pivots
      Node pivot = new Node("pivot");
      pivots[i] = pivot;

      // Create sections
      Geometry section = new Geometry("Box", box);
      section.setMaterial(matRoboticArm);
      sections[i] = section;

      // create joints
      Geometry sphere = new Geometry("joint", sphereJoint);
      sphere.setMaterial(matRoboticArm);
      joints[i] = sphere;

    }

    // Create Head
    headNode = new Node("headNode");
    Sphere sphereHead = new Sphere(20, 20, HEAD_RADIUS);
    sphereHead.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphereHead);
    head = new Geometry("head", sphereHead);
    head.setMaterial(matRoboticArm);

    // Create eyes
    Sphere sphereEye = new Sphere(20, 20, EYE_RADIUS);
    leftEye = new Geometry("leftEye", sphereEye);
    leftEye.setMaterial(matRoboticArm);
    rightEye = new Geometry("rightEye", sphereEye);
    rightEye.setMaterial(matRoboticArm);

    // Create robotic Arm
    localRootNode.attachChild(pivots[0]);

    for (int i = 0; i < numJoints; i++) {

      if (i > 0) {
        pivots[i].move(0, 0, 2 * SECTION_LENGTH);
      }

      pivots[i].attachChild(sections[i]);
      pivots[i].attachChild(joints[i]);
      if (i < numJoints - 1) {
        pivots[i].attachChild(pivots[i + 1]);
      }
    }

    // place Head
    headNode.move(0, 0, 2 * SECTION_LENGTH);
    headNode.attachChild(head);
    float shift = (float) Math.sqrt(HEAD_RADIUS * HEAD_RADIUS / 2.0);
    leftEye.move(shift, 0, shift);
    headNode.attachChild(leftEye);
    rightEye.move(-1.0f * shift, 0, shift);
    headNode.attachChild(rightEye);
    pivots[numJoints - 1].attachChild(headNode);

    // Load the HUD
    BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(24);
    hudText.setColor(ColorRGBA.White); // font color
    hudText.setLocalTranslation(10, hudText.getLineHeight(), 0);
    hudText.setText(getHUDText());
    localGuiNode.attachChild(hudText);

  }

  protected void setHudText() {

    hudText.setText(getHUDText());
  }

  private String getHUDText() {

    StringBuilder sb = new StringBuilder();
    sb.append("Level = ");
    sb.append(roboticArmApp.currentLevelIndex + 1);
    sb.append("/");
    sb.append(roboticArmApp.levels.size());

    sb.append(", Blue Pills = ");
    sb.append(score.getNumBluePills());
    sb.append("/");
    sb.append(roboticArmApp.numTargetsPerLevel);

    sb.append(", Score = ");
    sb.append(score.getScore());

    return sb.toString();
  }

  protected void setupKeys() {

    int[] keyArray = new int[] { KeyInput.KEY_P, KeyInput.KEY_L, KeyInput.KEY_O, KeyInput.KEY_K, KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_U, KeyInput.KEY_H, KeyInput.KEY_Y, KeyInput.KEY_G, KeyInput.KEY_T,
        KeyInput.KEY_F, KeyInput.KEY_R, KeyInput.KEY_D, KeyInput.KEY_E, KeyInput.KEY_S, KeyInput.KEY_W, KeyInput.KEY_A, };

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
      roboticArmApp.wasMovement = true;
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

  protected EnvState getEnvState() {

    Vector3f[] relativePositions = new Vector3f[numJoints];
    for (int i = 0; i < numJoints; i++) {
      if (i == (numJoints - 1)) { // head relative to last joint
        relativePositions[numJoints - 1] = head.getWorldTranslation().subtract(joints[numJoints - 1].getWorldTranslation()).divide(2 * SECTION_LENGTH);
      } else {
        relativePositions[i] = joints[i + 1].getWorldTranslation().subtract(joints[i].getWorldTranslation()).divide(2 * SECTION_LENGTH);
      }
    }

    // env state - target
    Vector3f targetCoords = getBluePillWorldTranslation();
    Vector3f leftEyeCoords = leftEye.getWorldTranslation();
    float distL = leftEyeCoords.distance(targetCoords) - TARGET_RADIUS;
    Vector3f rightEyeCoords = rightEye.getWorldTranslation();
    float distR = rightEyeCoords.distance(targetCoords) - TARGET_RADIUS;
    Vector3f headCoords = head.getWorldTranslation();
    float headDistance = headCoords.distance(targetCoords) - TARGET_RADIUS - HEAD_RADIUS;
    boolean wasCollision = headDistance < 0.005f;

    EnvState roboticArmEnvState = new EnvState(distL, distR, headDistance, relativePositions, wasCollision);
    return roboticArmEnvState;
  }

  /**
   * This is where JointCommands come in from an AI algorithm
   * 
   * @param jointCommands
   */
  public void moveJoints(List<JointCommand> jointCommands, float speed) {

    for (JointCommand jointCommand : jointCommands) {
      score.incActuationEnergy(jointCommand.getSteps());
      for (int i = 0; i < jointCommand.getSteps(); i++) {
        pivots[jointCommand.getJointNumber()].rotate(0f, jointCommand.getDirection() * .005f * speed, 0f);
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
