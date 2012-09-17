package com.xeiam.proprioceptron;

/**
 * A double wrapper, a typed java pointer. Used for features of the physics of the program which specify a dimension. Will eventually be used to specify an interface, which specifies a way of retrieving dimension.
 * 
 * @author Zackkenyon
 * @create Sep 12, 2012
 */

public class FreeVar {

  // multidimensional support in future. not really sure how to do this objectively.
  public double var;
  public VarType type;

  public FreeVar(double var, VarType type) {

    this.var = var;
    this.type = type;
  }
}
