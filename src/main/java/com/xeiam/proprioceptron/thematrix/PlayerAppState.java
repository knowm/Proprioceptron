package com.xeiam.proprioceptron.thematrix;


public interface PlayerAppState {

  public void update(float tpf);

  public void onAction(String name, boolean keyPressed, float tpf);
}
