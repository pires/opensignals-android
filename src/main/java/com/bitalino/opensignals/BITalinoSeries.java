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

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;

import java.util.Observable;
import java.util.Observer;

/**
 * Data series for BITalino readings.
 */
public class BITalinoSeries implements XYSeries {
  private SimpleXYSeries series;
  private final MyObserver notifier;

  public BITalinoSeries(final String title) {
    this.series = new SimpleXYSeries(title);
    this.notifier = new MyObserver();
  }

  public String getTitle() {
    return series.getTitle();
  }

  public int size() {
    return series.size();
  }

  public Number getX(int index) {
    return series.getX(index);
  }

  public Number getY(int index) {
    return series.getY(index);
  }

  public void addLast(Number x, Number y) {
    series.addLast(x, y);
    notifier.notifyObservers();
  }

  public void removeFirst() {
    if (series.size() > 0)
      series.removeFirst();
  }

  public void addObserver(Observer observer) {
    notifier.addObserver(observer);
  }

  public void removeObserver(Observer observer) {
    notifier.deleteObserver(observer);
  }

  /**
   * Observable instrumentation.
   */
  private class MyObserver extends Observable {
    @Override
    public void notifyObservers() {
      setChanged();
      super.notifyObservers();
    }
  }

}
