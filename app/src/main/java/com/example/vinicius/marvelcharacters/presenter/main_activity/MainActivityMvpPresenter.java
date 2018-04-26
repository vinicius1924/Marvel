package com.example.vinicius.marvelcharacters.presenter.main_activity;

import android.view.View;

import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.presenter.base.MvpPresenter;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivityMvpView;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

public interface MainActivityMvpPresenter<V extends MainActivityMvpView> extends MvpPresenter<V>
{
	void loadCharacters(Disposable disposableObserver, Scheduler... schedulers);
	int recyclerViewNumberOfColumns();
	void onRecyclerViewItemClick(CharacterDTO characterDTO, View shredElementTransition);
	void onDestroy();
}
