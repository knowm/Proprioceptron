package com.xeiam.proprioceptron.thematrix;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class MatrixPhysicsObjectFactory {

  public static void makeRedPill(float x, float y, Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/red.png"));

    // Make the Character Geometry
    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry pillGeometry = new Geometry("red", sphere);
    pillGeometry.setLocalTranslation(x, 0, y);
    pillGeometry.setMaterial(material);

    // define physical interactions
    GhostControl pillghost = new GhostControl(new SphereCollisionShape(1f));
    // CharacterControl pillcharacter = new CharacterControl((new CylinderCollisionShape(new Vector3f(1, 1, 1), 1)), 0f);
    pillGeometry.addControl(pillghost);
    // pillGeometry.addControl(pillcharacter);
    // pillcharacter.setGravity(0f);
    // Add the character to the environment and to the physics.
    rootNode.attachChild(pillGeometry);
    space.add(pillGeometry);
  }

  public static void makeBluePill(float x, float y, Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/blue.png"));

    // Make the Character Geometry

    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry pillGeometry = new Geometry("blue", sphere);

    pillGeometry.setLocalTranslation(x, 0, y);
    pillGeometry.setMaterial(material);

    // define physical interactions
    GhostControl pillghost = new GhostControl(new SphereCollisionShape(1f));
    // CharacterControl pillcharacter = new CharacterControl((new CylinderCollisionShape(new Vector3f(1, 1, 1), 1)), 0f);
    pillGeometry.addControl(pillghost);
    // pillGeometry.addControl(pillcharacter);
    // pillcharacter.setGravity(0f);
    // Add the character to the environment and to the physics.
    rootNode.attachChild(pillGeometry);
    space.add(pillGeometry);
  }

  public static void makeLevelEnvironment(Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // set the lighting.
    AmbientLight light = new AmbientLight();
    light.setColor(ColorRGBA.LightGray);
    rootNode.addLight(light);
    // load and create materials for the environment.
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked.jpeg"));
    // cheap debug hack.
    Material northwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    northwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_north.jpg"));
    Material southwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    southwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_south.jpg"));
    Material eastwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    eastwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_east.jpg"));
    Material westwallmaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    westwallmaterial.setTexture("ColorMap", assetManager.loadTexture("Textures/concrete_cracked_west.jpg"));

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
    northWallGeom.setMaterial(northwallmaterial);
    southWallGeom.setMaterial(southwallmaterial);
    eastWallGeom.setMaterial(eastwallmaterial);
    westWallGeom.setMaterial(westwallmaterial);
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

  public static void makeCharacter(Node rootNode, PhysicsSpace space, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/NeoGlasses.png"));

    // Make the Character Geometry
    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry characterGeometry = new Geometry("char", sphere);

    characterGeometry.setLocalTranslation(0, 0, 0);
    characterGeometry.setMaterial(material);
    // this rotates the geometry so that the camera is looking at the back of neo's head

    // characterGeometry.rotate(0, 0, FastMath.PI);
    // characterGeometry.rotate(FastMath.HALF_PI, 0, 0);
    // characterGeometry.

    // define physical interactions
    // the cylinders are set to be oriented on the y axis, but I think it's using some sort of local Y axis
    // GhostControl pillghost = new GhostControl(new CylinderCollisionShape(new Vector3f(1, 1, 1), 1));
    CharacterControl pillcharacter = new CharacterControl(new SphereCollisionShape(1f), 0f);


    // characterGeometry.addControl(pillghost);
    // ***************************************************
    // when you uncomment the next line, the cylinders appear upright again and the head appears sideways and pointed the wrong direction.
    characterGeometry.addControl(pillcharacter);
    pillcharacter.setGravity(0f);
    // has the same effect as uncommenting this line of code;
    // characterGeometry.setLocalRotation(Matrix3f.IDENTITY);
    // conclusion: make your own sphere meshes with blender or something.

    // Add the character to the environment and to the physics.
    rootNode.attachChild(characterGeometry);
    space.add(characterGeometry);

  }

  // public static void makeLevel(Random rand, int numreds, int numblues, float livingtax, boolean pillsmoving, Node rootNode, PhysicsSpace space, AssetManager assetManager){
  // for(int i = 0; i<numreds; i++){
  // makeBluePill(rand.nextFloat()*38-19,rand.nextFloat()38-19)
  // }
  //
  // }

  public static void makeHumanUpdater() {

  }

  public static void makeAIUpdater() {

  }

}
