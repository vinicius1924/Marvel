package com.example.vinicius.marvelcharacters.model.data_manager;

import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;
import com.example.vinicius.marvelcharacters.model.api.base.ApiHelper;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AppDataManager implements DataManager
{
	private final ApiHelper mApiHelper;

	@Inject
	public AppDataManager(ApiHelper apiHelper)
	{
		this.mApiHelper = apiHelper;
	}

	@Override
	public Observable<GetCharactersResponse> getCharacters(String limit, String offset)
	{
		return mApiHelper.getCharacters(limit, offset);
	}

	@Override
	public Observable<GetCharacterComicsResponse> getCharacterComics(long characterId, String limit, String offset)
	{
		return mApiHelper.getCharacterComics(characterId, limit, offset);
	}
}
