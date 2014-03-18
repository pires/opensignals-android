package pt.lighthouse.opensignals.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Map;

import pt.lighthouse.opensignals.R;
import pt.lighthouse.opensignals.model.Port;
import pt.lighthouse.opensignals.model.PortEnum;

/**
 * Created by fork on 18/03/14.
 */
public class PortViewPagerAdapter extends FragmentPagerAdapter {

  private Map<Integer, Port> portInfo;
  private Context context;

  public PortViewPagerAdapter(FragmentManager fm, Map<Integer, Port> portInfo) {
    super(fm);
    this.portInfo = portInfo;
  }

  @Override
  public Fragment getItem(int position) {
    return null;
  }

  @Override
  public CharSequence getPageTitle(int position) {
    String portDescription = portInfo.get(position).getName();
    return context.getString(R.string.label_port_title, position, portDescription);
  }

  @Override
  public int getCount() {
    int count = 0;
    for (Port port : portInfo.values()){
      if (port.getPort() != PortEnum.NONE)
        count++;
    }
    return count;
  }
}
