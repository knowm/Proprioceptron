package com.xeiam.proprioceptron.game;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class MatrixPhysicsObjectFactory {

  public static void MakeRedPill(float x, float y, Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/red.png"));

    // Make the Character Geometry
    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry pillGeometry = new Geometry("red", sphere);

    pillGeometry.setLocalTranslation(x, 0, y);
    pillGeometry.setMaterial(material);
    pillGeometry.addControl(new RigidBodyControl(0));

    // Add the character to the environment and to the physics.
    rootNode.attachChild(pillGeometry);
    space.add(pillGeometry);
  }

  public static void MakeBluePill(float x, float y, Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.png"));

    // Make the Character Geometry

    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry pillGeometry = new Geometry("blue", sphere);

    pillGeometry.setLocalTranslation(x, 0, y);
    pillGeometry.setMaterial(material);
    pillGeometry.addControl(new RigidBodyControl(0));

    // Add the character to the environment and to the physics.
    rootNode.attachChild(pillGeometry);
    space.add(pillGeometry);
  }

  public static void MakeLevelEnvironment(Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // set the lighting.
    AmbientLight light = new AmbientLight();
    light.setColor(ColorRGBA.LightGray);
    rootNode.addLight(light);
    // load and create materials for the environment.
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    // Create the geometry of the environment.
    Box floorBox = new Box(20, 0.25f, 20);
    Geometry floorGeometry = new Geometry("Floor", floorBox);
    floorGeometry.setMaterial(material);
    floorGeometry.setLocalTranslation(0, -2.5f, 0);

    Box northWallBox = new Box(20, 5, .25f);
    Box southWallBox = new Box(20, 5, .25f);
    Box eastWallBox = new Box(.25f, 5, 20);
    Box westWallBox = new Box(.25f, 5, 20);
    Geometry northWallGeom = new Geometry("Wall", northWallBox);
    Geometry southWallGeom = new Geometry("Wall", southWallBox);
    Geometry eastWallGeom = new Geometry("Wall", eastWallBox);
    Geometry westWallGeom = new Geometry("Wall", westWallBox);
    northWallGeom.setMaterial(material);
    southWallGeom.setMaterial(material);
    eastWallGeom.setMaterial(material);
    westWallGeom.setMaterial(material);
    northWallGeom.setLocalTranslation(0, 0, 20);// I have no idea if these are in order, but it will be easy to fix.
    southWallGeom.setLocalTranslation(0, 0, -20);
    eastWallGeom.setLocalTranslation(20, 0, 0);
    westWallGeom.setLocalTranslation(-20, 0, 0);

    // Add the geometries to the environment, and endow them with physical properties

    floorGeometry.addControl(new RigidBodyControl(0));
    rootNode.attachChild(floorGeometry);
    space.add(floorGeometry);
    northWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(northWallGeom);
    space.add(northWallGeom);
    southWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(southWallGeom);
    space.add(southWallGeom);
    eastWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(eastWallGeom);
    space.add(eastWallGeom);
    westWallGeom.addControl(new RigidBodyControl(0));
    rootNode.attachChild(westWallGeom);
    space.add(westWallGeom);

  }

  public static void MakeCharacter(Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/NeoGlasses.png"));

    // Make the Character Geometry
    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry characterGeometry = new Geometry("char", sphere);

    characterGeometry.setLocalTranslation(0, 0, 0);
    characterGeometry.setMaterial(material);
    characterGeometry.addControl(new RigidBodyControl(1));
    // Add the character to the environment and to the physics.
    rootNode.attachChild(characterGeometry);
    space.add(characterGeometry);

  }

  public static void SetCameraPosition() {

  }

  public static void MakeHumanUpdater() {

  }

  public static void MakeAIUpdater() {

  }
}
