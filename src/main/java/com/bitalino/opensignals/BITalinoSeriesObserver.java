/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bitalino.opensignals;

import com.androidplot.xy.XYPlot;

import java.util.Observable;
import java.util.Observer;

/**
 * Redraws a plot whenever notified.
 */
public class BITalinoSeriesObserver implements Observer {
  XYPlot plot = null;

  public BITalinoSeriesObserver() {
  }

  @Override
  public void update(Observable o, Object arg) {
    plot.redraw();
  }

  public XYPlot getPlot() {
    return plot;
  }

  public void setPlot(XYPlot plot) {
    this.plot = plot;
  }

}