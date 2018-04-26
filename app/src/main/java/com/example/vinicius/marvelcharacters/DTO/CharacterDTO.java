package com.example.vinicius.marvelcharacters.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CharacterDTO implements Parcelable, Cloneable
{
  private long id;
  private String name;
  private String description;
  private ThumbnailDTO thumbnail;
  private CharacterComicsDTO comics;
  public static final String PARCELABLE_KEY = "character";

  public CharacterDTO()
  {
  }

  public CharacterDTO(Parcel in)
  {
    readFromParcel(in);
  }

  private void readFromParcel(Parcel in)
  {
    id = in.readLong();
    name = in.readString();
    description = in.readString();
    thumbnail = (ThumbnailDTO) in.readParcelable(ThumbnailDTO.class.getClassLoader());
    comics = (CharacterComicsDTO) in.readParcelable(CharacterComicsDTO.class.getClassLoader());
  }

  public long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public ThumbnailDTO getThumbnail()
  {
    return thumbnail;
  }

  public CharacterComicsDTO getComics()
  {
    return comics;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags)
  {
    parcel.writeLong(id);
    parcel.writeString(name);
    parcel.writeString(description);
    parcel.writeParcelable(thumbnail, flags);
    parcel.writeParcelable(comics, flags);
  }

  public static final Creator<CharacterDTO> CREATOR = new Creator<CharacterDTO>()
  {
    public CharacterDTO createFromParcel(Parcel in)
    {
      return new CharacterDTO(in);
    }

    public CharacterDTO[] newArray(int size)
    {
      return new CharacterDTO[size];
    }
  };

  public CharacterDTO clone()
  {
    CharacterDTO clone = null;

    try
    {
      clone = (CharacterDTO) super.clone();
    }
    catch(CloneNotSupportedException e)
    {
      Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
    }

    return clone;
  }
}
