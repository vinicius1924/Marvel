package com.example.vinicius.marvelcharacters.model.api;

import com.example.vinicius.marvelcharacters.DTO.ComicsDataDTO;

public class GetCharacterComicsResponse
{
  private int code;
  private String status;
  private ComicsDataDTO data;

  public int getCode()
  {
    return code;
  }

  public String getStatus()
  {
    return status;
  }

  public ComicsDataDTO getData()
  {
    return data;
  }
}
