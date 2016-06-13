package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;

/**
 * Created by david on 11.06.2016.
 */
public class RPSFieldView extends ImageView {
    boolean black;

    public RPSFieldView(Context context, AttributeSet attrs,boolean black) {
        super(context, attrs);
        this.black=black;

    }

    public void setImage(RPSFigure fig){
        if(fig!=null)
            this.setImageResource(fig.getImageResourceId());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(this.black)
            this.setBackgroundColor(Color.BLACK);
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) this.getLayoutParams();
        GridLayout parent=(GridLayout) this.getParent();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            params.width = parent.getWidth() / parent.getColumnCount();
            params.height = parent.getWidth() / parent.getRowCount();
        }
        else{
            params.width = parent.getHeight() / parent.getColumnCount();
            params.height = parent.getHeight() / parent.getRowCount();
        }
        this.setLayoutParams(params);
    }
}
