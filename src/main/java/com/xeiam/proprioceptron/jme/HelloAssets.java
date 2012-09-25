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
package com.xeiam.proprioceptron.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Sample 3 - how to load an OBJ model, and OgreXML model, a material/texture, or text.
 */
public class HelloAssets extends SimpleApplication {

  public static void main(String[] args) {

    HelloAssets app = new HelloAssets();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
    Material mat_default = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
    teapot.setMaterial(mat_default);
    rootNode.attachChild(teapot);

    // Create a wall with a simple texture from test_data
    Box box = new Box(Vector3f.ZERO, 2.5f, 2.5f, 1.0f);
    Spatial wall = new Geometry("Box", box);
    Material mat_brick = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat_brick.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
    wall.setMaterial(mat_brick);
    wall.setLocalTranslation(2.0f, -2.5f, 0.0f);
    rootNode.attachChild(wall);

    // Display a line of text with a default font
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText helloText = new BitmapText(guiFont, false);
    helloText.setSize(guiFont.getCharSet().getRenderedSize());
    helloText.setText("Hello World");
    helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
    guiNode.attachChild(helloText);

    // Load a model from test_data (OgreXML + material + texture)
    Spatial ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
    ninja.scale(0.05f, 0.05f, 0.05f);
    ninja.rotate(0.0f, -3.0f, 0.0f);
    ninja.setLocalTranslation(0.0f, -5.0f, -2.0f);
    rootNode.attachChild(ninja);
    // You must add a light to make the model visible
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
    rootNode.addLight(sun);

  }
}
