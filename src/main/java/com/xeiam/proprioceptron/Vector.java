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
 * @author zackkenyon
 * @create Aug 21, 2012
 */
// an attempt at Cs struct funtionality.
public class Vector implements Dimensional {

  public double x;
  public double y;

  public Vector(double x, double y) {

    this.x = x;
    this.y = y;
  }

  public static Vector fromPolar(double length, double rotation) {

    double x = length * Math.cos(rotation);
    double y = length * Math.sin(rotation);
    return new Vector(x, y);
  }

  @Override
  public double[] toArray() {

    return new double[] { x, y };
  }

  public void plusequals(Vector p) {

    x += p.x;
    y += p.y;
  }

  public void minusequals(Vector p) {

    x -= p.x;
    y -= p.y;
  }

  public void scaleequals(double k) {

    x *= k;
    y *= k;
  }

  public void rotateequals(double rotate, double length /* for persistence */) {// slightly faster to do cos,sin, outside, and take as args

    double x_ = x * Math.sin(rotate) + y * Math.cos(rotate);
    y = x * Math.cos(rotate) - y * Math.sin(rotate);
    x = x_;
  }

  public static Vector invert(Vector p) {

    return new Vector(-p.x, -p.y);
  }

  public static Vector plus(Vector p, Vector q) {

    return new Vector(p.x + q.x, p.y + q.y);
  }

  public static Vector minus(Vector p, Vector q) {

    return new Vector(p.x - q.x, p.y - q.y);

  }

  public static Vector scale(double k, Vector p) {

    return new Vector(k * p.x, k * p.y);
  }

  public static double distance(Vector p, Vector q) {

    return magnitude(minus(p, q));
  }

  public static double argument(Vector v) {

    return Math.atan(v.y / v.x);
  }

  public static double magnitude(Vector v) {

    return Math.sqrt(innerproduct(v, v));
  }

  public static Vector normalize(Vector v) {

    return scale(1.0 / magnitude(v), v);
  }

  public static Vector project(Vector v, Vector u) {

    return scale(innerproduct(u, v) * 1.0 / magnitude(u), u);
  }

  public static double innerproduct(Vector v, Vector u) {

    return v.x * u.x + v.y * u.y;
  }

  public static int orientation(Vector v, Vector u) {

    // returns the sgn of the linear transformation with matrix [uv]
    if (v.y * u.x - v.x * u.y >= 0) {
      return 1;
    }
    return -1;
  }

  public static Vector Zero() {

    return (new Vector(0, 0));
  }

  public static Vector UnitX() {

    return (new Vector(1.0, 0));
  }

  public static Vector UnitY() {

    return (new Vector(0, 1.0));
  }
}
