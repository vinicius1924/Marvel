package com.example.vinicius.marvelcharacters.model.api;

import com.example.vinicius.marvelcharacters.DTO.CharacterDataDTO;

public class GetCharactersResponse
{
  private int code;
  private String status;
  private CharacterDataDTO data;

  public int getCode()
  {
    return code;
  }

  public String getStatus()
  {
    return status;
  }

  public CharacterDataDTO getData()
  {
    return data;
  }

  public void setData(CharacterDataDTO data)
  {
    this.data = data;
  }
}
