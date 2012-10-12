package com.xeiam.proprioceptron.thematrix;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;

public class AIAppState extends AbstractAppState implements PlayerAppState {

  Queue<PlayerCommand> commands;
  float toBeRotated = 0;
  float toBeMoved = 0;
  TheMatrix app;

  public AIAppState() {

    commands = new LinkedList<PlayerCommand>();
  }

  @Override
  public void initialize(AppStateManager asm, Application app) {

    super.initialize(asm, app);
    this.app = (TheMatrix) app;
    this.app.notifyListeners();
  }

  @Override
  public void update(float tpf) {

    if (FastMath.abs(toBeRotated) > tpf) {
      app.movementOver = false;
      app.nowWaiting = false;
      toBeRotated -= AIrotatestep(toBeRotated, tpf);
    } else if (FastMath.abs(toBeMoved) > 5 * tpf) {
      toBeMoved -= AIforwardstep(5 * toBeMoved, tpf);
    } else {
      app.movementOver = !app.nowWaiting;
      app.nowWaiting = app.movementOver || app.nowWaiting;
      if (commands.peek() != null) {
        toBeRotated = commands.peek().getRotation();
        toBeMoved = commands.poll().getForwardMotion();
      }
    }
  }

  public float AIrotatestep(float rotation, float tpf) {

    app.rotateEpsilon(tpf * Math.signum(rotation));
    return (tpf * Math.signum(rotation));
  }

  public float AIforwardstep(float distance, float tpf) {

    // should not use walk direction for this, since cannot determine the number of steps taken.
    app.forwardEpsilon(tpf * Math.signum(distance));
    return tpf * Math.signum(distance);
  }

  public void pushCommand(List<PlayerCommand> commands) {

    this.commands.addAll(commands);
  }

  public void pushCommand(PlayerCommand command) {

    commands.add(command);
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (keyPressed) {
      System.out.println("it's the computer's turn right now.");
    }
  }
}
