package com.xeiam.proprioceptron;

/**
 * @author zackkenyon
 * @create Sep 11, 2012
 */
public abstract class Actuator {

  /**
   * Actuators relate two physical states in a one to one manner, where the physical state of the system needs to satisfy the "complete specification" conditions, SemiActuators must be countable to one. States must be derived from pieces of the
   * physics engine plus an array of floating point variables. the dimensions of the states are measured and compared for equality and the constructor will throw an exception if they are unequal. SemiActuators may also have an array of integers
   * associated to them, for when the states are k to 1.
   */


  // Actuators may be composed with each other, Gravity would be a zero dimension actuator for instance.
  State domain;
  State codomain;

  public Actuator(State domain, State codomain) {

    this.domain = domain;
    this.codomain = codomain;
    if (domain.toVector().length < codomain.toVector().length) {
      System.out.println("Learning algorithm must have at least as much information as the environment is providing,");
      // not an exhaustive check, one might fall out of the math eventually though.
      System.exit(1);
    }
  }

  public abstract State Mapto(State domain);// A wrapper for physics logic.

  public abstract State Mapfrom(State codomain);

}
