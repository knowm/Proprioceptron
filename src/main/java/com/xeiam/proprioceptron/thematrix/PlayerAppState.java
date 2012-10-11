package com.xeiam.proprioceptron.thematrix;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

public interface PlayerAppState {

  public void update(float tpf);

  public void onAction(String name, boolean keyPressed, float tpf);

  public void initialize(AppStateManager stateManager, Application app);
}
