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
package com.xeiam.proprioceptron.jme.hello;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Sample 1 - how to get started with the most simple JME 3 application. Display a blue 3D cube and view from all sides by moving the mouse and pressing the WASD keys.
 */
public class HelloJME3 extends SimpleApplication {

  public static void main(String[] args) {

    HelloJME3 app = new HelloJME3();
    app.start(); // start the game
  }

  @Override
  public void simpleInitApp() {

    Box b = new Box(Vector3f.ZERO, 1, 1, 1); // create cube shape at the origin
    Geometry geom = new Geometry("Box", b); // create cube geometry from the shape
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); // create a simple material
    mat.setColor("Color", ColorRGBA.Blue); // set color of material to blue
    geom.setMaterial(mat); // set the cube's material
    rootNode.attachChild(geom); // make the cube appear in the scene
  }
}
