package com.example.vinicius.marvelcharacters.presenter.base;


import com.example.vinicius.marvelcharacters.view.base.MvpView;

public interface MvpPresenter<V extends MvpView>
{
	void registerView(V mvpView);

	void unregisterView();
}
