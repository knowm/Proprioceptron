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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jme3.app.SimpleApplication;

/**
 * @author timmolter
 * @create Oct 5, 2012
 */
public abstract class ProprioceptronApplication extends SimpleApplication {

  protected final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
  public EnvState oldEnvState;
  public EnvState newEnvState;

  @Override
  public void simpleInitApp() {

    // let app run in background
    setPauseOnLostFocus(false);

    // hide scene graph statistics
    setDisplayStatView(false);
    setDisplayFps(false);
  }

  public void addChangeListener(PropertyChangeListener newListener) {

    listeners.add(newListener);
  }

  /**
   * Send PropertyChangeEvent to observers (listeners)
   */
  public void notifyListeners() {

    PropertyChangeEvent pce = new PropertyChangeEvent(this, "", oldEnvState, newEnvState);

    for (Iterator<PropertyChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
      PropertyChangeListener observer = iterator.next();
      observer.propertyChange(pce);
    }
  }
}
