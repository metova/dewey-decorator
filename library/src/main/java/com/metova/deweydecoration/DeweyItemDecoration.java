package com.metova.deweydecoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DeweyItemDecoration extends RecyclerView.ItemDecoration {

    private Resources mRes;
    private Drawable mDrawable;

    private TextPaint mTextPaint;
    private float mTextOffsetY;
    private float mTextOffsetX;

    private List<String> mDisplayedLabels = new ArrayList<>();

    /**
     * <p>Creates a new {@code DeweyItemDecoration} with the provided Drawable. A default {@code TextPaint} will be created.</p>
     *
     * <p>See {@link #DeweyItemDecoration(Context, Drawable, TextPaint)} for further documentation.</p>
     *
     * @param context    app's context
     * @param background drawable used when the decoration draws
     */
    public DeweyItemDecoration(Context context, Drawable background) {
        this(context, background, null);
    }

    /**
     * <p>Creates a new {@code DeweyItemDecoration} with the provided Drawable and TextPaint.</p>
     *
     * <p>A decoration that will display a label for a unique group, as well as a sticky label for the currently
     * displayed group. A group is defined as a collection of items with a matching label. The decoration will be
     * displayed on the first item within a group.</p>
     *
     * <p>To use, the {@code RecyclerView.Adapter} must implement {@link DeweyProvider}.</p>
     *
     * <p>The items in the Adapter must be sorted for this decoration to display correctly.</p>
     *
     * <p>If a {@code TextPaint} is not supplied, a default one is created with the following properties:
     * <ul>
     * <li>Text Size: 18dp</li>
     * <li>Color: Black</li>
     * <li>Typeface: Default typeface, bold</li>
     * <li>Text Align: Center</li>
     * </ul>
     * To make changes to the default {@code TextPaint}, pass {@code null} for the {@code TextPaint} parameter
     * and use {@link #getTextPaint()}.</p>
     *
     * @param context    app's context
     * @param background drawable used when the decoration draws
     * @param textPaint  paint used when displaying the label
     */
    public DeweyItemDecoration(Context context, Drawable background, TextPaint textPaint) {
        mRes = context.getResources();
        mDrawable = background;

        mTextPaint = textPaint;
        if (mTextPaint == null) {
            mTextPaint = createDefaultTextPaint();
        }

        // Hack to get the baseline height of the text
        Rect bounds = new Rect();
        mTextPaint.getTextBounds("A", 0, 1, bounds);

        mTextOffsetY = bounds.height() / 2;
        mTextOffsetX = 0;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getAdapter() instanceof DeweyProvider)) {
            throw new IllegalStateException("Adapter must implement DeweyProvider");
        }

        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }

        mDisplayedLabels.clear();

        int left = parent.getWidth() - mDrawable.getIntrinsicWidth();
        int right = parent.getWidth();

        View child = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(child);

        DeweyProvider deweyProvider = (DeweyProvider) parent.getAdapter();
        String labelText = deweyProvider.getDeweyLabelForPosition(position);
        mDisplayedLabels.add(labelText);

        int nextTop = Integer.MAX_VALUE;
        boolean nextTopSet = false;
        for (int i = 1; i < childCount; i++) {
            child = parent.getChildAt(i);
            position = parent.getChildAdapterPosition(child);

            deweyProvider = (DeweyProvider) parent.getAdapter();
            String text = deweyProvider.getDeweyLabelForPosition(position);
            if (!mDisplayedLabels.contains(text)) {
                mDisplayedLabels.add(text);
                int top = child.getTop();
                if (top > 0) {
                    int bottom = top + mDrawable.getIntrinsicHeight();
                    mDrawable.setBounds(left, top, right, bottom);
                    mDrawable.draw(c);

                    c.drawText(text, (left + right) / 2 + mTextOffsetX, (top + bottom) / 2 + mTextOffsetY, mTextPaint);

                    if (!nextTopSet) {
                        nextTop = top;
                        nextTopSet = true;
                    }
                }
            }
        }

        int top = Math.min(0, nextTop - mDrawable.getIntrinsicHeight());
        int bottom = top + mDrawable.getIntrinsicHeight();

        mDrawable.setBounds(left, top, right, bottom);
        mDrawable.draw(c);

        c.drawText(labelText, (left + right) / 2 + mTextOffsetX, (top + bottom) / 2 + mTextOffsetY, mTextPaint);
    }

    private TextPaint createDefaultTextPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(mRes.getColor(android.R.color.black));
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18f, mRes.getDisplayMetrics());
        textPaint.setTextSize(size);

        return textPaint;
    }

    /**
     * <p>Returns the TextPaint used when a label is displayed. If one was not supplied during object creation, the default
     * {@code TextPaint} is returned.</p>
     *
     * <p>Values can be changed on this object at any time. The caller must call {@code invalidate()} on the RecyclerView if
     * items are already displayed in order for changes to be seen immediately.</p>
     *
     * @return paint used for drawing the label
     */
    public TextPaint getTextPaint() {
        return mTextPaint;
    }

    /**
     * <p>Sets the TextPaint used when a label is displayed.</p>
     *
     * <p>This value can set be set at any time. The caller must call {@code invalidate()} on the RecyclerView if
     * items are already displayed in order for changes to be seen immediately.</p>
     *
     * @param mTextPaint paint used for drawing the label
     */
    public void setTextPaint(@NonNull TextPaint mTextPaint) {
        this.mTextPaint = mTextPaint;
    }

    /**
     * <p>Returns the Y-offset of the label.</p>
     *
     * <p>Default is half the baseline height of the text based on the text size.</p>
     *
     * @return Y-offset of the label
     */
    public float getTextOffsetY() {
        return mTextOffsetY;
    }

    /**
     * <p>Sets the Y-offset of the label.</p>
     *
     * <p>Default is half the baseline height of the text based on the text size.</p>
     *
     * @param mTextOffsetY Y-offset of the label
     */
    public void setTextOffsetY(float mTextOffsetY) {
        this.mTextOffsetY = mTextOffsetY;
    }

    /**
     * <p>Returns the X-offset of the label.</p>
     *
     * <p>Default is 0 since the default {@code TextPaint} has a text align value of {@code CENTER}.</p>
     *
     * @return X-offset of the label
     */
    public float getTextOffsetX() {
        return mTextOffsetX;
    }

    /**
     * <p>Sets the X-offset of the label.</p>
     *
     * <p>Default is 0 since the default {@code TextPaint} has a text align value of {@code CENTER}.</p>
     *
     * @param mTextOffsetX X-offset of the label
     */
    public void setTextOffsetX(float mTextOffsetX) {
        this.mTextOffsetX = mTextOffsetX;
    }
}
