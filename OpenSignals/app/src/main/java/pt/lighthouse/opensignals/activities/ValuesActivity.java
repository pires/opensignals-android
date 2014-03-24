package pt.lighthouse.opensignals.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pt.lighthouse.opensignals.R;
import pt.lighthouse.opensignals.adapters.PortViewPagerAdapter;
import pt.lighthouse.opensignals.model.Port;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * The Values activity is where the readings from the bitalino will be shown.
 */
@ContentView(R.layout.activity_values)
public class ValuesActivity extends RoboFragmentActivity {

  @InjectView(R.id.button_stop_reading_values)
  private Button buttonStopReading;

  @InjectView(R.id.pager_ports)
  private ViewPager portPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    if (intent != null) {
      List<Port> ports = intent.getParcelableArrayListExtra(MainActivity.EXTRA_PORTS);
      portPager.setAdapter(new PortViewPagerAdapter(this, getSupportFragmentManager(), ports));
    } else {
      throw new IllegalStateException("This activity needs to be started from an intent.");
    }

    buttonStopReading.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
  }

  @Override
  public void onBackPressed() {
    AlertDialog.Builder builder = new AlertDialog.Builder(ValuesActivity.this);
    builder.setMessage(R.string.message_stop_readings)
            .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                ValuesActivity.super.onBackPressed();
              }
            })
            .setNegativeButton(R.string.button_no, null)
            .show();
  }
}
