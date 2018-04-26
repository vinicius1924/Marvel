package com.example.vinicius.marvelcharacters.view.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FourThreeLinearLayout extends ForegroundLinearLayout
{

  public FourThreeLinearLayout(Context context, AttributeSet attrs)
  {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec)
  {
    int fourThreeHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthSpec) * 3 / 4,
      View.MeasureSpec.EXACTLY);
    super.onMeasure(widthSpec, fourThreeHeight);
  }

  @Override
  public boolean hasOverlappingRendering()
  {
    return false;
  }
}
