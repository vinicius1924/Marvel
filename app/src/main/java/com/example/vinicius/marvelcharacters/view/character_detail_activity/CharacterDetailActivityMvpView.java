package com.example.vinicius.marvelcharacters.view.character_detail_activity;

import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.view.base.MvpView;

public interface CharacterDetailActivityMvpView extends MvpView
{
	void showSnackBarWithLoadCharacterComicsAction(String message);
	void showSnackBar(String message);
	void loadComicsResponse(GetCharacterComicsResponse getCharacterComicsResponse);
	void showProgressBar();
	void hideProgressBar();
	void showNoComicsText(boolean requestError);
	void hideNoComicsText();
	void showRecyclerView();
	void hideRecyclerView();
}
