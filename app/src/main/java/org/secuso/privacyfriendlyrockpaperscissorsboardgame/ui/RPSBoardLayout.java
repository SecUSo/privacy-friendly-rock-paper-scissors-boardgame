package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.GridLayout;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.Coordinate;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.IPlayer;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSGameFigure;

import java.util.List;

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
                board[i][j]= new RPSFieldView(getContext(),this.attrs,black,j,i);
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
    public void drawFigures(RPSGameFigure[][] pane,IPlayer onTurn){
        for(int i =0; i<pane.length;i++){
            for(int j=0;j<pane[i].length;j++){
                if(pane[i][j]!=null){
                    if(pane[i][j].getOwner().equals(onTurn)){
                        board[i][j].setImage(pane[i][j].getType(),pane[i][j].getOwner().getColor());
                    }
                    else board[i][j].setImage(pane[i][j].isHidden()?RPSFigure.GHOST:pane[i][j].getType(),pane[i][j].getOwner().getColor());
                }
                else board[i][j].setImage(null,Color.TRANSPARENT);
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

    public void handleTurnover(final IPlayer player) {
        this.clearBoard();
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder((Activity)this.getContext());
        dialogBuilder.setMessage(R.string.sDialogHandOverMessage);
        dialogBuilder.setTitle(R.string.sDialogHandOverTitle);
        dialogBuilder.setNegativeButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawFigures(RPSBoardLayout.this.gameController.getRepresentationForPlayer(),player);
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                handleTurnover(player);
            }
        });
        dialog.show();
    }

    private void clearBoard(){
        for(RPSFieldView[] row: this.board){
            for(RPSFieldView view: row){
                view.setImage(null,-1);
            }
        }
    }

    public void highlightDestinations(List<Coordinate> destinations){
        Drawable highlighted= ResourcesCompat.getDrawable(this.getResources(),R.drawable.higlighted,null).mutate();
        for(Coordinate c:destinations){
            if(this.board[c.getY()][c.getX()].getDrawable()!=null){
                Drawable[] drawables= new Drawable[2];
                drawables[0]=highlighted;
                drawables[1]= this.board[c.getY()][c.getX()].getDrawable();
                LayerDrawable layered= new LayerDrawable(drawables);
                this.board[c.getY()][c.getX()].setImageDrawable(layered);
            }
            else
                this.board[c.getY()][c.getX()].setImageDrawable(highlighted);


        }
    }
}
