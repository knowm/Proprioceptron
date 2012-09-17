package com.xeiam.proprioceptron;

class CentrifugalForceActuator implements Actuator {

  // this is an example of where it's useful to have overdetermined information. It's possible that you
  PosXState posxs;
  PosYState posys;
  LengthState lengths;
  DensityState densities;
  AngularVelocityState angularvels;
  AngleState angles;
  // codomain vars
  LinearAccelerationState tensions;

  public void setDomain(PosXState posxs, PosYState posys, LengthState lengths, DensityState densities, AngularVelocityState angularvels, AngleState angles) {

    this.posxs = posxs;
    this.posys = posys;
    this.lengths = lengths;
    this.densities = densities;
    this.angularvels = angularvels;
    this.angles = angles;
  }

  public void setRange(LinearAccelerationState tensions) {

    this.tensions = tensions;
  }

  @Override
  public void actuate() {

  }

  public Vector getCenterOfMassOutward(int index) {

    double averagex = 0.0;
    double averagey = 0.0;
    for (int i = index + 1; i < posxs.posxs.length; i++) {
      averagex += (posxs.posxs[i - 1].var + posxs.posxs[i].var) * densities.densities[i].var;
      averagey += (posys.posys[i - 1].var + posys.posys[i].var) * densities.densities[i].var;
    }
    return new Vector(averagex/posxs.posxs.length,averagey/posxs.posxs.length);
    
  }
  public Vector getCenterOfMassInward(int index) {
    double averagex = 0.0;
    double averagey = 0.0;
      averagex += (posxs.posxs[0].var) * densities.densities[0].var;
      averagey += (posys.posys[0].var) * densities.densities[0].var;
    for (int i = index; i >0; i--) {
      averagex += (posxs.posxs[i - 1].var + posxs.posxs[i].var) * densities.densities[i].var;
      averagey += (posys.posys[i - 1].var + posys.posys[i].var) * densities.densities[i].var;
    }
    return new Vector(averagex/posxs.posxs.length,averagey/posxs.posxs.length);
  }
}