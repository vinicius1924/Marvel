package com.example.vinicius.marvelcharacters.presenter.base;

import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;
import com.example.vinicius.marvelcharacters.view.base.MvpView;

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V>
{
	private final DataManager mDataManager;
	private V mMvpView;

	public BasePresenter(DataManager dataManager)
	{
		this.mDataManager = dataManager;
	}

	@Override
	public void registerView(V mvpView)
	{
		mMvpView = mvpView;
	}

	@Override
	public void unregisterView()
	{
		mMvpView = null;
	}

	public V getMvpView() throws NullPointerException
	{
		if(mMvpView != null)
		{
			return mMvpView;
		}
		else
		{
			throw new NullPointerException("View is unavailable");
		}
	}

	public DataManager getDataManager() throws NullPointerException
	{
		if(mDataManager != null)
		{
			return mDataManager;
		}
		else
		{
			throw new NullPointerException("DataManager is unavailable");
		}
	}
}
