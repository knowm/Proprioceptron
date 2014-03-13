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
package com.xeiam.proprioceptron.thematrix;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

/**
 * This class is responsible for constructing the graphical objects encountered in the game.
 * 
 * @author timmolter
 * @create Oct 5, 2012
 */
public class ObjectFactory {

  public static final int PLATFORM_DIMENSION = 20;
  public static final int WALL_DIMENSION = 2;
  public static final float PLAYER_RADIUS = 2.0f;
  public static final float PILL_RADIUS = 1.0f;

  public enum GameView {

    THIRD_PERSON_CENTER, THIRD_PERSON_FOLLOW, FIRST_PERSON, GOD_VIEW;

    public GameView getNext() {

      return this.ordinal() < GameView.values().length - 1 ? GameView.values()[this.ordinal() + 1] : GameView.values()[0];
    }
  }

  public static void setupGameEnvironment(Node rootNode, PhysicsSpace physicsSpace, AssetManager assetManager) {

    // Must add a light to make the lit objects visible
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(1, -5, -2).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    // Platform
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    Box floorBox = new Box(PLATFORM_DIMENSION, WALL_DIMENSION, PLATFORM_DIMENSION);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -WALL_DIMENSION, 0); // shift it down so the platform surface is at y = 0

    // Walls
    Material northwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    northwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_north.jpg"));
    Material southwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    southwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_south.jpg"));
    Material eastwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    eastwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_east.jpg"));
    Material westwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    westwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_west.jpg"));

    Box northWallBox = new Box(PLATFORM_DIMENSION, WALL_DIMENSION, WALL_DIMENSION);
    Box southWallBox = new Box(PLATFORM_DIMENSION, WALL_DIMENSION, WALL_DIMENSION);
    Box eastWallBox = new Box(WALL_DIMENSION, WALL_DIMENSION, PLATFORM_DIMENSION);
    Box westWallBox = new Box(WALL_DIMENSION, WALL_DIMENSION, PLATFORM_DIMENSION);
    Geometry northWallGeom = new Geometry("Wall", northWallBox);
    Geometry southWallGeom = new Geometry("Wall", southWallBox);
    Geometry eastWallGeom = new Geometry("Wall", eastWallBox);
    Geometry westWallGeom = new Geometry("Wall", westWallBox);
    northWallGeom.setMaterial(northwallmaterial);
    southWallGeom.setMaterial(southwallmaterial);
    eastWallGeom.setMaterial(eastwallmaterial);
    westWallGeom.setMaterial(westwallmaterial);
    northWallGeom.setLocalTranslation(0, 0, PLATFORM_DIMENSION + WALL_DIMENSION);
    southWallGeom.setLocalTranslation(0, 0, -PLATFORM_DIMENSION - WALL_DIMENSION);
    eastWallGeom.setLocalTranslation(PLATFORM_DIMENSION + WALL_DIMENSION, 0, 0);
    westWallGeom.setLocalTranslation(-PLATFORM_DIMENSION - WALL_DIMENSION, 0, 0);

    // Add the geometries to the environment, and endow them with physical properties

    floorGeometry.addControl(new RigidBodyControl(0));
    rootNode.attachChild(floorGeometry);
    physicsSpace.add(floorGeometry);
    northWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(northWallGeom);
    physicsSpace.add(northWallGeom);
    southWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(southWallGeom);
    physicsSpace.add(southWallGeom);
    eastWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(eastWallGeom);
    physicsSpace.add(eastWallGeom);
    westWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(westWallGeom);
    physicsSpace.add(westWallGeom);

  }

  public static CharacterControl getPlayer(Node rootNode, PhysicsSpace physicsSpace, AssetManager assetManager) {

    // make player
    // CollisionShape capsule = new SphereCollisionShape(PLAYER_RADIUS);
    // CharacterControl player = new CharacterControl(capsule, 0f);
    // Node playerNode = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
    // playerNode.scale(.6f);
    // playerNode.addControl(player);
    // physicsSpace.add(player);
    // rootNode.attachChild(playerNode);
    //
    // return player;

    Node playerNode = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
    playerNode.setLocalScale(.3f);
    rootNode.attachChild(playerNode);

    // Create a appropriate physical shape for it
    // spheres both calculate collision MUCH faster, and also much more intelligently.
    // The capsule clipping problem is going to be intrinsic to any object model which isn't using forces to adjust it's position smoothly.

    SphereCollisionShape sphereShape = new SphereCollisionShape(PLAYER_RADIUS);
    CharacterControl player = new CharacterControl(sphereShape, 2f);
    // Attach physical properties to model and PhysicsSpace
    playerNode.addControl(player);
    physicsSpace.add(player);

    return player;
  }

  public static Geometry getPill(AssetManager assetManager, ColorRGBA color) {

    Material matTarget = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    matTarget.setBoolean("UseMaterialColors", true);
    matTarget.setColor("Specular", ColorRGBA.White);
    matTarget.setColor("Diffuse", color);
    matTarget.setFloat("Shininess", 128f); // [1,128]

    Sphere sphere = new Sphere(10, 10, PILL_RADIUS);
    sphere.setTextureMode(Sphere.TextureMode.Projected);
    TangentBinormalGenerator.generate(sphere);
    Geometry bluePill = new Geometry("pill", sphere);
    bluePill.setMaterial(matTarget);

    return bluePill;
  }
}
