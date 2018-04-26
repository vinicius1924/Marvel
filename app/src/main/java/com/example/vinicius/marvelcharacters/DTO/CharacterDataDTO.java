package com.example.vinicius.marvelcharacters.DTO;

import java.util.List;

public class CharacterDataDTO
{
  private int total;
  private List<CharacterDTO> results;

  public void setTotal(int total)
  {
    this.total = total;
  }

  public void setResults(List<CharacterDTO> results)
  {
    this.results = results;
  }

  public int getTotal()
  {
    return total;
  }

  public List<CharacterDTO> getResults()
  {
    return results;
  }
}
