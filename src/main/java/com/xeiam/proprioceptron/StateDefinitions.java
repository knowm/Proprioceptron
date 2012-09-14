package com.xeiam.proprioceptron;


/**
 * these are really angular impulses, but timing has not yet been specified
 * 
 * @author Zackkenyon
 * @create Sep 11, 2012
 */
class TorqueState implements State {

  FreeVar[] torques;

  @Override
  public FreeVar[] toVector() {

    return torques;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { torques.length + " torques" }; // + id
  }

  @Override
  public void addVars(FreeVar[] torques) {

    this.torques = torques;

  }
}

class TensionState implements State {

  FreeVar[] tensions;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return tensions;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] tensions) {

    this.tensions = tensions;

  }
}

class PosYState implements State {

  // TODO rewrite in new posx, posy format.
  FreeVar[] posys;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return posys;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] posys) {

    this.posys = posys;

  }
}

class PosXState implements State {

  // TODO rewrite in new posx, posy format.
  FreeVar[] posxs;
  String[] doc;

  @Override
  public FreeVar[] toVector() {

    return posxs;
  }

  @Override
  public String[] vectorDoc() {

    return doc;
  }

  @Override
  public void addVars(FreeVar[] posxs) {

    this.posxs = posxs;

  }
}

class AngularVelocityState implements State {

  FreeVar[] angularvelocities;

  @Override
  public FreeVar[] toVector() {

    return angularvelocities;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "angular velocities" }; // + id
  }

  @Override
  public void addVars(FreeVar[] angularvelocities) {

    this.angularvelocities = angularvelocities;

  }
}

class AngleState implements State {

  // the angle will be regularly updated as the angle from the last joint. if it points in the same direction as the last joint,
  // then the angle is 0, positive is counterclockwise, negative angles are clockwise.
  FreeVar[] angles;

  float maxangle; // collision detection will be either difficult to implement or computationally expensive without these fields.
  float[] maxangles;

  public void initialize() {

    maxangles = new float[angles.length];
    maxangles[0] = 360f;
    for (int i = 1; i < angles.length; i++)
      maxangles[i] = 170f;
    maxangle = 360;
  }

  @Override
  public String[] vectorDoc() {

    return new String[] { "AngleState * " + angles.length };
  }

  @Override
  public FreeVar[] toVector() {

    return angles;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    angles = vars;

  }

}

class EnergyState implements State {

  // this is in anticipation of giving the the learning algorithm a much finer solution. namely one in which
  // energy usage is minimized.
  FreeVar[] energy;

  @Override
  public String[] vectorDoc() {

    return new String[] { "energy * " + energy.length };
  }

  @Override
  public FreeVar[] toVector() {

    return energy;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    energy = vars;

  }

}

class DistanceFromGoalState implements State {

  FreeVar[] distances;

  @Override
  public String[] vectorDoc() {

    return new String[] { "distancesfromgoal * " + distances.length };
  }

  @Override
  public FreeVar[] toVector() {

    return distances;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    distances = vars;

  }

}

class LengthState implements State {

  FreeVar[] lengths;

  @Override
  public String[] vectorDoc() {

    return new String[] { "lengths * " + lengths.length };
  }

  @Override
  public FreeVar[] toVector() {

    return lengths;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    lengths = vars;
  }

}

class DensityState implements State {

  FreeVar[] densities;

  @Override
  public String[] vectorDoc() {

    return new String[] { "densities * " + densities.length };
  }

  @Override
  public FreeVar[] toVector() {

    return densities;
  }

  @Override
  public void addVars(FreeVar[] vars) {

    densities = vars;
  }
}