package com.example.vinicius.marvelcharacters.presenter.main_activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.dependency_injection.PerActivity;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;
import com.example.vinicius.marvelcharacters.presenter.base.BasePresenter;
import com.example.vinicius.marvelcharacters.utils.NetworkUtils;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivity;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivity;
import com.example.vinicius.marvelcharacters.view.main_activity.MainActivityMvpView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class MainActivityPresenter<V extends MainActivityMvpView> extends BasePresenter<V>
  implements MainActivityMvpPresenter<V>, DataLoadingStatus
{
  @Named("ActivityContext")
  Context context;
  AppCompatActivity activity;
  private Observable<GetCharactersResponse> callCharactersResponse = null;
  private List<DataLoadingStatus.DataLoadingCallbacks> loadingCallbacks;
  private final AtomicInteger loadingCount;
  private int offset = 0;
  private int totalResults = -1;
  private final int limit = 30;
  private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
  Disposable loadCharactersObserver = null;


  @Inject
  public MainActivityPresenter(@Named("ActivityContext") Context context, DataManager dataManager, AppCompatActivity activity)
  {
    super(dataManager);
    this.context = context;
    this.activity = activity;
    loadingCount = new AtomicInteger(0);
  }

  @Override
  public void loadCharacters(Disposable disposableObserver, Scheduler... schedulers)
  {
    Scheduler subscribeScheduler;
    Scheduler observeScheduler;

    if(disposableObserver != null)
    {
      loadCharactersObserver = disposableObserver;
    }
    else
    {
      loadCharactersObserver = new DisposableObserver<GetCharactersResponse>()
      {
        @Override
        public void onNext(GetCharactersResponse getCharactersResponse)
        {
          totalResults = getCharactersResponse.getData().getTotal();
          loadFinished();
          getMvpView().hideProgressBar();
          getMvpView().getCharactersResponse(getCharactersResponse);
        }

        @Override
        public void onError(Throwable e)
        {
          offset = offset - limit;
          loadFinished();
          getMvpView().hideProgressBar();
          Log.e(((MainActivity) getMvpView()).MAINACTIVITYTAG, e.getLocalizedMessage());
        }

        @Override
        public void onComplete()
        {

        }
      };
    }

    if(schedulers.length == 2)
    {
      subscribeScheduler = schedulers[0];
      observeScheduler = schedulers[1];
    }
    else
    {
      subscribeScheduler = Schedulers.io();
      observeScheduler = AndroidSchedulers.mainThread();
    }

    if(offset == 0)
      getMvpView().showProgressBar();

    if(!allItemsLoaded())
    {
      if(NetworkUtils.isOnline(context))
      {
        callCharactersResponse = getDataManager().getCharacters(String.valueOf(limit), String.valueOf(offset));

        offset = offset + limit;

        loadStarted();

        callCharactersResponse.subscribeOn(subscribeScheduler)
          .observeOn(observeScheduler)
          .subscribe((Observer<GetCharactersResponse>) loadCharactersObserver);
      } else
      {
        getMvpView().hideProgressBar();

        if(offset == 0)
          getMvpView().showSnackBarWithLoadCharactersAction(context.getResources().getString(R.string.no_internet_connection));
        else
          getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
      }
    }
  }

  public boolean allItemsLoaded()
  {
    if((offset < totalResults) || totalResults == -1)
      return false;

    return true;
  }

  @Override
  public int recyclerViewNumberOfColumns()
  {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    /**
     * Esse valor deve ser ajustado de acordo com a largura do poster que será mostrado
     */
    int widthDivider = 300;

    int width = displayMetrics.widthPixels;
    int nColumns = width / widthDivider;
    if(nColumns < 2)
      return 2;
    return nColumns;
  }

  @Override
  public void onRecyclerViewItemClick(CharacterDTO characterDTO, View shredElementTransition)
  {
    Intent i = new Intent(context, CharacterDetailActivity.class);
    i.putExtra(CharacterDTO.PARCELABLE_KEY, characterDTO);
    i.putExtra(CharacterDetailActivity.TRANSITION_NAME, ViewCompat.getTransitionName(shredElementTransition));

    context.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity) getMvpView(),
      shredElementTransition, ViewCompat.getTransitionName(shredElementTransition)).toBundle());
  }

  @Override
  public void onDestroy()
  {
    //mCompositeDisposable.clear();
    loadCharactersObserver.dispose();
    unregisterView();
  }

  @Override
  public boolean isDataLoading()
  {
    return loadingCount.get() > 0;
  }

  @Override
  public void registerCallback(DataLoadingCallbacks callback)
  {
    if(loadingCallbacks == null)
    {
      loadingCallbacks = new ArrayList<>(1);
    }

    loadingCallbacks.add(callback);
  }

  private void loadStarted()
  {
    if(loadingCount.getAndIncrement() == 0)
    {
      dispatchLoadingStartedCallbacks();
    }
  }

  private void loadFinished()
  {
    if(loadingCount.decrementAndGet() == 0)
    {
      dispatchLoadingFinishedCallbacks();
    }
  }

  private void dispatchLoadingStartedCallbacks()
  {
    if(loadingCallbacks == null || loadingCallbacks.isEmpty())
      return;

    for(DataLoadingCallbacks loadingCallback : loadingCallbacks)
    {
      loadingCallback.dataStartedLoading();
    }
  }

  private void dispatchLoadingFinishedCallbacks()
  {
    if(loadingCallbacks == null || loadingCallbacks.isEmpty())
      return;

    for(DataLoadingCallbacks loadingCallback : loadingCallbacks)
    {
      loadingCallback.dataFinishedLoading();
    }
  }
}
