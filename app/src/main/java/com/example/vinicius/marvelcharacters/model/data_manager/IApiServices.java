package com.example.vinicius.marvelcharacters.model.data_manager;

import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiServices
{
	@GET("characters")
	Observable<GetCharactersResponse> getCharacters(@Query("limit") String limit, @Query("offset") String offset);

	@GET("characters/{characterId}/comics")
	Observable<GetCharacterComicsResponse> getCharacterComics(@Path("characterId") String user, @Query("limit") String limit,
																														@Query("offset") String offset);
}
