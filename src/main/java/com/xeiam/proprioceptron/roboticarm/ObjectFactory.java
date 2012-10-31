package com.xeiam.proprioceptron.roboticarm;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * @author timmolter
 * @create Oct 31, 2012
 */
public class ObjectFactory {

  public static void setupGameEnvironment(Node rootNode, AssetManager assetManager, int numJoints) {

    // Must add a light to make the lit object visible!
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, -5, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    // create floor
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    float dimension = RoboticArmConstants.SECTION_LENGTH * numJoints * 2.3f;
    Box floorBox = new Box(dimension, .5f, dimension);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -1.0f * RoboticArmConstants.HEAD_RADIUS - .5f, 0);
    floorGeometry.addControl(new RigidBodyControl(0));
    rootNode.attachChild(floorGeometry);

    // scene background color
    // viewPort.setBackgroundColor(new ColorRGBA(.5f, .8f, .99f, 1.0f));

  }

  public static void setupKeys(InputManager inputManager, InputListener inputListener, int numJoints) {

    int[] keyArray = new int[] { KeyInput.KEY_P, KeyInput.KEY_L, KeyInput.KEY_O, KeyInput.KEY_K, KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_U, KeyInput.KEY_H, KeyInput.KEY_Y, KeyInput.KEY_G, KeyInput.KEY_T,
        KeyInput.KEY_F, KeyInput.KEY_R, KeyInput.KEY_D, KeyInput.KEY_E, KeyInput.KEY_S, KeyInput.KEY_W, KeyInput.KEY_A, };

    for (int i = 0; i < numJoints; i++) {
      if (i < 9) { // only up to 9 joints (18 keys) bounded
        inputManager.addMapping("Right_" + i, new KeyTrigger(keyArray[2 * i]));
        inputManager.addListener(inputListener, ("Right_" + i));
        inputManager.addMapping("Left_" + i, new KeyTrigger(keyArray[2 * i + 1]));
        inputManager.addListener(inputListener, ("Left_" + i));
      }
    }

  }

}
