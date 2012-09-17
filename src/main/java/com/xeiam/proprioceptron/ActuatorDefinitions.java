package com.xeiam.proprioceptron;


class AngularVelocityActuator implements Actuator {

  AngularVelocityState angularvelocities;
  AngleState angles;

  public void setDomain(AngularVelocityState angularvelocities) {

    this.angularvelocities = angularvelocities;
  }

  public void setRange(AngleState angles) {

    this.angles = angles;
  }

  @Override
  public void actuate() {

    for (int i = 0; i < angularvelocities.angularvelocities.length; i++) {
      angles.angles[i].var += angularvelocities.angularvelocities[i].var;
    }
  }
}

class PositionActuator implements Actuator {

  PosXState posxs;
  PosYState posys;
  AngleState angles;
  LengthState lengths;

  public void setDomain(AngleState angles, LengthState lengths) {

    this.angles = angles;
    this.lengths = lengths;
  }

  public void setRange(PosXState posxs, PosYState posys) {

    this.posxs = posxs;
    this.posys = posys;
  }

  @Override
  public void actuate() {

    posxs.posxs[0].var = lengths.lengths[0].var * Math.cos(angles.angles[0].var);
    posys.posys[0].var = lengths.lengths[0].var * Math.sin(angles.angles[0].var);
    for (int i = 1; i < angles.angles.length; i++) {
      posxs.posxs[i].var = posxs.posxs[i - 1].var + lengths.lengths[i].var * Math.cos(angles.angles[i].var);
      posys.posys[i].var = posys.posys[i - 1].var + lengths.lengths[i].var * Math.sin(angles.angles[i].var);
    }

  }

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

}
