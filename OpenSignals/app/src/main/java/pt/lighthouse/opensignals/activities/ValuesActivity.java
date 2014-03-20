package pt.lighthouse.opensignals.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import pt.lighthouse.opensignals.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * The Values activity is where the readings from the bitalino will be shown.
 */
@ContentView(R.layout.activity_values)
public class ValuesActivity extends RoboActivity {

  @InjectView(R.id.button_stop_reading_values)
  private Button buttonStopReading;

  @InjectView(R.id.pager_ports)
  private ViewPager portPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // TODO: Process the received intents
    // TODO: Write the adapter for the pager
  }
}
