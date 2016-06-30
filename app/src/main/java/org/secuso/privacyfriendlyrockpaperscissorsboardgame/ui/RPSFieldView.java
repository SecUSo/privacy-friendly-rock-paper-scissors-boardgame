package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
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
        else this.setImageDrawable(null);
    }

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(this.black)
            this.setBackgroundColor(Color.BLACK);
        else this.setBackgroundColor(Color.WHITE);
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) this.getLayoutParams();
        RPSBoardLayout parent=(RPSBoardLayout) this.getParent();
        Rect display= new Rect();
        parent.getDisplay().getRectSize(display);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
            //params.width = (int)(parent.getWidth()-2*getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
            //params.height = (int)(parent.getWidth()-2*getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();
            //This seems to work partly for Android N API 24
                params.width = (int)((display.right-display.left)-2*getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
                params.height = (int)((display.right-display.left)-2*getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();
        }
        else {
            params.width = (int) (parent.getHeight() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getColumnCount();
            params.height = (int) (parent.getHeight() - 2 * getResources().getDimension(R.dimen.border_margin)) / parent.getRowCount();
        }
        //Does not work on Android N
        //this.setLayoutParams(params);
        int topPar=parent.getTop();
        int bottomPar=parent.getBottom();
        int leftPar=parent.getLeft();
        int rightPar=parent.getRight();
        int width= (int) (parent.getRight()-parent.getLeft()-2*getResources().getDimension(R.dimen.border_margin));
        int cellWidth= (int) (width/parent.getColumnCount());
        int offset= (int) (bottomPar-topPar-(rightPar-leftPar-2*getResources().getDimension(R.dimen.border_margin)))/2;
        int cleft= (int) (parent.getLeft()+cellWidth*this.getxIndex());
        int cright=(int) (parent.getLeft()+getResources().getDimension(R.dimen.border_margin)+cellWidth*(this.getxIndex()+1));
        int ctop=  offset+cellWidth*this.getyIndex();
        int cbottom= offset+cellWidth*(this.getyIndex()+1);
        this.setLeft(cleft);
        this.setRight(cright);
        this.setTop(ctop);
        this.setBottom(cbottom);
    }*/
}
