package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;

/**
 * Created by david on 11.06.2016.
 */
public class RPSFieldView extends ImageView {
    boolean black;
    int xIndex;

    private boolean isHighlighted;

    public int getyIndex() {
        return yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }

    int yIndex;

    public RPSFieldView(Context context, AttributeSet attrs, boolean black, int x, int y) {
        super(context, attrs);
        this.black = black;
        this.xIndex = x;
        this.yIndex = y;
    }

    public void setImage(RPSFigure fig, int playerColor) {
        if (fig != null) {
            Drawable drawable = DrawableCompat.wrap(ResourcesCompat.getDrawable(getResources(), fig.getImageResourceId(), null));
            if (playerColor != -1)
                drawable.setColorFilter(playerColor, PorterDuff.Mode.MULTIPLY);
            else clearColorFilter();
            this.setImageDrawable(drawable);
        } else this.setImageDrawable(null);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.black)
            this.setBackgroundColor(Color.BLACK);
        else this.setBackgroundColor(Color.WHITE);
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) this.getLayoutParams();
        RPSBoardLayout parent = (RPSBoardLayout) this.getParent();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = (int) (parent.getWidth() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
            params.height = (int) (parent.getWidth() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();

        } else {
            params.width = (int) (parent.getHeight() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
            params.height = (int) (parent.getHeight() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();
        }
        //Does not work on Android N
        this.setLayoutParams(params);
    }
}
