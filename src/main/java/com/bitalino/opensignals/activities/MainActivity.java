package com.bitalino.opensignals.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.bitalino.opensignals.R;
import com.bitalino.opensignals.model.Port;
import com.bitalino.opensignals.model.PortEnum;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * The Main activity, where the user must input a MAC address and select which sensors are plugged in each port.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private List<Port> portList;

  public static final String EXTRA_MACADDRESS = "macAddress";
  public static final String EXTRA_PORTS = "ports";

  @InjectView(R.id.field_mac_address)
  private EditText txtMacAddress;

  @InjectView(R.id.spinner_port_one)
  private Spinner spinnerPortOne;

  @InjectView(R.id.spinner_port_two)
  private Spinner spinnerPortTwo;

  @InjectView(R.id.spinner_port_three)
  private Spinner spinnerPortThree;

  @InjectView(R.id.spinner_port_four)
  private Spinner spinnerPortFour;

  @InjectView(R.id.spinner_port_five)
  private Spinner spinnerPortFive;

  @InjectView(R.id.spinner_port_six)
  private Spinner spinnerPortSix;

  @InjectView(R.id.button_connect)
  private Button buttonConnect;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initiate the port list and populate it
    portList = new ArrayList<>();
    portList.add(new Port(getString(R.string.port_none), getString(R.string.none_unit), PortEnum.NONE));
    portList.add(new Port(getString(R.string.port_ecg), getString(R.string.ecg_unit), PortEnum.ECG));
    portList.add(new Port(getString(R.string.port_emg), getString(R.string.emg_unit), PortEnum.EMG));
    portList.add(new Port(getString(R.string.port_eda), getString(R.string.eda_unit), PortEnum.EDA));
    portList.add(new Port(getString(R.string.port_lux), getString(R.string.lux_unit), PortEnum.LUX));
    portList.add(new Port(getString(R.string.port_acc), getString(R.string.acc_unit), PortEnum.ACC));

    // Create a simple Array Adapter and pass it on to the spinners
    ArrayAdapter<Port> adapter = new ArrayAdapter<>(this, R.layout.port_spinner_item, portList);
    spinnerPortOne.setAdapter(adapter);
    spinnerPortTwo.setAdapter(adapter);
    spinnerPortThree.setAdapter(adapter);
    spinnerPortFour.setAdapter(adapter);
    spinnerPortFive.setAdapter(adapter);
    spinnerPortSix.setAdapter(adapter);

    buttonConnect.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loadNextActivity();
      }
    });
  }

  /**
   * Utility method to store the port configuration and launch the next configuration
   */
  private void loadNextActivity() {
    Intent readingsIntent = new Intent(this, ValuesActivity.class);

    ArrayList<Port> ports = new ArrayList<>();

    Port port1 = (Port) spinnerPortOne.getSelectedItem();
    checkAndAddPort(ports, port1, 1);

    Port port2 = (Port) spinnerPortTwo.getSelectedItem();
    checkAndAddPort(ports, port2, 2);

    Port port3 = (Port) spinnerPortThree.getSelectedItem();
    checkAndAddPort(ports, port3, 3);

    Port port4 = (Port) spinnerPortFour.getSelectedItem();
    checkAndAddPort(ports, port4, 4);

    Port port5 = (Port) spinnerPortFive.getSelectedItem();
    checkAndAddPort(ports, port5, 5);

    Port port6 = (Port) spinnerPortSix.getSelectedItem();
    checkAndAddPort(ports, port6, 6);

    if (ports.size() > 0) {
      readingsIntent.putExtra(EXTRA_MACADDRESS, txtMacAddress.getText().toString());
      readingsIntent.putExtra(EXTRA_PORTS, ports);
      startActivity(readingsIntent);
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage(R.string.message_no_ports).setPositiveButton(R.string.label_ok, null).show();
    }
  }

  /**
   * Utility method to check the nullability of a port and add it to the extra array that will be stored in the intent
   *
   * @param ports      the array where the port will be added
   * @param port1      the port to add
   * @param portNumber the port number associated with the port
   */
  private void checkAndAddPort(ArrayList<Port> ports, Port port1, Integer portNumber) {
    if (port1 != null && port1.getPort() != PortEnum.NONE) {
      port1.setNumber(portNumber);
      ports.add(port1);
    }
  }
}
