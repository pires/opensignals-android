package com.bitalino.opensignals.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import com.bitalino.opensignals.R;
import com.bitalino.opensignals.fragments.PortPageFragment;
import com.bitalino.opensignals.model.Port;

/**
 * Created on 18/03/14.
 */
public class PortViewPagerAdapter extends FragmentPagerAdapter {

  private List<Port> ports;
  private Context context;

  public PortViewPagerAdapter(Context context, FragmentManager fm, List<Port> ports) {
    super(fm);
    this.context = context;
    this.ports = ports;
  }

  @Override
  public Fragment getItem(int position) {
    return PortPageFragment.newInstance(ports.get(position));
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return context.getString(R.string.label_port_title, ports.get(position).getNumber());
  }

  @Override
  public int getCount() {
    return ports.size();
  }
}
