package com.xeiam.proprioceptron;

/**
 * The complete set of free variable types to be used in all implementors of State. Used for sorting the environment state into substates easily.
 * 
 * @author Zackkenyon
 * @create Sep 11, 2012
 */
public enum VarType {
  TORQUE, ANGLE, ANGULARVELOCITY, TENSION, ENERGY, POSITION, DISTANCEFROMGOAL, LENGTH, DENSITY, DIRECTION
}
