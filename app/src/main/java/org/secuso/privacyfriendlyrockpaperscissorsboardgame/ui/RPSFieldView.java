package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;

/**
 * View representing a cell.
 */
public class RPSFieldView extends AppCompatImageView {
    boolean black;
    int xIndex;
    int yIndex;
    public int getyIndex() {
        return yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }

    public RPSFieldView(Context context, AttributeSet attrs, boolean black, int x, int y) {
        super(context, attrs);
        this.black = black;
        if(black)
            this.setBackgroundColor(Color.BLACK);
        else this.setBackgroundColor(Color.WHITE);
        this.xIndex = x;
        this.yIndex = y;
    }

    /**
     * Sets the image of a view as well as tinting it in the players color.
     * @param fig
     * @param playerColor
     */
    public void setImage(RPSFigure fig, int playerColor) {
        if (fig != null) {
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), fig.getImageResourceId(), null);
            if(drawable!=null)
                drawable.mutate();
            if (playerColor != -1)
                drawable.setColorFilter(playerColor, PorterDuff.Mode.MULTIPLY);
            else clearColorFilter();
            this.setImageDrawable(drawable);
        } else this.setImageDrawable(null);
    }
}
