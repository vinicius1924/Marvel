package com.example.vinicius.marvelcharacters.view.main_activity;


import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;
import com.example.vinicius.marvelcharacters.view.base.MvpView;

public interface MainActivityMvpView extends MvpView
{
	void getCharactersResponse(GetCharactersResponse response);
	void showSnackBar(String message);
	void showSnackBarWithLoadCharactersAction(String message);
	void showProgressBar();
	void hideProgressBar();
}
