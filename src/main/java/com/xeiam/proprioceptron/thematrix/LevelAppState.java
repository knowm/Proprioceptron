package com.xeiam.proprioceptron.thematrix;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class LevelAppState extends AbstractAppState {

  int numbluepills;
  int numredpills;
  boolean pillsmoving;
  TheMatrix app;
  List<Geometry> bluePills;
  List<Geometry> redPills;

  public LevelAppState(int numbluepills, int numredpills, boolean pillsmoving) {

    this.numbluepills = numbluepills;
    this.numredpills = numredpills;
    this.pillsmoving = pillsmoving;
  }

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

  @Override
  public void update(float tpf) {


    // TODO: if pillsmoving code goes here.

    if (app.movementOver) {
      app.score--;
      List<PillPerceptionState> perceptions = new ArrayList<PillPerceptionState>();
      app.oldEnvState = app.newEnvState;
      Vector3f righteyelocation = app.player.getPhysicsLocation().add(app.viewDirection).add(app.viewDirection.cross(Vector3f.UNIT_Y));
      Vector3f lefteyelocation = app.player.getPhysicsLocation().add(app.viewDirection).add(Vector3f.UNIT_Y.cross(app.viewDirection));

      // 4. calculate state
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
        }
        perceptions.add(new PillPerceptionState(relativePosition, lefteyedistance, righteyedistance, middleeyedistance, wasCollision));

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
        perceptions.add(new PillPerceptionState(relativePosition, lefteyedistance, righteyedistance, middleeyedistance, wasCollision));

      }
      app.newEnvState = new TheMatrixEnvState(perceptions);
      app.notifyListeners();
    }
  }

  public float[] getBlueDistances() {

    return null;

  }

  public float[] getRedDistances() {

    return null;
  }

}
