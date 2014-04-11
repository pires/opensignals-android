package com.bitalino.opensignals.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoFrame;
import com.bitalino.opensignals.BITalinoSeries;
import com.bitalino.opensignals.BITalinoSeriesObserver;
import com.bitalino.opensignals.R;
import com.bitalino.opensignals.adapters.PortViewPagerAdapter;
import com.bitalino.opensignals.model.Port;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * The Values activity is where the readings from the bitalino will be shown.
 */
@ContentView(R.layout.activity_values)
public class ValuesActivity extends RoboFragmentActivity {

  private static final String TAG = "OpenSignals";

  /*
   * http://developer.android.com/reference/android/bluetooth/BluetoothDevice.html
   * #createRfcommSocketToServiceRecord(java.util.UUID)
   *
   * "Hint: If you are connecting to a Bluetooth serial board then try using the
   * well-known SPP UUID 00001101-0000-1000-8000-00805F9B34FB. However if you
   * are connecting to an Android peer then please generate your own unique
   * UUID."
   */
  private static final UUID MY_UUID = UUID
      .fromString("00001101-0000-1000-8000-00805F9B34FB");

  @InjectView(R.id.button_stop_reading_values)
  private Button buttonStopReading;

  @InjectView(R.id.pager_ports)
  private ViewPager portPager;

  private String macAddress;

  BITalinoSeries seriesPort1;
  private ReadBITalinoAsyncTask task = new ReadBITalinoAsyncTask();
  private int frameCounter = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Intent intent = getIntent();
    if (intent != null) {
      macAddress = intent.getExtras().getString(MainActivity.EXTRA_MACADDRESS);
      final List<Port> ports = intent.getParcelableArrayListExtra(MainActivity.EXTRA_PORTS);

      seriesPort1 = new BITalinoSeries(ports.get(0).getName());
      BITalinoSeriesObserver observerPort1 = new BITalinoSeriesObserver();
      seriesPort1.addObserver(observerPort1);

      List<BITalinoSeries> series = new ArrayList<>(6);
      series.add(seriesPort1);

      List<BITalinoSeriesObserver> observers = new ArrayList<>(6);
      observers.add(observerPort1);

      portPager.setAdapter(new PortViewPagerAdapter(this, getSupportFragmentManager(), ports, series, observers));
    } else {
      throw new IllegalStateException("This activity needs to be started from an intent.");
    }

    task.execute();

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
            task.cancel(true);
            task = null;
            ValuesActivity.super.onBackPressed();
          }
        })
        .setNegativeButton(R.string.button_no, null)
        .show();
  }

  private class ReadBITalinoAsyncTask extends AsyncTask<Void, BITalinoFrame, Void> {
    private BluetoothDevice dev = null;
    private BluetoothSocket sock = null;
    private InputStream is = null;
    private OutputStream os = null;
    private BITalinoDevice bitalino = null;

    @Override
    protected Void doInBackground(Void... paramses) {
      try {
        // Let's get the remote Bluetooth device
        final String remoteDevice = macAddress;

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        dev = btAdapter.getRemoteDevice(remoteDevice);

    /*
     * Establish Bluetooth connection
     *
     * Because discovery is a heavyweight procedure for the Bluetooth adapter,
     * this method should always be called before attempting to connect to a
     * remote device with connect(). Discovery is not managed by the Activity,
     * but is run as a system service, so an application should always call
     * cancel discovery even if it did not directly request a discovery, just to
     * be sure. If Bluetooth state is not STATE_ON, this API will return false.
     *
     * see
     * http://developer.android.com/reference/android/bluetooth/BluetoothAdapter
     * .html#cancelDiscovery()
     */
        Log.d(TAG, "Stopping Bluetooth discovery.");
        btAdapter.cancelDiscovery();

        sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
        sock.connect();

        BITalinoDevice bitalino = new BITalinoDevice(1000, new int[]{0, 1, 2, 3, 4, 5});
        Log.i(TAG, "Connecting to BITalino [" + remoteDevice + "]..");
        bitalino.open(sock.getInputStream(), sock.getOutputStream());
        Log.i(TAG, "Connected.");

        // get BITalino version
        Log.i(TAG, "Version: " + bitalino.version());

        // start acquisition on predefined analog channels
        bitalino.start();

        // read n samples
        final int numberOfSamplesToRead = 1000;
        Log.i(TAG, "Reading " + numberOfSamplesToRead + " samples..");
        while (!isCancelled()) {
          BITalinoFrame[] frames = bitalino.read(numberOfSamplesToRead);
          publishProgress(frames);
        }
      } catch (Exception e) {
        Log.e(TAG, "There was an error.", e);
      }

      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      releaseBITalino();
    }

    @Override
    protected void onProgressUpdate(BITalinoFrame... frames) {
      // update series
      for (BITalinoFrame frame : frames) {
        // don't overflow series buffer, but maintain some history
        if (seriesPort1.size() > 2500)
          seriesPort1.removeFirst();

        // add value each 50 frames count
        if (frameCounter % 50 == 0)
          seriesPort1.addLast(frameCounter, frame.getAnalog(0));
        frameCounter++;
      }
    }

    @Override
    protected void onCancelled() {
      releaseBITalino();
    }

    private void releaseBITalino() {
      try {
        // stop acquisition and close bluetooth connection
        bitalino.stop();
        Log.i(TAG, "BITalino is stopped");
        sock.close();
        Log.i(TAG, "And we're done! :-)");
      } catch (Exception e) {
        // ignore it
      }
    }

  }

}