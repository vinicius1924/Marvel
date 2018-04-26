package com.example.vinicius.marvelcharacters.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ThumbnailDTO implements Parcelable, Cloneable
{
  private String path;
  private String extension;

  public ThumbnailDTO()
  {
  }

  public ThumbnailDTO(Parcel in)
  {
    readFromParcel(in);
  }

  private void readFromParcel(Parcel in)
  {
    path = in.readString();
    extension = in.readString();
  }

  public String getPath()
  {
    return path;
  }

  public String getExtension()
  {
    return extension;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags)
  {
    parcel.writeString(path);
    parcel.writeString(extension);
  }

  public static final Creator<ThumbnailDTO> CREATOR = new Creator<ThumbnailDTO>()
  {
    public ThumbnailDTO createFromParcel(Parcel in)
    {
      return new ThumbnailDTO(in);
    }

    public ThumbnailDTO[] newArray(int size)
    {
      return new ThumbnailDTO[size];
    }
  };

  public ThumbnailDTO clone()
  {
    ThumbnailDTO clone = null;

    try
    {
      clone = (ThumbnailDTO) super.clone();
    }
    catch(CloneNotSupportedException e)
    {
      Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
    }

    return clone;
  }
}
