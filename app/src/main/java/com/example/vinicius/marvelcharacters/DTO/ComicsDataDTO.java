package com.example.vinicius.marvelcharacters.DTO;

import java.util.List;

public class ComicsDataDTO
{
  private int total;
  private List<ComicDTO> results;

  public int getTotal()
  {
    return total;
  }

  public List<ComicDTO> getResults()
  {
    return results;
  }
}
