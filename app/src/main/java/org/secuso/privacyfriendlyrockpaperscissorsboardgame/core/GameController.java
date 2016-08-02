package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Toast;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSBoardLayout;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSFieldView;

import java.util.List;

/**
 * Created by david on 06.05.2016.
 */
public class GameController {
    IPlayer p0;
    IPlayer p1;
    IPlayer playerOnTurn;
    int fieldX;
    int fieldY;
    RPSBoardLayout view;
    GameState model;
    boolean cellSelected;
    int selX;
    int selY;
    Context context;
    /**
     * Starts a new Game in Standard 8x8 Layout
     */
    public GameController(Context context){
        this.fieldX=8;
        this.fieldY=8;
        cellSelected=false;
        this.context=context;
    }


    /**
     * Starts a GameController with a saved instance. This can either be a savegame or the advencing into the next level when a game is draw for now.
     * @param fieldX describes how many fields we have in x direction
     * @param fieldY describes how many fields we have in y direction
     */
    public GameController(int fieldX, int fieldY, Context context){
        this.fieldX=fieldX;
        this.fieldY=fieldY;
        this.context=context;
    }

    public int getX(){
        return fieldX;
    }

    public int getY(){
        return fieldY;
    }

    /**
     * Starts a new game. Creates the model, requests the starting assignments from both players and initializes the first turn
     * @param view the Board view to use as view
     */
    public void startGame(RPSBoardLayout view){
        this.p0=new NormalPlayer(0, -1);
        this.p1=new NormalPlayer(1, ContextCompat.getColor(this.context,R.color.colorAccent));
        this.playerOnTurn=this.p1;
        this.view=view;
        this.model=new GameState(fieldX,fieldY,p0,p1);
        this.model.setGamePane(this.placeFigures(p0.provideInitialAssignment(this.getX()*2),p1.provideInitialAssignment(this.getX()*2)));
        this.view.drawFigures(this.getRepresentationForPlayer(),this.playerOnTurn);
    }

    /**
     * Places the figures of both players randomly in the correct spots for the starting assignment
     * @param figuresP0 the figures of p0
     * @param figuresP1 the figures of p1
     * @return the starting gamepane
     */
    public RPSGameFigure[][] placeFigures(List<RPSGameFigure> figuresP0,List<RPSGameFigure> figuresP1){
        RPSGameFigure[][] gamePane=new RPSGameFigure[fieldY][fieldX];
        for(int i =0; i<gamePane.length;i++){
            for(int j=0;j<gamePane[i].length;j++){
                gamePane[i][j]=null;
            }
        }
        for(int i=0;i<16;i++){
            gamePane[i/8][i%8]=figuresP0.get(i);
        }
        for(int i=0;i<16;i++){
            gamePane[6+i/8][i%8]=figuresP1.get(i);
        }
        return gamePane;

    }

    /**
     * Implements the Figure Hiding Game and the bottom up view for the player that is currently on turn.
     * @return
     */
    public RPSGameFigure[][] getRepresentationForPlayer(){
        RPSGameFigure[][] originalGamePane=model.getGamePane();
        RPSGameFigure[][] representationForPlayer= new RPSGameFigure[originalGamePane.length][originalGamePane[0].length];
        if(playerOnTurn.equals(p0)){
            for(int i=0;i<originalGamePane.length;i++){
                for(int j=0;j<originalGamePane[i].length;j++){
                    representationForPlayer[getY()-1-i][getX()-1-j]=originalGamePane[i][j];
                }
            }
        }
        else return originalGamePane;
        return representationForPlayer;
    }

    public void forceRedraw(){
        this.view.drawFigures(this.getRepresentationForPlayer(),playerOnTurn);
    }

    public void playerMove(int xTarget, int yTarget){
        try {
            validateMove(this.selX, this.selY,xTarget,yTarget);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.cellSelected=false;
    }

    public void selectCell(int x, int y){
            Toast.makeText(this.context, "Select Cell "+y+", "+x, Toast.LENGTH_SHORT).show();
            this.cellSelected=true;
            this.selX=x;
            this.selY=y;
    }

    public void deselect(){
        this.cellSelected=false;
        Toast.makeText(this.context, "Deselect Cell "+selY+", "+selX, Toast.LENGTH_SHORT).show();
    }
    public boolean isCellSelected(){
        return this.cellSelected;
    }

    public int getSelX(){
        return this.selX;
    }

    public int getSelY(){
        return this.selY;
    }

    private void validateMove(int xStart, int yStart, int xTarget, int yTarget) throws InterruptedException {
        //Check if movement direction and length is fine
        if(Math.abs(xStart-xTarget)==1) {
            if (Math.abs(yStart - yTarget) == 0) {
                ;
            }
            else{
                Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
                return ;
            }
        }
        else if(Math.abs(xStart-xTarget)==0) {
            if (Math.abs(yStart - yTarget) == 1) {
                ;
            } else {
                Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
                return ;
            }
        }
        else {
            Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
            return ;
        }
        //Check if source field is occupied
        RPSGameFigure[][] gamePane=this.model.getGamePane();
        if(playerOnTurn.getId()==p1.getId()){
            if(gamePane[yStart][xStart]==null) {
                Toast.makeText(this.context, "Invalid Move, Starting Field is empty", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else if(gamePane[getY()-1-yStart][getX()-1-xStart]==null){
            Toast.makeText(this.context, "Invalid Move, Starting Field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check if figure on field is of current Player
        if(playerOnTurn.getId()==p1.getId()){
            if(gamePane[yStart][xStart].getOwner().getId()!=p1.getId()) {
                Toast.makeText(this.context, "Invalid Move, Figure is not owned by Player "+playerOnTurn.getId(), Toast.LENGTH_SHORT).show();
                return ;
            }
        }
        else if (gamePane[getY()-1-yStart][getX()-1-xStart].getOwner().getId()!=p0.getId()){
            Toast.makeText(this.context, "Invalid Move, Figure is not owned by Player "+playerOnTurn.getId(), Toast.LENGTH_SHORT).show();
            return ;
        }
        if(playerOnTurn.getId()==p1.getId())
            //Check if target field is empty
            if(gamePane[yTarget][xTarget]==null) {
                gamePane[yTarget][xTarget]=gamePane[yStart][xStart];
                gamePane[yStart][xStart]=null;
                updateModelAndView(gamePane);
                //wait(2000);
                nextTurn();
                return ;
            }
            else{
                    if(gamePane[yStart][xStart].getOwner().getId()==p1.getId()) {
                        gamePane[yTarget][xTarget]=attack(gamePane[yStart][xStart],gamePane[yTarget][xTarget]);
                        gamePane[yTarget][xTarget].discover();
                        gamePane[yStart][xStart]=null;
                        updateModelAndView(gamePane);
                        //wait(2000);
                        nextTurn();
                        return ;
                    }
                    else{
                        Toast.makeText(this.context, "Invalid Move, attacking own Figures", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                }
        else{
            //Check if target field is empty
            if(gamePane[getY()-1-yTarget][getX()-1-xTarget]==null){
                gamePane[getY()-1-yTarget][getX()-1-xTarget]=gamePane[getY()-1-yStart][getX()-1-xStart];
                gamePane[getY()-1-yStart][getX()-1-xStart]=null;
                updateModelAndView(gamePane);
                //wait(2000);
                nextTurn();
                return ;
            }
            else{
                if(gamePane[getY()-1-yStart][getX()-1-xStart].getOwner().getId()==p0.getId()) {
                    gamePane[getY()-1-yTarget][getX()-1-xTarget]=attack(gamePane[getY()-1-yStart][getX()-1-xStart],gamePane[getY()-1-yTarget][getX()-1-xStart]);
                    gamePane[getY()-1-yTarget][getX()-1-xTarget].discover();
                    gamePane[getY()-1-yStart][getX()-1-xStart]=null;
                    updateModelAndView(gamePane);
                    //wait(2000);
                    nextTurn();
                    return ;
                }
                else{
                    Toast.makeText(this.context, "Invalid Move, attacking own Figures", Toast.LENGTH_SHORT).show();
                    return ;
                }
            }
        }
    }

    void updateModelAndView(RPSGameFigure[][] newPane){
        model.setGamePane(newPane);
        this.view.drawFigures(this.getRepresentationForPlayer(),this.playerOnTurn);
    }

    void nextTurn(){
        if(playerOnTurn.getId()==p1.getId())
            playerOnTurn=p0;
        else playerOnTurn=p1;
        forceRedraw();
    }

    RPSGameFigure attack(RPSGameFigure attacker, RPSGameFigure attacked){
        if(attacked.getType()==attacker.getType())
            return attack(p0.getNewType(),p1.getNewType());
        if(attacked.getType().getsBeatenBy(attacker.getType()))
            return attacker;
        else return attacked;
    }


}
