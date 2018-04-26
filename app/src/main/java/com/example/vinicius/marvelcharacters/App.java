package com.example.vinicius.marvelcharacters;

import android.app.Application;

import com.example.vinicius.marvelcharacters.dependency_injection.components.ApplicationComponent;
import com.example.vinicius.marvelcharacters.dependency_injection.components.DaggerApplicationComponent;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.ApplicationModule;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.NetworkModule;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;

import javax.inject.Inject;

public class App extends Application
{
  @Inject
  DataManager mDataManager;

  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate()
  {
    super.onCreate();

    mApplicationComponent = DaggerApplicationComponent.builder()
      .applicationModule(new ApplicationModule(this))
      .networkModule(new NetworkModule("https://gateway.marvel.com/v1/public/"))
      .build();

    mApplicationComponent.inject(this);
  }

  public ApplicationComponent getComponent() {
    return mApplicationComponent;
  }

}
