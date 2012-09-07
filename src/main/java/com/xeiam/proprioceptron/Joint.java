/**
 * Copyright 2012 MANC LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xeiam.proprioceptron;

/**
 * @author Zackkenyon
 * @create Aug 21, 2012
 */
public class Joint {

  // as far as the renderer and the learning algorithm is concerned, this consists of exactly
  // three values, angle, omega, and omegadot.
  Joint out;// pointing away from center
  Joint in;// pointing towards center
  public Vector rod;
  public Vector dir;
  public Vector pos;
  public double omega;
  public double omegadot;
  public double theta;// in radians
  double magnitude;

  public Joint(Vector pos) {// adds a new segment pointing to the right

    this.pos = pos;
    omega = 0;
    omegadot = 0;
  }

  public void setout(Joint out) {

    this.out = out;
    magnitude = Vector.magnitude(Vector.minus(out.pos, pos));
    dir = Vector.normalize(Vector.minus(out.pos, pos));
  }

  public void setin(Joint in) {

    this.in = in;
  }

  public void setangle() {

    if (in == null) {
      theta = Vector.orientation(dir, Vector.UnitX()) * Math.acos(Vector.innerproduct(dir, Vector.UnitX()));
    } else {
      theta = Vector.orientation(dir, in.dir) * Math.acos(Vector.innerproduct(dir, in.dir));
    }
  }

  // rigidity is a statement about the propagation of force, so our method will be to
  // propagate forces iteratively, and then propagate motion
  // the tension on any rod is the centripetal force from the base of that rod.
  //
  public void accelerate() {

    omega += omegadot;
  }

  public void move() {

    theta += omega;

  }

  public Vector getposition() {

    if (in == null) {
      return Vector.Zero();
    } else {
      double temp = globaltheta();
      rod = new Vector(magnitude * Math.cos(temp), magnitude * Math.sin(temp));
      return Vector.plus(in.getposition(), rod);
    }
  }

  public double globaltheta() {

    if (in != null) {
      return theta + in.globaltheta();
    }

    else {
      return theta;
    }
  }
}