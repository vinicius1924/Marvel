package com.example.vinicius.marvelcharacters.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.vinicius.marvelcharacters.App;
import com.example.vinicius.marvelcharacters.dependency_injection.components.ActivityComponent;
import com.example.vinicius.marvelcharacters.dependency_injection.components.DaggerActivityComponent;
import com.example.vinicius.marvelcharacters.dependency_injection.modules.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity implements MvpView
{
	private ActivityComponent mActivityComponent;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mActivityComponent = DaggerActivityComponent.builder()
				  .activityModule(new ActivityModule(this))
				  .applicationComponent(((App) getApplication()).getComponent())
				  .build();
	}

	public ActivityComponent getActivityComponent() {
		return mActivityComponent;
	}

	@Override
	public void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT);
	}
}
