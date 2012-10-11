package com.xeiam.proprioceptron.thematrix;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;


public class HumanAppState extends AbstractAppState implements PlayerAppState {

  private boolean isGoingForward = false;
  private boolean isGoingBackward = false;
  private boolean isTurningLeft = false;
  private boolean isTurningRight = false;
  private TheMatrix app;

  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (TheMatrix) app;
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    app.nowWaiting = false;
    if (name.equals("forward")) {
      // forward direction
      isGoingForward = keyPressed;
    } else if (name.equals("backward")) {
      // backward direction
      isGoingBackward = keyPressed;
    } else if (name.equals("turnright")) {
      isTurningRight = keyPressed;
    } else if (name.equals("turnleft")) {
      isTurningLeft = keyPressed;
    } else if (name.equals("givecontrol") && keyPressed) {
      app.currentPlayer = app.ai;
    }
  }

  @Override
  public void update(float tpf) {

    app.movementOver = !(app.nowWaiting || isGoingForward || isGoingBackward || isTurningLeft || isTurningRight);
    app.nowWaiting = app.nowWaiting || app.movementOver;
    if (isGoingForward && !isGoingBackward)
      app.forwardEpsilon(5 * tpf);
    else if (!isGoingForward && isGoingBackward)
      app.forwardEpsilon(-5 * tpf);
    if (isTurningLeft && !isTurningRight)
      app.rotateEpsilon(tpf);
    else if (!isTurningLeft && isTurningRight)
      app.rotateEpsilon(-tpf);
  }
}
