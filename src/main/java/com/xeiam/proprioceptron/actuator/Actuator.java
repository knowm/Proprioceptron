package com.xeiam.proprioceptron.actuator;

/**
 * @author zackkenyon
 * @create Sep 11, 2012
 */
public interface Actuator {

  // Actuators may be composed with each other, Gravity would be a zero dimension actuator for instance.

  public void actuate();// A wrapper for physics logic.

}
