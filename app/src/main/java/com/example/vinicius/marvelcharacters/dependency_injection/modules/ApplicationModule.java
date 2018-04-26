package com.example.vinicius.marvelcharacters.dependency_injection.modules;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;

import com.example.vinicius.marvelcharacters.model.api.AppApiHelper;
import com.example.vinicius.marvelcharacters.model.api.base.ApiHelper;
import com.example.vinicius.marvelcharacters.model.data_manager.AppDataManager;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApplicationModule
{
	private final Application mApplication;

	public ApplicationModule(Application mApplication)
	{
		this.mApplication = mApplication;
	}

	@Provides
	@Named("ApplicationContext")
	Context provideContext()
	{
		return mApplication;
	}

	@Provides
	Application provideApplication() {
		return mApplication;
	}

	@Provides
	ContentValues provideContentValues() {
		return new ContentValues();
	}

	@Provides
	@Singleton
	DataManager provideDataManager(AppDataManager appDataManager)
	{
		return appDataManager;
	}

	@Provides
	@Singleton
	ApiHelper provideApiHelper(Retrofit retrofit)
	{
		return new AppApiHelper<>(retrofit);
	}
}
