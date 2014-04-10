package com.bitalino.opensignals.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Port class is used as a wrapper around the {@link PortEnum}
 * in order to allow for i18n names.
 */
public class Port implements Parcelable {
  private String name;
  private Integer number;
  private String unit;
  private PortEnum port;

  /**
   * Instantiates a new Port.
   *
   * @param name the I18n name of the port
   * @param unit the I18n unit of the values read by the port
   * @param port the port type
   */
  public Port(String name, String unit, PortEnum port) {
    this.name = name;
    this.unit = unit;
    this.port = port;
  }

  public Port(Parcel in) {
    this.name = in.readString();
    this.unit = in.readString();
    this.port = (PortEnum) in.readValue(null);
    this.number = (Integer) in.readValue(null);
  }

  /**
   * Gets the port type.
   *
   * @return the port type
   */
  public PortEnum getPort() {
    return port;
  }

  /**
   * Gets the i18n name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets I18n unit.
   *
   * @return the unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Gets port number.
   *
   * @return the number
   */
  public int getNumber() {
    return number;
  }

  /**
   * Sets port number.
   *
   * @param number the number
   */
  public void setNumber(int number) {
    this.number = number;
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
    parcel.writeString(unit);
    parcel.writeValue(port);
    parcel.writeValue(number);
  }

  public static final Parcelable.Creator<Port> CREATOR
          = new Parcelable.Creator<Port>() {
    public Port createFromParcel(Parcel in) {
      return new Port(in);
    }

    public Port[] newArray(int size) {
      return new Port[size];
    }
  };
}
