package com.metova.deweydecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeweyItemDecoration extends RecyclerView.ItemDecoration {

    private final LayoutInflater mInflater;
    private final int mLayoutResId;

    private final List<String> mDisplayedLabels = new ArrayList<>();

    // TODO: Implement view recycling

    private View mView;
    private TextView mTextView;

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
     * @param context app's context
     */
    public DeweyItemDecoration(Context context, int layoutResId) {
        mInflater = LayoutInflater.from(context);
        mLayoutResId = layoutResId;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getAdapter() instanceof DeweyProvider)) {
            throw new IllegalStateException("Adapter must implement DeweyProvider");
        }

        if (mView == null) {
            mView = createView(parent);
            mTextView = (TextView) mView.findViewById(R.id.dd_text);
        }

        int childCount = parent.getChildCount();
        if (childCount == 0) {
            return;
        }

        mDisplayedLabels.clear();

        View child = parent.getChildAt(0);
        int position = parent.getChildAdapterPosition(child);

        DeweyProvider deweyProvider = (DeweyProvider) parent.getAdapter();
        String labelText = deweyProvider.getDeweyLabelForPosition(position);
        if (!mTextView.getText().equals(labelText)) {
            mTextView.setText(labelText);
        }

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
                    View v = createView(parent);
                    TextView textView = (TextView) v.findViewById(R.id.dd_text);
                    textView.setText(text);

                    int left = parent.getWidth() - v.getMeasuredWidth();
                    drawView(c, v, left, top);

                    if (!nextTopSet) {
                        nextTop = top;
                        nextTopSet = true;
                    }
                }
            }
        }

        int left = parent.getWidth() - mView.getMeasuredWidth();
        int top = Math.min(0, nextTop - mView.getMeasuredHeight());
        drawView(c, mView, left, top);
    }

    @SuppressWarnings("ResourceType")
    private View createView(RecyclerView parent) {
        FrameLayout container = (FrameLayout) mInflater.inflate(R.layout.dd_container, parent, false);
        mInflater.inflate(mLayoutResId, container);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) container.getLayoutParams();

        int widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.UNSPECIFIED);
        int heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.UNSPECIFIED);
        container.measure(widthSpec, heightSpec);

        container.layout(0, 0, container.getMeasuredWidth(), container.getMeasuredHeight());

        return container;
    }

    private void drawView(Canvas c, View v, int left, int top) {
        c.save();
        c.translate(left, top);
        v.draw(c);
        c.restore();
    }
}
