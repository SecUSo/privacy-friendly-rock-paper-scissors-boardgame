package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;

/**
 * Created by david on 11.06.2016.
 */
public class RPSBoardLayout extends GridLayout{

    private RPSFieldView [][] board;
    private AttributeSet attrs;
    private GameController gameController;
    private int fieldWidth;
    private int fieldHeight;

    public RPSBoardLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.attrs=attrs;
    }

    public void createBoard(GameController gameController){
        boolean black=true;
        this.board= new RPSFieldView[gameController.getY()][gameController.getX()];
        this.gameController=gameController;
        this.setRowCount(gameController.getX());
        this.setColumnCount(gameController.getY());
        for(int i=0;i<gameController.getY();i++){
            black=!black;
            for(int j=0; j<gameController.getX();j++){
                board[i][j]= new RPSFieldView(getContext(),this.attrs,black);
                black=!black;
                addView(board[i][j]);
            }
        }
        requestLayout();
    }

}
