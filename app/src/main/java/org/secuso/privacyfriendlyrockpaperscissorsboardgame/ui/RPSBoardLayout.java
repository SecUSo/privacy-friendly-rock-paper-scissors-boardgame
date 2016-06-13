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

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;

/**
 * Created by david on 11.06.2016.
 */
public class RPSBoardLayout extends GridLayout{

    private TextView [][] board;
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
        this.board= new TextView[gameController.getY()][gameController.getX()];
        this.gameController=gameController;
        this.setRowCount(gameController.getX());
        this.setColumnCount(gameController.getY());
        for(int i=0;i<gameController.getY();i++){
            black=!black;
            for(int j=0; j<gameController.getX();j++){
                board[i][j]= new TextView(getContext());
                switch(j){
                    case 0:
                        board[i][j].setText(new String(Character.toChars(0x1F48E)));
                        break;
                    case 1:
                        board[i][j].setText(new String(Character.toChars(0x1F4C4)));
                        break;
                    case 2:
                        board[i][j].setText(new String(Character.toChars(0x2702)));
                        break;
                    case 3:
                        board[i][j].setText(new String(Character.toChars(0x1F596)));
                        break;
                    case 4:
                        board[i][j].setText(new String(Character.toChars(0x1F98E)));
                        break;
                    case 5:
                        board[i][j].setText(new String(Character.toChars(0x1F47B)));
                        break;
                    default:
                        board[i][j].setText(new String(Character.toChars(0x1F601)));

                }
                if(black){
                    board[i][j].setBackgroundColor(Color.BLACK);
                    board[i][j].setTextColor(Color.WHITE);
                    black=!black;
                }
                else{
                    board[i][j].setTextColor(Color.BLACK);
                    board[i][j].setBackgroundColor(Color.WHITE);
                    black=!black;
                }
                addView(board[i][j]);
            }
        }
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            int height = b - t;
            int width = r - l;
            int cellSize = Math.min(this.getWidth(),this.getHeight());
            for (TextView[] viewarray : this.board) {
                for (TextView view : viewarray) {
                    if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT) {
                        view.setWidth(this.getWidth() / this.gameController.getX());
                        view.setHeight(this.getWidth() / this.gameController.getY());
                    }
                    else{
                        view.setWidth(this.getHeight() / this.gameController.getX());
                        view.setHeight(this.getHeight() / this.gameController.getY());
                    }
            }
        }
    }
    }
}
