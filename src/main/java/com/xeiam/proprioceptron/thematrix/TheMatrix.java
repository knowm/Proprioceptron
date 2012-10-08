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

import java.util.List;
import java.util.Random;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

public class TheMatrix extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

  private BulletAppState bulletAppState;
  private final Random rand;
  float score = 50;
  // there is no native support for angular velocity so we set these flags in triggers and have angular velocity modeled in update.
  private boolean turnleft;
  private boolean turnright;
  private boolean goforward;
  private boolean gobackward;

  private boolean followCameraOn = false;

  // these are proprioceptive properties
  float scoresnapshot;
  List<Float> distancestoblues;
  List<Float> distancestoreds;

  BitmapText hudText;

  public TheMatrix() {

    super();
    rand = new Random();

  }

  @Override
  public void simpleInitApp() {

    bulletAppState = new BulletAppState();
    stateManager.detach(stateManager.getState(FlyCamAppState.class));

    stateManager.attach(bulletAppState);

    bulletAppState.getPhysicsSpace().enableDebug(assetManager);

    TheMatrixObjectFactory.makeCharacter(rootNode, bulletAppState.getPhysicsSpace(), assetManager);

    TheMatrixObjectFactory.makeLevelEnvironment(rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    TheMatrixObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    TheMatrixObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, bulletAppState.getPhysicsSpace(), assetManager);
    setupKeys();
    // add ourselves as collision listener
    getPhysicsSpace().addCollisionListener(this);
    hudText = new BitmapText(guiFont, false);
    hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
    hudText.setColor(ColorRGBA.Blue); // font color
    hudText.setText("score: " + score); // the text
    hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
    guiNode.attachChild(hudText);
    cam.setLocation(Vector3f.UNIT_Y.mult(50));
    cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z/* because these should not be in the same direction */);

  }

  private PhysicsSpace getPhysicsSpace() {

    return bulletAppState.getPhysicsSpace();
  }

  // public void updateProperties() {
  //
  // scoresnapshot = score;
  // distancestoblues = new ArrayList<Float>();
  // distancestoreds = new ArrayList<Float>();
  // List<Spatial> distanceobjects = rootNode.getChildren();
  // // character is always first child, next 5 are always walls and floor
  // // should probably use an iterator as well.
  // for (int i = 6; i < distanceobjects.size(); i++) {
  // if (distanceobjects.get(i).getName().equals("red")) {
  // distancestoreds.add(new Float(distanceobjects.get(0).getWorldTranslation().distance(distanceobjects.get(i).getWorldTranslation())));
  // } else if (distanceobjects.get(i).getName().equals("blue")) {
  // distancestoblues.add(new Float(distanceobjects.get(0).getWorldTranslation().distance(distanceobjects.get(i).getWorldTranslation())));
  // }
  // }
  // }

  @Override
  public void simpleUpdate(float tpf) {

    score -= tpf;
    if (turnright != turnleft) {
      if (turnright) {
        rootNode.getChild("char").rotate(0, -tpf, 0);
      } else {
        rootNode.getChild("char").rotate(0, tpf, 0);
      }
    }
    if (goforward != gobackward) {
      if (goforward)
        // this is a hack with WAY too much math to be efficient. if you can find a expression for this variable which does not require transforming a quaternion
        // you should definitely replace it.
        rootNode.getChild("char").move(rootNode.getChild("char").getLocalRotation().toRotationMatrix().getColumn(0).mult(10 * tpf));
      else
        rootNode.getChild("char").move(rootNode.getChild("char").getLocalRotation().toRotationMatrix().getColumn(0).mult(10 * -tpf));

    }

  }

  public void setupKeys() {

    inputManager.addMapping("charforward", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("charbackward", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("charturnleft", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("charturnright", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("togglefollow", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(this, "charforward", "charbackward", "charturnleft", "charturnright", "togglefollow");
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf /* completelyunused */) {

    // if key is pressed, change velocity to .01f if key is unpressed, change back to 0
    if (name.equals("charforward")) {

      goforward = keyPressed;
    }
    if (name.equals("charbackward")) {

      gobackward = keyPressed;
    }
    if (name.equals("charturnright")) {
      turnright = keyPressed;
    }
    if (name.equals("charturnleft")) {
      turnleft = keyPressed;
    }
    if (name.equals("togglefollow") && keyPressed) {
      if (followCameraOn) { // turn follow camera off
        cam.setLocation(Vector3f.UNIT_Y.mult(50));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Z/* because these should not be in the same direction */);
        followCameraOn = false;
      } else
        followCameraOn = true;
    }
  }

  @Override
  public void simpleRender(RenderManager rm) {

    if (followCameraOn) {
      cam.setLocation(((Geometry) rootNode.getChild("char")).getWorldTranslation().add(rootNode.getChild("char").getLocalRotation().toRotationMatrix().getColumn(0).mult(-10f).add(Vector3f.UNIT_Y.mult(5f))));
      cam.lookAt(((Geometry) rootNode.getChild("char")).getWorldTranslation(), Vector3f.UNIT_Y);
    }

    // hudText.setText("score: " + score);
    // the text
  }

  @Override
  public void collision(PhysicsCollisionEvent event) {

    if ("char".equals(event.getNodeA().getName()) && "red".equals(event.getNodeB().getName())) {
      score -= 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
        TheMatrixObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
        TheMatrixObjectFactory.makeRedPill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);

      }

    }
    if ("char".equals(event.getNodeA().getName()) && "blue".equals(event.getNodeB().getName())) {
      score += 10;
      if (!("char".equals(event.getNodeA().getName()))) {
        event.getNodeA().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeA());
        TheMatrixObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);
      } else {
        event.getNodeB().removeFromParent();
        getPhysicsSpace().removeAll(event.getNodeB());
        TheMatrixObjectFactory.makeBluePill(rand.nextFloat() * 38 - 19, rand.nextFloat() * 38 - 19, rootNode, getPhysicsSpace(), assetManager);
      }
    }
    if ("char".equals(event.getNodeA().getName()) && "northWall".equals(event.getNodeB().getName())) {

      rootNode.getChild("char").move(Vector3f.UNIT_X.mult(event.getDistance1()));
    }
    if ("char".equals(event.getNodeA().getName()) && "southWall".equals(event.getNodeB().getName())) {
      rootNode.getChild("char").move(Vector3f.UNIT_X.mult(-event.getDistance1()));
    }
    if ("char".equals(event.getNodeA().getName()) && "eastWall".equals(event.getNodeB().getName())) {
      rootNode.getChild("char").move(Vector3f.UNIT_Z.mult(event.getDistance1()));
    }
    if ("char".equals(event.getNodeA().getName()) && "westWall".equals(event.getNodeB().getName())) {
      rootNode.getChild("char").move(Vector3f.UNIT_Z.mult(-event.getDistance1()));
    }
  }
}
