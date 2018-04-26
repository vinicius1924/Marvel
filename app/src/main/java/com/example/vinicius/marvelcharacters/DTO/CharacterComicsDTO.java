package com.example.vinicius.marvelcharacters.DTO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

public class CharacterComicsDTO implements Parcelable, Cloneable
{
  private int available;
  private String collectionURI;
  private List<ItemsDTO> items;

  public CharacterComicsDTO()
  {
  }

  public CharacterComicsDTO(Parcel in)
  {
    readFromParcel(in);
  }

  private void readFromParcel(Parcel in)
  {
    available = in.readInt();
    collectionURI = in.readString();
    items = in.readArrayList(ItemsDTO.class.getClassLoader());
  }

  public int getAvailable()
  {
    return available;
  }

  public String getCollectionURI()
  {
    return collectionURI;
  }

  public List<ItemsDTO> getItems()
  {
    return items;
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags)
  {
    parcel.writeInt(available);
    parcel.writeString(collectionURI);
    parcel.writeList(items);
  }

  public static final Parcelable.Creator<CharacterComicsDTO> CREATOR = new Parcelable.Creator<CharacterComicsDTO>()
  {
    public CharacterComicsDTO createFromParcel(Parcel in)
    {
      return new CharacterComicsDTO(in);
    }

    public CharacterComicsDTO[] newArray(int size)
    {
      return new CharacterComicsDTO[size];
    }
  };

  public CharacterComicsDTO clone()
  {
    CharacterComicsDTO clone = null;

    try
    {
      clone = (CharacterComicsDTO) super.clone();
    }
    catch(CloneNotSupportedException e)
    {
      Log.e(this.getClass().toString(), this.getClass().toString() + " does not implement Cloneable");
    }

    return clone;
  }
}
