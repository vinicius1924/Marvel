package com.example.vinicius.marvelcharacters.dependency_injection.components;

import com.example.vinicius.marvelcharacters.dependency_injection.PerActivity;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.ActivityModule;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivity;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent
{
	void inject(MainActivity activity);
	void inject(CharacterDetailActivity activity);
}
