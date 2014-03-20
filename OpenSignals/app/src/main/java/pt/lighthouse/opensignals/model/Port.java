package pt.lighthouse.opensignals.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Port class is used as a wrapper around the {@link pt.lighthouse.opensignals.model.PortEnum}
 * in order to allow for i18n names.
 */
public class Port implements Parcelable {
  private PortEnum port;
  private String name;

  /**
   * Instantiates a new Port.
   *
   * @param name the name
   * @param port the port
   */
  public Port(String name, PortEnum port) {
    this.port = port;
    this.name = name;
  }

  public PortEnum getPort() {
    return port;
  }

  public void setPort(PortEnum port) {
    this.port = port;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeString(name);
    parcel.writeValue(port);
  }
}
