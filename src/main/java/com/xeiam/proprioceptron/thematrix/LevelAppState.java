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

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 * primarily loads and removes the level specific objects into the game environment, level specific update logic is also delegated to this class.
 * 
 * @author ZackKenyon
 * @create Oct 12, 2012
 */
public class LevelAppState extends AbstractAppState {

  int numbluepills;
  int numredpills;
  boolean pillsmoving;
  TheMatrix app;
  List<Geometry> bluePills;
  List<Geometry> redPills;

  /**
   * Constructor
   * 
   * @param numbluepills number of blue pills in this level, blue pills add 10 to score
   * @param numredpills number of red pills in this level, red pills subtract 10 from score
   * @param pillsmoving **Not Implemented** extra level of difficulty, the pills are moving
   */
  public LevelAppState(int numbluepills, int numredpills, boolean pillsmoving) {

    this.numbluepills = numbluepills;
    this.numredpills = numredpills;
    this.pillsmoving = pillsmoving;
  }

  /**
   * Makes the pill objects. Adds self to statemanager, casts the Application as a TheMatrix object.
   */
  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (TheMatrix) app;
    bluePills = new ArrayList<Geometry>();
    redPills = new ArrayList<Geometry>();
    while (bluePills.size() < numbluepills) {
      Geometry pill = ObjectFactory.getPill(this.app.getAssetManager(), ColorRGBA.Blue);
      this.app.movePill(pill);
      bluePills.add(pill);
    }
    while (redPills.size() < numredpills) {
      Geometry pill = ObjectFactory.getPill(this.app.getAssetManager(), ColorRGBA.Red);
      this.app.movePill(pill);
      redPills.add(pill);
    }

  }

  /**
   * Adds or removes the pills from the scenegraph and the physics space. does not create or destroy pill geometries.
   */
  @Override
  public void setEnabled(boolean enabled) {

    if (enabled) {

      for (Geometry g : bluePills) {
        app.getRootNode().attachChild(g);
      }
      for (Geometry g : redPills) {
        app.getRootNode().attachChild(g);
      }
    } else {

      for (Geometry g : bluePills) {
        app.getRootNode().detachChild(g);
      }
      for (Geometry g : redPills) {
        app.getRootNode().detachChild(g);
      }
    }
  }

  /**
   * Responsible for implementing all the non-physical rules of the game. If the Character's movement is over we decrement the score and take a snapshot of the state of the game according to the AI. We also check for collisions and move the pills
   * that were collided with.
   */
  @Override
  public void update(float tpf) {

    // TODO: if pillsmoving code goes here.

    if (app.movementOver) {
      app.score--;
      List<PillPerceptionState> perceptions = new ArrayList<PillPerceptionState>();
      app.oldEnvState = app.newEnvState;
      Vector3f righteyelocation = app.player.getPhysicsLocation().add(app.viewDirection).add(app.viewDirection.cross(Vector3f.UNIT_Y));
      Vector3f lefteyelocation = app.player.getPhysicsLocation().add(app.viewDirection).add(Vector3f.UNIT_Y.cross(app.viewDirection));

      for (Geometry g : bluePills) {
        Vector3f relativePosition = app.player.getPhysicsLocation().subtract(g.getWorldTranslation());
        // calculate eye positions.
        // calculate eye distances.
        float righteyedistance = righteyelocation.distance(g.getWorldTranslation());
        float lefteyedistance = lefteyelocation.distance(g.getWorldTranslation());
        float middleeyedistance = g.getWorldTranslation().distance(app.player.getPhysicsLocation());
        boolean wasCollision = middleeyedistance < 3f;
        if (wasCollision) {
          app.score += 10;
          app.movePill(g);
          app.numBluePills++;
        }
        perceptions.add(new PillPerceptionState(relativePosition, lefteyedistance, righteyedistance, middleeyedistance, true, wasCollision));

        // 2. notify listeners

      }
      for (Geometry g : redPills) {
        Vector3f relativePosition = app.player.getPhysicsLocation().subtract(g.getWorldTranslation());
        // calculate eye positions.
        // calculate eye distances.
        float righteyedistance = righteyelocation.distance(g.getWorldTranslation());
        float lefteyedistance = lefteyelocation.distance(g.getWorldTranslation());

        float middleeyedistance = g.getWorldTranslation().distance(app.player.getPhysicsLocation());
        boolean wasCollision = middleeyedistance < 3f;
        if (wasCollision) {
          app.score -= 10;
          app.movePill(g);
        }
        perceptions.add(new PillPerceptionState(relativePosition, lefteyedistance, righteyedistance, middleeyedistance, false, wasCollision));

      }
      app.newEnvState = new TheMatrixEnvState(perceptions);
      app.notifyListeners();
    }
  }
}
