package com.example.vinicius.marvelcharacters.view.character_detail_activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vinicius.marvelcharacters.DTO.ComicDTO;
import com.example.vinicius.marvelcharacters.DTO.ItemsDTO;
import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.example.vinicius.marvelcharacters.view.main_activity.recyclerview.CharactersRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
  implements DataLoadingStatus.DataLoadingCallbacks
{
  private List<ComicDTO> comicsList;
  private Context mContext;
  private final @Nullable DataLoadingStatus dataLoading;
  private boolean showLoadingMore = false;
  private static final int TYPE_COMIC = 0;
  private static final int TYPE_LOADING_MORE = -1;

  public ComicsRecyclerAdapter(Context context, @Nullable DataLoadingStatus dataLoading, List<ComicDTO> comicsList)
  {
    this.comicsList = comicsList;
    this.mContext = context;
    this.dataLoading = dataLoading;

    if(dataLoading != null)
    {
      dataLoading.registerCallback(this);
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    switch (viewType)
    {
      case TYPE_COMIC:
        View viewComic = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_comics_layout, parent, false);
        ComicsRecyclerAdapter.ComicsViewHolder viewHolderComics = new ComicsRecyclerAdapter.ComicsViewHolder(viewComic);
        return viewHolderComics;
      case TYPE_LOADING_MORE:
        View viewInfiniteLoading = LayoutInflater.from(parent.getContext()).inflate(R.layout.infinite_loading, parent, false);
        ComicsRecyclerAdapter.LoadingMoreHolder viewHolderInfiniteLoading = new ComicsRecyclerAdapter.LoadingMoreHolder(viewInfiniteLoading);
        return viewHolderInfiniteLoading;
    }

    return null;
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position)
  {
    switch(getItemViewType(position))
    {
      case TYPE_COMIC:
        bindCharacter((ComicsRecyclerAdapter.ComicsViewHolder)viewHolder, position);
        break;
      case TYPE_LOADING_MORE:
        bindLoadingViewHolder((ComicsRecyclerAdapter.LoadingMoreHolder)viewHolder, position);
        break;
    }
  }

  @Override
  public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder)
  {
    if(holder instanceof ComicsRecyclerAdapter.ComicsViewHolder)
    {
      ComicsRecyclerAdapter.ComicsViewHolder characterViewHolder = (ComicsRecyclerAdapter.ComicsViewHolder) holder;
      characterViewHolder.thumbnailImage.setImageBitmap(null);
    }
  }

  @Override
  public int getItemCount()
  {
    return comicsList.size() + (showLoadingMore ? 1 : 0);
  }

  @Override
  public int getItemViewType(int position)
  {
    //Log.d("RecyclerAdapter", "getItemViewType");
    if(position < getDataItemCount() && getDataItemCount() > 0)
    {
      return TYPE_COMIC;
    }

    return TYPE_LOADING_MORE;
  }

  @Override
  public long getItemId(int position)
  {
    //Log.d("RecyclerAdapter", "getItemId");
    if (getItemViewType(position) == TYPE_LOADING_MORE)
    {
      return -1L;
    }

    if(position < 0 || position >= comicsList.size())
      return -2L;

    return comicsList.get(position).getId();
  }

  @Override
  public void dataStartedLoading()
  {
    if(showLoadingMore)
      return;

    showLoadingMore = true;
    notifyItemInserted(getLoadingMoreItemPosition());
  }

  @Override
  public void dataFinishedLoading()
  {
    if(!showLoadingMore)
      return;

    final int loadingPos = getLoadingMoreItemPosition();
    showLoadingMore = false;
    notifyItemRemoved(loadingPos);
  }

  int getDataItemCount()
  {
    return comicsList.size();
  }

  private int getLoadingMoreItemPosition()
  {
    return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
  }

  private void bindCharacter(final ComicsViewHolder viewHolder, int position)
  {
    final ComicDTO comicDTO = comicsList.get(position);

    viewHolder.comicName.setText(comicDTO.getTitle());

    String imageUrl = comicDTO.getThumbnail().getPath() + "/portrait_fantastic." +
      comicDTO.getThumbnail().getExtension();

    Picasso.get().load(imageUrl)
      //.fit()
      .into(viewHolder.thumbnailImage);

    //ViewCompat.setTransitionName(viewHolder.thumbnailImage, String.valueOf(comicDTO.getId()));
  }

  private void bindLoadingViewHolder(LoadingMoreHolder holder, int position)
  {
    // only show the infinite load progress spinner if there are already items in the
    // grid i.e. it's not the first item & data is being loaded
    holder.progress.setVisibility((position > 0 && dataLoading != null && dataLoading.isDataLoading()) ? View.VISIBLE : View.INVISIBLE);
  }

  class ComicsViewHolder extends RecyclerView.ViewHolder
  {
    private ImageView thumbnailImage;
    private TextView comicName;


    public ComicsViewHolder(View itemView)
    {
      super(itemView);

      this.thumbnailImage = itemView.findViewById(R.id.thumbnail);
      this.comicName = itemView.findViewById(R.id.comic_name);
    }
  }

  class LoadingMoreHolder extends RecyclerView.ViewHolder
  {
    ProgressBar progress;

    LoadingMoreHolder(View itemView)
    {
      super(itemView);
      progress = (ProgressBar) itemView;
    }
  }
}
