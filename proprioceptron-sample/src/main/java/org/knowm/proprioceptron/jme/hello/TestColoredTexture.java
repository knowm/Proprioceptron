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
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class TestColoredTexture extends SimpleApplication {

  private float time = 0;
  private ColorRGBA nextColor;
  private ColorRGBA prevColor;
  private Material mat;

  public static void main(String[] args) {

    TestColoredTexture app = new TestColoredTexture();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    Quad quadMesh = new Quad(512, 512);
    Geometry quad = new Geometry("Quad", quadMesh);
    quad.setQueueBucket(Bucket.Gui);

    mat = new Material(assetManager, "Common/MatDefs/Misc/ColoredTextured.j3md");
    mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
    quad.setMaterial(mat);
    guiNode.attachChildAt(quad, 0);

    nextColor = ColorRGBA.randomColor();
    prevColor = ColorRGBA.Black;
  }

  @Override
  public void simpleUpdate(float tpf) {

    time += tpf;
    if (time > 1f) {
      time -= 1f;
      prevColor = nextColor;
      nextColor = ColorRGBA.randomColor();
    }
    ColorRGBA currentColor = new ColorRGBA();
    currentColor.interpolate(prevColor, nextColor, time);

    mat.setColor("Color", currentColor);
  }

}
