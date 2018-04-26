package com.example.vinicius.marvelcharacters.view.main_activity.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vinicius.marvelcharacters.DTO.CharacterDTO;
import com.example.vinicius.marvelcharacters.R;
import com.example.vinicius.marvelcharacters.model.data_manager.DataLoadingStatus;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CharactersRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
  implements DataLoadingStatus.DataLoadingCallbacks
{
  private List<CharacterDTO> charactersList;
  private Context mContext;
  private final ListItemClickListener mOnClickListener;
  private final @Nullable DataLoadingStatus dataLoading;

  private static final int TYPE_CHARACTER = 0;
  private static final int TYPE_LOADING_MORE = -1;
  private boolean showLoadingMore = false;
  private int numberOfColumns;

  public CharactersRecyclerAdapter(Context context, @Nullable DataLoadingStatus dataLoading, List<CharacterDTO> charactersList,
                                   ListItemClickListener mOnClickListener, int numberOfColumns)
  {
    this.charactersList = charactersList;
    this.mContext = context;
    this.mOnClickListener = mOnClickListener;
    this.dataLoading = dataLoading;
    this.numberOfColumns = numberOfColumns;

    if(dataLoading != null)
    {
      dataLoading.registerCallback(this);
    }
  }

  public interface ListItemClickListener
  {
    void onListItemClick(int clickedItemIndex, ImageView thumbnailImage);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    switch (viewType)
    {
      case TYPE_CHARACTER:
        View viewCharacter = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_characters_layout, parent, false);
        CharacterViewHolder viewHolderCharacter = new CharacterViewHolder(viewCharacter);
        return viewHolderCharacter;
      case TYPE_LOADING_MORE:
        View viewInfiniteLoading = LayoutInflater.from(parent.getContext()).inflate(R.layout.infinite_loading, parent, false);
        LoadingMoreHolder viewHolderInfiniteLoading = new LoadingMoreHolder(viewInfiniteLoading);
        return viewHolderInfiniteLoading;
    }

    return null;
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position)
  {
    switch(getItemViewType(position))
    {
      case TYPE_CHARACTER:
        bindCharacter((CharacterViewHolder)viewHolder, position);
        break;
      case TYPE_LOADING_MORE:
        bindLoadingViewHolder((LoadingMoreHolder)viewHolder, position);
        break;
    }
  }

  @Override
  public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder)
  {
    if(holder instanceof CharacterViewHolder)
    {
      CharacterViewHolder characterViewHolder = (CharacterViewHolder) holder;
      characterViewHolder.thumbnailImage.setImageBitmap(null);
    }
  }

  @Override
  public int getItemCount()
  {
    return charactersList.size() + (showLoadingMore ? 1 : 0);
  }

  @Override
  public int getItemViewType(int position)
  {
    if(position < getDataItemCount() && getDataItemCount() > 0)
    {
      return TYPE_CHARACTER;
    }

    return TYPE_LOADING_MORE;
  }

  @Override
  public long getItemId(int position)
  {
    if (getItemViewType(position) == TYPE_LOADING_MORE)
    {
      return -1L;
    }

    if(position < 0 || position >= charactersList.size())
      return -2L;

    return charactersList.get(position).getId();
  }

  private void bindCharacter(final CharacterViewHolder viewHolder, int position)
  {
    final CharacterDTO characterDTO = charactersList.get(position);

    viewHolder.characterName.setText(characterDTO.getName());

    String imageUrl = characterDTO.getThumbnail().getPath() + "/portrait_uncanny." +
      characterDTO.getThumbnail().getExtension();

    Picasso.get().load(imageUrl)
      .into(viewHolder.thumbnailImage);

    ViewCompat.setTransitionName(viewHolder.thumbnailImage, String.valueOf(characterDTO.getId()));
  }

  private void bindLoadingViewHolder(LoadingMoreHolder holder, int position)
  {
    holder.progress.setVisibility((position > 0 && dataLoading != null && dataLoading.isDataLoading()) ? View.VISIBLE : View.INVISIBLE);
  }



  private int getLoadingMoreItemPosition()
  {
    return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
  }

  int getDataItemCount()
  {
    return charactersList.size();
  }

  class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
  {
    private ImageView thumbnailImage;
    private TextView characterName;
    private LinearLayout contentBackground;

    public CharacterViewHolder(View itemView)
    {
      super(itemView);

      itemView.setOnClickListener(this);

      this.contentBackground = itemView.findViewById(R.id.contentBackground);
      this.thumbnailImage = itemView.findViewById(R.id.thumbnail);
      this.characterName = itemView.findViewById(R.id.character_name);
    }

    @Override
    public void onClick(View view)
    {
      int position = getAdapterPosition();

      mOnClickListener.onListItemClick(position, thumbnailImage);
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

  @Override
  public void dataStartedLoading()
  {
    Log.d("RecyclerAdapter", "dataStartedLoading");

    if(showLoadingMore)
      return;

    showLoadingMore = true;
    notifyItemInserted(getLoadingMoreItemPosition());
  }

  @Override
  public void dataFinishedLoading()
  {
    Log.d("RecyclerAdapter", "dataFinishedLoading");

    if(!showLoadingMore)
      return;

    final int loadingPos = getLoadingMoreItemPosition();
    showLoadingMore = false;
    notifyItemRemoved(loadingPos);
  }

  public int getItemColumnSpan(int position)
  {
    switch (getItemViewType(position))
    {
      case TYPE_LOADING_MORE:
        return numberOfColumns;
      default:
        return 1;
    }
  }
}
