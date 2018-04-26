package com.example.vinicius.marvelcharacters.dependency_injection.components;

import android.app.Application;
import android.content.Context;

import com.example.vinicius.marvelcharacters.App;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.ApplicationModule;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.NetworkModule;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent
{
	void inject(App app);

	@Named("ApplicationContext")
	Context context();

	Application application();

	DataManager getDataManager();
}
