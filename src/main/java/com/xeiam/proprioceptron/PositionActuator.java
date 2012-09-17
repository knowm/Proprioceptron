package com.xeiam.proprioceptron;


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

}
