package com.example.vinicius.marvelcharacters.view.main_activity.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener
{

  private DataLoadingStatus dataLoading;
  private Runnable loadMoreRunnable = new Runnable()
  {
    @Override
    public void run()
    {
      onLoadMore();
    }
  };

  public EndlessRecyclerOnScrollListener(DataLoadingStatus dataLoading)
  {
    this.dataLoading = dataLoading;
  }

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy)
  {

    if(dy < 0 || dataLoading.isDataLoading())
      return;

    int visibleItemCount = recyclerView.getChildCount();
    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
    int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    int visibleThreshold = 0;

    if((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
    {
      recyclerView.post(loadMoreRunnable);
    }
  }

  public abstract void onLoadMore();
}