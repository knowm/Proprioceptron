package com.xeiam.proprioceptron;

/**
 * A float wrapper. Used for features of the physics of the program which specify a dimension. Will eventually be used to specify an interface, which specifies a way of retrieving dimension.
 * 
 * @author Zackkenyon
 * @create Sep 12, 2012
 */

public class FreeVar {

  public float var;
  public VarType type;

  public FreeVar(float var, VarType type) {

    this.var = var;
    this.type = type;
  }
}
