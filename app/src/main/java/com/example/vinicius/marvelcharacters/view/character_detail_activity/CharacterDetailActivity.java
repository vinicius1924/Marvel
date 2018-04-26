package com.example.vinicius.marvelcharacters.view.character_detail_activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.DTO.ComicDTO;
import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.model.api.GetCharacterComicsResponse;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.example.vinicius.marvelcharacters.presenter.character_detail_activity.CharacterDetailActivityMvpPresenter;
import com.example.vinicius.marvelcharacters.view.base.BaseActivity;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.EndlessRecyclerOnScrollListener;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.SlideInItemAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CharacterDetailActivity extends BaseActivity implements CharacterDetailActivityMvpView
{
  private Toolbar toolbar;
  private CollapsingToolbarLayout collapsingToolbarLayout;
  private CharacterDTO characterDTO;
  private int mMutedColor = 0xFF333333;
  private ImageView mPhotoView;
  private RecyclerView recyclerViewComics;
  private ComicsRecyclerAdapter comicsRecyclerAdapter;
  private TextView descriptionText;
  private CoordinatorLayout coordinatorLayout;
  private Snackbar snackbar;
  private List<ComicDTO> comicsList = new ArrayList();
  private ProgressBar progressBar;
  private TextView noComicsText;
  private SharedElementTransitionEndedListener sharedElementTransitionEndedListener;

  public static final String TRANSITION_NAME = "TRANSITION_NAME";
  public static final String CHARACTERDETAILACTIVITYTAG = "CharacterDetailActivity";

  @Inject
  CharacterDetailActivityMvpPresenter<CharacterDetailActivityMvpView> mPresenter;

  public interface SharedElementTransitionEndedListener
  {
    void onTransitionEnded();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_character_detail);

    getActivityComponent().inject(this);
    mPresenter.registerView(this);
    sharedElementTransitionEndedListener = (SharedElementTransitionEndedListener) mPresenter;

    toolbar = findViewById(R.id.toolbar_article);
    collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
    mPhotoView = findViewById(R.id.photo);
    recyclerViewComics = findViewById(R.id.recycler_view_comics);
    descriptionText = findViewById(R.id.descriptionText);
    coordinatorLayout = findViewById(R.id.coordinatorLayout);
    progressBar = findViewById(R.id.progressBar);
    noComicsText = findViewById(R.id.noComicsText);

    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayShowTitleEnabled(false);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Bundle bundle = getIntent().getExtras();
    characterDTO = bundle.getParcelable(CharacterDTO.PARCELABLE_KEY);
    mPhotoView.setTransitionName(bundle.getString(TRANSITION_NAME));

    recyclerViewComics.addOnScrollListener(new EndlessRecyclerOnScrollListener((DataLoadingStatus) mPresenter)
    {
      @Override
      public void onLoadMore()
      {
        mPresenter.loadComics(characterDTO.getId());
      }
    });

    if(toolbar != null)
    {
      toolbar.setTitle(characterDTO.getName());
    }

    if(!characterDTO.getDescription().isEmpty())
    {
      descriptionText.setText(characterDTO.getDescription());
    } else
    {
      descriptionText.setText("No description provided");
    }

    loadImage();

    mPresenter.loadComics(characterDTO.getId());

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerViewComics.setLayoutManager(layoutManager);

    recyclerViewComics.setHasFixedSize(true);

    comicsRecyclerAdapter = new ComicsRecyclerAdapter(this.getApplicationContext(), (DataLoadingStatus) mPresenter, comicsList);
    recyclerViewComics.setAdapter(comicsRecyclerAdapter);

    recyclerViewComics.setItemAnimator(new SlideInItemAnimator());
  }

  private void loadImage()
  {
    if(addTransitionListener())
    {
      loadThumbnail();
    } else
    {
      loadFullSizeImage();
    }
  }

  /**
   * Try and add a {@link Transition.TransitionListener} to the entering shared element
   * {@link Transition}. We do this so that we can load the full-size image after the transition
   * has completed.
   *
   * @return true if we were successful in adding a listener to the enter transition
   */
  private boolean addTransitionListener()
  {
    final Transition transition = getWindow().getSharedElementEnterTransition();

    if(transition != null)
    {
      // There is an entering shared element transition so add a listener to it
      transition.addListener(new Transition.TransitionListener()
      {
        @Override
        public void onTransitionEnd(Transition transition)
        {
          // As the transition has ended, we can now load the full-size image
          loadFullSizeImage();
          sharedElementTransitionEndedListener.onTransitionEnded();

          // Make sure we remove ourselves as a listener
          transition.removeListener(this);
        }

        @Override
        public void onTransitionStart(Transition transition)
        {
          // No-op
        }

        @Override
        public void onTransitionCancel(Transition transition)
        {
          // Make sure we remove ourselves as a listener
          transition.removeListener(this);
        }

        @Override
        public void onTransitionPause(Transition transition)
        {
          // No-op
        }

        @Override
        public void onTransitionResume(Transition transition)
        {
          // No-op
        }
      });
      return true;
    }

    // If we reach here then we have not added a listener
    return false;
  }

  /**
   * Load the item's thumbnail image into our {@link ImageView}.
   */
  private void loadThumbnail()
  {
    String imageUrl = characterDTO.getThumbnail().getPath() + "/portrait_uncanny." +
      characterDTO.getThumbnail().getExtension();

    Picasso.get()
      .load(imageUrl)
      .noFade()
      .into(mPhotoView);
  }

  /**
   * Load the item's full-size image into our {@link ImageView}.
   */
  private void loadFullSizeImage()
  {

    String imageUrl = characterDTO.getThumbnail().getPath() + "." +
      characterDTO.getThumbnail().getExtension();

    Picasso.get()
      .load(imageUrl)
      .noFade()
      .noPlaceholder()
      .into(mPhotoView);
  }

  @Override
  public void showSnackBarWithLoadCharacterComicsAction(String message)
  {
    snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE)
      .setAction(getResources().getString(R.string.retry), new View.OnClickListener()
      {
        @Override
        public void onClick(View view)
        {
          snackbar.dismiss();
          mPresenter.loadComics(characterDTO.getId());
        }
      });

    snackbar.show();
  }

  @Override
  public void showSnackBar(String message)
  {
    if(snackbar == null)
    {
      snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
      snackbar.show();
    } else
    {
      if(!snackbar.isShown())
      {
        snackbar.show();
      }
    }
  }

  @Override
  public void loadComicsResponse(GetCharacterComicsResponse getCharacterComicsResponse)
  {
    comicsList.addAll(getCharacterComicsResponse.getData().getResults());
    comicsRecyclerAdapter.notifyDataSetChanged();
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
  public void showNoComicsText(boolean requestError)
  {
    noComicsText.setVisibility(View.VISIBLE);

    if(requestError)
    {
      noComicsText.setText(R.string.request_error);
    } else
    {
      noComicsText.setText(R.string.no_comics_text);
    }
  }

  @Override
  public void hideNoComicsText()
  {
    noComicsText.setVisibility(View.INVISIBLE);
  }

  @Override
  public void showRecyclerView()
  {
    recyclerViewComics.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideRecyclerView()
  {
    recyclerViewComics.setVisibility(View.INVISIBLE);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch(item.getItemId())
    {
      case android.R.id.home:
        onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy()
  {
    mPresenter.onDestroy();
    super.onDestroy();
  }
}
