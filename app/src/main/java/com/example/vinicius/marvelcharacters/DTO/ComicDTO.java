package com.example.vinicius.marvelcharacters.DTO;

public class ComicDTO
{
  private long id;
  private String title;
  private ComicThumbnailDTO thumbnail;

  public long getId()
  {
    return id;
  }

  public String getTitle()
  {
    return title;
  }

  public ComicThumbnailDTO getThumbnail()
  {
    return thumbnail;
  }
}
