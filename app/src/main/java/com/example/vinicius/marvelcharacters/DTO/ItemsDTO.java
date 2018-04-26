package com.example.vinicius.marvelcharacters.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ItemsDTO implements Parcelable, Cloneable
{
  private String resourceURI;
  private String name;

  public ItemsDTO()
  {
  }

  public ItemsDTO(Parcel in)
  {
    readFromParcel(in);
  }

  private void readFromParcel(Parcel in)
  {
    resourceURI = in.readString();
    name = in.readString();
  }

  public String getResourceURI()
  {
    return resourceURI;
  }

  public String getName()
  {
    return name;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags)
  {
    parcel.writeString(resourceURI);
    parcel.writeString(name);
  }

  public static final Parcelable.Creator<ItemsDTO> CREATOR = new Parcelable.Creator<ItemsDTO>()
  {
    public ItemsDTO createFromParcel(Parcel in)
    {
      return new ItemsDTO(in);
    }

    public ItemsDTO[] newArray(int size)
    {
      return new ItemsDTO[size];
    }
  };

  public ItemsDTO clone()
  {
    ItemsDTO clone = null;

    try
    {
      clone = (ItemsDTO) super.clone();
    }
    catch(CloneNotSupportedException e)
    {
      Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
    }

    return clone;
  }
}
