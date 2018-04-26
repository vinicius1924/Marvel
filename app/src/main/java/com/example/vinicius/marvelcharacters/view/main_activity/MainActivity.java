package com.example.vinicius.marvelcharacters.view.main_activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.model.api.GetCharactersResponse;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.example.vinicius.marvelcharacters.presenter.main_activity.MainActivityMvpPresenter;
import com.example.vinicius.marvelcharacters.view.base.BaseActivity;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.CharactersRecyclerAdapter;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.EndlessRecyclerOnScrollListener;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.SlideInItemAnimator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainActivityMvpView, CharactersRecyclerAdapter.ListItemClickListener
{
  private RecyclerView recyclerViewCharacters;
  private CoordinatorLayout coordinatorLayout;
  private Toolbar toolbar;
  private Snackbar snackbar = null;
  private Snackbar snackbarWithAction = null;
  private ProgressBar progressBar;
  private CharactersRecyclerAdapter charactersRecyclerAdapter;
  public static final String MAINACTIVITYTAG = "MainActivity";
  private List<CharacterDTO> charactersList = new ArrayList();

  @Inject
  MainActivityMvpPresenter<MainActivityMvpView> mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getActivityComponent().inject(this);
    mPresenter.registerView(this);

    recyclerViewCharacters = findViewById(R.id.recycler_view_characters);
    toolbar = findViewById(R.id.toolbar);
    coordinatorLayout = findViewById(R.id.coordinatorLayout);
    progressBar = findViewById(R.id.progressBar);

    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayShowTitleEnabled(false);

    recyclerViewCharacters.addOnScrollListener(new EndlessRecyclerOnScrollListener((DataLoadingStatus) mPresenter)
    {
      @Override
      public void onLoadMore()
      {
          mPresenter.loadCharacters(null);
      }
    });

    int numberOfColumns = mPresenter.recyclerViewNumberOfColumns();

    charactersRecyclerAdapter = new CharactersRecyclerAdapter(this.getApplicationContext(), (DataLoadingStatus) mPresenter,
      charactersList, this, numberOfColumns);
    charactersRecyclerAdapter.setHasStableIds(true);

    GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);

    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override
      public int getSpanSize(int position)
      {
        return charactersRecyclerAdapter.getItemColumnSpan(position);
      }
    });
    recyclerViewCharacters.setLayoutManager(gridLayoutManager);

    recyclerViewCharacters.setHasFixedSize(true);
    recyclerViewCharacters.setItemAnimator(new SlideInItemAnimator());

    recyclerViewCharacters.setAdapter(charactersRecyclerAdapter);

    mPresenter.loadCharacters(null);
  }

  @Override
  public void getCharactersResponse(GetCharactersResponse response)
  {
    charactersList.addAll(response.getData().getResults());
    charactersRecyclerAdapter.notifyDataSetChanged();
  }

  @Override
  public void showSnackBar(String message)
  {
    if(snackbar == null)
    {
      snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
      snackbar.show();
    }
    else
    {
      if(!snackbar.isShown())
      {
        snackbar.show();
      }
    }
  }

  @Override
  public void showSnackBarWithLoadCharactersAction(String message)
  {
    snackbarWithAction = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
      .setAction(getResources().getString(R.string.retry), new View.OnClickListener()
      {
        @Override
        public void onClick(View view)
        {
          snackbarWithAction.dismiss();
          mPresenter.loadCharacters(null);
        }
      });

    snackbarWithAction.show();
  }

  @Override
  public void showProgressBar()
  {
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressBar()
  {
    progressBar.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onListItemClick(int clickedItemIndex, ImageView thumbnailImage)
  {
    CharacterDTO characterDTO = charactersList.get(clickedItemIndex).clone();
    mPresenter.onRecyclerViewItemClick(characterDTO, thumbnailImage);
  }

  @Override
  protected void onDestroy()
  {
    mPresenter.onDestroy();
    super.onDestroy();
  }
}
