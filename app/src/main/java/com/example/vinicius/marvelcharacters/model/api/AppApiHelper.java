package com.example.vinicius.marvelcharacters.model.api;


import com.example.vinicius.marvelcharacters.model.api.base.ApiHelper;
import com.example.vinicius.marvelcharacters.model.data_manager.IApiServices;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class AppApiHelper<T> implements ApiHelper<T>
{

	private IApiServices apiServices;

	public AppApiHelper(Retrofit retrofit)
	{
		apiServices = retrofit.create(IApiServices.class);
	}

	@Override
	public Observable<GetCharactersResponse> getCharacters(String limit, String offset)
	{
		return apiServices.getCharacters(limit, offset);
	}

	@Override
	public Observable<GetCharacterComicsResponse> getCharacterComics(long characterId, String limit, String offset)
	{
		return apiServices.getCharacterComics(String.valueOf(characterId), limit, offset);
	}
}
