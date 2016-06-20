package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridLayout;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;

/**
 * Created by david on 11.06.2016.
 */
public class RPSBoardLayout extends GridLayout{
    private RPSFieldView [][] board;
    private AttributeSet attrs;
    private GameController gameController;
    private int xSel;
    private int ySel;
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
                board[i][j]= new RPSFieldView(getContext(),this.attrs,black,i,j);
                black=!black;
                addView(board[i][j]);
            }
        }
        requestLayout();
        gameController.startGame(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RPSFieldView target;
        if(event.getAction()==MotionEvent.ACTION_DOWN){
           target=this.findViewByAbsoluteLocation(event.getRawX(),event.getRawY());
            if(target!=null){
                this.xSel=target.getxIndex();
                this.ySel=target.getyIndex();
            }
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
                target = this.findViewByAbsoluteLocation(event.getRawX(), event.getRawY());
                if (target != null){
                    if(xSel==target.getxIndex()&&ySel==target.getyIndex()){ //This is for a Tap Action
                        //Covers Motions where UP and DOWN had the same target
                        if(this.gameController.isCellSelected())
                            if(this.xSel==this.gameController.getSelX()&&this.ySel==this.gameController.getSelY())
                                this.gameController.deselect(); //Tap a selected field to deselect it
                            else this.gameController.playerMove(target.getxIndex(),target.getyIndex());
                        else this.gameController.selectCell(this.xSel,this.ySel);
                    }
                    else{   //This is for a Swipe Action
                        if(this.gameController.isCellSelected())
                            this.gameController.deselect();         //Ignore previous selected Fields.
                        this.gameController.selectCell(this.xSel,this.ySel);
                        this.gameController.playerMove(target.getxIndex(),target.getyIndex());
                    }
                }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Draws the figures to the Pane
     * @param pane the GamePane to take the figures from, this is already oriented correctly
     */
    public void drawFigures(RPSFigure[][] pane){
        for(int i =0; i<pane.length;i++){
            for(int j=0;j<pane[i].length;j++){
                if(pane[i][j]!=null)
                    board[i][j].setImage(pane[i][j],null);
            }
        }
    }

    /**
     * Returns the RPSFieldView that Contains the Location x,y
     * @param x the x Direction of the Location
     * @param y the y Direction of the Location
     * @return the RPSFieldView containing the location or null if no RPSFieldView contains it
     */
    RPSFieldView findViewByAbsoluteLocation(float x, float y){
        for(int i=0;i<RPSBoardLayout.this.board.length;i++){
            for(int j=0;j<RPSBoardLayout.this.board[i].length;j++){
                Rect r= new Rect();
                Point p= new Point();
                board[i][j].getGlobalVisibleRect(r,p);
                if(r.contains((int)x,(int)y))
                    return board[i][j];
            }
        }
        return null;
    }
}
