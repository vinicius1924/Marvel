package com.example.vinicius.marvelcharacters.presenter.character_detail_activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.dependency_injection.PerActivity;
import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.example.vinicius.marvelcharacters.model.data_manager.base.DataManager;
import com.example.vinicius.marvelcharacters.presenter.base.BasePresenter;
import com.example.vinicius.marvelcharacters.utils.NetworkUtils;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivity;
import com.example.vinicius.marvelcharacters.view.character_detail_activity.CharacterDetailActivityMvpView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class CharacterDetailActivityPresenter<V extends CharacterDetailActivityMvpView> extends BasePresenter<V>
  implements CharacterDetailActivityMvpPresenter<V>, DataLoadingStatus, CharacterDetailActivity.SharedElementTransitionEndedListener
{
  @Named("ActivityContext")
  Context context;
  @Named("ApplicationContext")
  Context applicationContext;
  AppCompatActivity activity;
  private Observable<GetCharacterComicsResponse> callGetCharacterComicsResponse;
  private List<DataLoadingStatus.DataLoadingCallbacks> loadingCallbacks;
  private final AtomicInteger loadingCount;
  private int offset = 0;
  private int totalResults = -1;
  private final int limit = 100;
  private boolean showSnackBar = false;
  private boolean transitionEnded = false;
  private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  @Inject
  public CharacterDetailActivityPresenter(@Named("ActivityContext") Context context,
                                          @Named("ApplicationContext") Context applicationContext, DataManager dataManager,
                                          AppCompatActivity activity)
  {
    super(dataManager);
    this.context = context;
    this.applicationContext = applicationContext;
    this.activity = activity;
    loadingCount = new AtomicInteger(0);
  }

  @Override
  public void onDestroy()
  {
    unregisterView();
    mCompositeDisposable.clear();
  }

  @Override
  public void loadComics(long characterId)
  {
    if(offset == 0)
      getMvpView().showProgressBar();

    if(!allItemsLoaded())
    {
      if(NetworkUtils.isOnline(context))
      {
        callGetCharacterComicsResponse = getDataManager().getCharacterComics(characterId, String.valueOf(limit), String.valueOf(offset));

        offset = offset + limit;

        loadStarted();

        mCompositeDisposable.add(callGetCharacterComicsResponse.subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<GetCharacterComicsResponse>()
          {
            @Override
            public void accept(GetCharacterComicsResponse getCharacterComicsResponse) throws Exception
            {
              totalResults = getCharacterComicsResponse.getData().getTotal();
              loadFinished();
              getMvpView().hideProgressBar();

              if(totalResults == 0)
              {
                getMvpView().showNoComicsText(false);
              } else
              {
                getMvpView().showRecyclerView();
                getMvpView().loadComicsResponse(getCharacterComicsResponse);
              }
            }
          }, new Consumer<Throwable>()
          {
            @Override
            public void accept(Throwable throwable) throws Exception
            {
              offset = offset - limit;
              loadFinished();
              getMvpView().hideProgressBar();
              getMvpView().showNoComicsText(true);
              Log.e(CharacterDetailActivity.CHARACTERDETAILACTIVITYTAG, throwable.getLocalizedMessage());
            }
          }));
      } else
      {
        getMvpView().hideProgressBar();

        if(offset == 0)
        {
          if(transitionEnded)
          {
            getMvpView().showSnackBarWithLoadCharacterComicsAction(context.getResources().getString(R.string.no_internet_connection));
          }
          showSnackBar = true;
        } else
        {
          getMvpView().showSnackBar(context.getResources().getString(R.string.no_internet_connection));
        }
      }
    }
  }

  private void loadStarted()
  {
    if(loadingCount.getAndIncrement() == 0)
    {
      dispatchLoadingStartedCallbacks();
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

  private void loadFinished()
  {
    if(loadingCount.decrementAndGet() == 0)
    {
      dispatchLoadingFinishedCallbacks();
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

  private boolean allItemsLoaded()
  {
    if((offset < totalResults) || totalResults == -1)
      return false;

    return true;
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

  @Override
  public void onTransitionEnded()
  {
    transitionEnded = true;

    if(showSnackBar)
    {
      showSnackBar = false;
      getMvpView().showSnackBarWithLoadCharacterComicsAction(context.getResources().getString(R.string.no_internet_connection));
    }
  }
}
