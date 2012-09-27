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

import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * @author timmolter
 * @create Sep 25, 2012
 */
public class RoboticArmUtils {

  public static void createWorld(Node rootNode, AssetManager assetManager) {

    // AmbientLight light = new AmbientLight();
    // light.setColor(ColorRGBA.White.mult(1.3f));
    // rootNode.addLight(light);

    // PointLight pl = new PointLight();
    // pl.setPosition(new Vector3f(-2, 4, 3));
    // pl.setColor(new ColorRGBA(1f, 1f, 0.8f, 0.4f));
    // pl.setRadius(0f);
    // rootNode.addLight(pl);

    // DirectionalLight sun = new DirectionalLight();
    // sun.setColor(ColorRGBA.White);
    // sun.setDirection(new Vector3f(0, -.5f, -.5f).normalizeLocal());
    // rootNode.addLight(sun);

    /** Must add a light to make the lit object visible! */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, -5, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));

    Box floorBox = new Box(8, .5f, 8);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -1.0f * Constants.HEAD_RADIUS - .5f, 0);
    floorGeometry.addControl(new RigidBodyControl(0));
    rootNode.attachChild(floorGeometry);
    // physicsSpace.add(floorGeometry);

  }

}
