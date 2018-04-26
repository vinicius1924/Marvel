package com.example.vinicius.marvelcharacters.dependency_injection.modules;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.vinicius.marvelcharacters.dependency_injection.PerActivity;
import com.example.vinicius.marvelcharacters.presenter.character_detail_activity.CharacterDetailActivityMvpPresenter;
import com.example.vinicius.marvelcharacters.presenter.character_detail_activity.CharacterDetailActivityPresenter;
import com.example.vinicius.marvelcharacters.presenter.main_activity.MainActivityMvpPresenter;
import com.example.vinicius.marvelcharacters.presenter.main_activity.MainActivityPresenter;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivityMvpView;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivityMvpView;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule
{
	private AppCompatActivity mActivity;

	public ActivityModule(AppCompatActivity activity)
	{
		this.mActivity = activity;
	}

	@Provides
	@Named("ActivityContext")
	Context provideContext()
	{
		return mActivity;
	}

	@Provides
	AppCompatActivity provideActivity()
	{
		return mActivity;
	}

	@Provides
	@PerActivity
	MainActivityMvpPresenter<MainActivityMvpView> provideMainActivityMvpPresenter(MainActivityPresenter<MainActivityMvpView> presenter)
	{
		return presenter;
	}

	@Provides
	@PerActivity
	CharacterDetailActivityMvpPresenter<CharacterDetailActivityMvpView> provideCharacterDetailActivityMvpPresenter
		(CharacterDetailActivityPresenter<CharacterDetailActivityMvpView> presenter)
	{
		return presenter;
	}
}
