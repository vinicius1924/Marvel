package com.example.vinicius.marvelcharacters.presenter.character_detail_activity;

import com.example.vinicius.marvelcharacters.presenter.base.MvpPresenter;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivityMvpView;

public interface CharacterDetailActivityMvpPresenter<V extends CharacterDetailActivityMvpView> extends MvpPresenter<V>
{
	void onDestroy();
	void loadComics(long characterId);
}
