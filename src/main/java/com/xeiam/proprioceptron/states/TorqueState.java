package com.xeiam.proprioceptron.states;


/**
 * TorqueState contains information about the linear acceleration of the joints perpendicular to the direction of the rod. counterclockwise is positive, units are newtons*meters.
 * 
 * @author Zackkenyon
 * @create Sep 11, 2012
 */
public class TorqueState extends State {

  @Override
  public String[] vectorDoc() {

    return new String[] { vars.length + " torques" }; // + id
  }
}