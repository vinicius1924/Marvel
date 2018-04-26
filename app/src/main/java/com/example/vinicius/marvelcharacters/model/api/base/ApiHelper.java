package com.example.vinicius.marvelcharacters.model.api.base;

import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;

import io.reactivex.Observable;

public interface ApiHelper<T>
{
	Observable<GetCharactersResponse> getCharacters(String limit, String offset);
	Observable<GetCharacterComicsResponse> getCharacterComics(long characterId, String limit, String offset);
}
