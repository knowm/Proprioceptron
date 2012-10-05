package com.xeiam.proprioceptron.thematrixv1;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public class MatrixPhysicsObjectFactoryV1 {

  private static final int PLATFORM_DIMENSION = 20;
  private static final int WALL_DIMENSION = 2;

  public static void makeGameEnvironment(Node rootNode, PhysicsSpace physicsSpace, AssetManager assetManager) {

    // set the lighting
    AmbientLight light = new AmbientLight();
    light.setColor(ColorRGBA.LightGray);
    rootNode.addLight(light);

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

  public static CharacterControl makeCharacter(Node rootNode, PhysicsSpace physicsSpace, AssetManager assetManager) {

    // load and make materials
    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    material.setTexture("ColorMap", assetManager.loadTexture("Textures/NeoGlasses.png"));

    // Make the Character Geometry
    Sphere sphere = new Sphere(20, 20, 1f);
    Geometry characterGeometry = new Geometry("player", sphere);

    characterGeometry.setLocalTranslation(0, 0, 0);
    characterGeometry.setMaterial(material);
    // this rotates the geometry so that the camera is looking at the back of neo's head

    // characterGeometry.rotate(0, 0, FastMath.PI);
    // characterGeometry.rotate(FastMath.HALF_PI, 0, 0);
    // characterGeometry.

    // define physical interactions
    // the cylinders are set to be oriented on the y axis, but I think it's using some sort of local Y axis
    // GhostControl pillghost = new GhostControl(new CylinderCollisionShape(new Vector3f(1, 1, 1), 1));
    CharacterControl player = new CharacterControl(new SphereCollisionShape(1f), 0f);

    // characterGeometry.addControl(pillghost);
    // ***************************************************
    // when you uncomment the next line, the cylinders appear upright again and the head appears sideways and pointed the wrong direction.
    characterGeometry.addControl(player);
    player.setGravity(0f);
    // has the same effect as uncommenting this line of code;
    // characterGeometry.setLocalRotation(Matrix3f.IDENTITY);
    // conclusion: make your own sphere meshes with blender or something.

    // Add the character to the environment and to the physics.
    rootNode.attachChild(characterGeometry);
    physicsSpace.add(characterGeometry);

    return player;

  }

}
