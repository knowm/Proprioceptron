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

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;

/**
 * Sample 7 - how to load an OgreXML model and play an animation, using channels, a controller, and an AnimEventListener.
 */
public class HelloAnimation extends SimpleApplication implements AnimEventListener {

  private AnimChannel channel;
  private AnimControl control;
  Node player;

  public static void main(String[] args) {

    HelloAnimation app = new HelloAnimation();
    app.start();
  }

  @Override
  public void simpleInitApp() {

    viewPort.setBackgroundColor(ColorRGBA.LightGray);
    initKeys();
    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
    rootNode.addLight(dl);
    player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
    player.setLocalScale(0.5f);
    rootNode.attachChild(player);
    control = player.getControl(AnimControl.class);
    control.addListener(this);
    channel = control.createChannel();
    channel.setAnim("stand");

    SkeletonDebugger skeletonDebug = new SkeletonDebugger("skeleton", control.getSkeleton());
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Green);
    mat.getAdditionalRenderState().setDepthTest(false);
    skeletonDebug.setMaterial(mat);
    player.attachChild(skeletonDebug);
  }

  @Override
  public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

    if (animName.equals("Walk")) {
      channel.setAnim("stand", 0.50f);
      channel.setLoopMode(LoopMode.DontLoop);
      channel.setSpeed(1f);
    }
  }

  @Override
  public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    // unused
  }

  /** Custom Keybinding: Map named actions to inputs. */
  private void initKeys() {

    inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(actionListener, "Walk");
  }

  private ActionListener actionListener = new ActionListener() {

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {

      if (name.equals("Walk") && !keyPressed) {
        if (!channel.getAnimationName().equals("Walk")) {
          channel.setAnim("Walk", 0.50f);
          channel.setLoopMode(LoopMode.Loop);
        }
      }
    }
  };
}