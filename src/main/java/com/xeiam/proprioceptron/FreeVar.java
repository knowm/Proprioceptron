package com.xeiam.proprioceptron;

/**
 * A double wrapper, a typed java pointer. Used for features of the physics of the program which specify a dimension. Will eventually be used to specify an interface, which specifies a way of retrieving dimension.
 * 
 * @author Zackkenyon
 * @create Sep 12, 2012
 */

public class FreeVar {

  // multidimensional support in future. not really sure how to do this objectively.
  private double var;
  public VarType type;
  private Dimensional dimvar;
  public FreeVar(double var, VarType type) {

    setVar(var);
    this.type = type;
  }

  public FreeVar(Dimensional dimvar, VarType type) {

    setDimensional(dimvar);
    this.type = type;
  }

  public Dimensional getDimensional() {

    return dimvar;
  }

  public void setDimensional(Dimensional dimvar) { // will require a cast to do object specific logic...

    this.dimvar = dimvar;
  }

  public double getVar() {

    return var;
  }

  public void setVar(double var) {

    this.var = var;
  }
  

}
