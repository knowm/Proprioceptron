package com.xeiam.proprioceptron.jmehello;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Sample 4 - how to trigger repeating actions from the main update loop. In this example, we make the player character rotate.
 * 
 * @author timmolter
 * @create Sep 3, 2012
 */
public class HelloLoop extends SimpleApplication {

  protected Geometry player;

  public static void main(String[] args) {

    HelloLoop app = new HelloLoop();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    Box b = new Box(Vector3f.ZERO, 1, 1, 1);
    player = new Geometry("blue cube", b);
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    player.setMaterial(mat);
    rootNode.attachChild(player);
  }

  /* This is the update loop */
  @Override
  public void simpleUpdate(float tpf) {

    // make the player rotate
    player.rotate(0, 2 * tpf, 0);
  }

}
