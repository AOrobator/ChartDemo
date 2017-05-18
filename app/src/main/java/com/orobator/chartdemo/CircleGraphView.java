package com.orobator.chartdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class CircleGraphView extends View {
  private float fraction = .5f;

  public CircleGraphView(Context context) {
    super(context);
    init(null, 0);
  }

  public CircleGraphView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs, 0);
  }

  public CircleGraphView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs, defStyle);
  }

  private void init(AttributeSet attrs, int defStyle) {
    // Load attributes
    final TypedArray a =
        getContext().obtainStyledAttributes(attrs, R.styleable.CircleGraphView, defStyle, 0);

    a.recycle();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    CircleGraphStyleKit.drawCanvas1(canvas, new RectF(0, 0, getWidth(), getHeight()),
        CircleGraphStyleKit.ResizingBehavior.AspectFit, fraction);
  }

  public void setFraction(float newFraction) {
    if (newFraction < 0f) {
      newFraction = 0f;
    } else if (newFraction > 1f) {
      newFraction = 1f;
    }

    fraction = newFraction;

    invalidate();
  }
}
