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
package org.knowm.proprioceptron.jme.hello;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * Sample 4 - how to trigger repeating actions from the main update loop. In this example, we make the player character rotate.
 */
public class HelloLoop extends SimpleApplication {

  public static void main(String[] args) {

    HelloLoop app = new HelloLoop();
    app.start();
  }

  protected Geometry player;

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