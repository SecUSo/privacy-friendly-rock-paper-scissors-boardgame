package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
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

    public int getyIndex() {
        return yIndex;
    }

    public int getxIndex() {
        return xIndex;
    }

    int yIndex;
    public RPSFieldView(Context context, AttributeSet attrs,boolean black,int x, int y) {
        super(context, attrs);
        this.black=black;
        this.xIndex=x;
        this.yIndex=y;
    }

    public void setImage(RPSFigure fig, Color playerColor){
        if(fig!=null) {
            Drawable[] layers = new Drawable[2];
            layers[0]= ResourcesCompat.getDrawable(getResources(),R.drawable.underlying,null);
            layers[1]= ResourcesCompat.getDrawable(getResources(),fig.getImageResourceId(),null);
            LayerDrawable layer = new LayerDrawable(layers);
            this.setImageDrawable(layer);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(this.black)
            this.setBackgroundColor(Color.BLACK);
        else this.setBackgroundColor(Color.WHITE);
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) this.getLayoutParams();
        RPSBoardLayout parent=(RPSBoardLayout) this.getParent();
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            params.width = (int)(parent.getWidth()-2*getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
            params.height = (int)(parent.getWidth()-2*getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();
        }
        else{
            params.width = (int)(parent.getHeight()-2*getResources().getDimension(R.dimen.border_margin))/ parent.getColumnCount();
            params.height = (int)(parent.getHeight()-2*getResources().getDimension(R.dimen.border_margin))/ parent.getRowCount();
        }
        this.setLayoutParams(params);
    }
}
