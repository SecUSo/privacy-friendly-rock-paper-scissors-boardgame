package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.GameActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.HomeActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.FightDialog;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSBoardLayout;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSFieldView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 06.05.2016.
 */
public class GameController {

    public static final int MODE_NORMAL_AUTO=0;
    public static final int MODE_ROCKPAPERSCISSORSLIZARDSPOCK_AUTO=1;
    public static final int MODE_NORMAL_MANUAL=2;
    public static final int MODE_ROCKPAPERSCISSORSLIZARDSPOCK_MANUAL=3;
    int gameMode;
    boolean ai;
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
    boolean gameFinished;
    /**
     * Starts a new Game in Standard 8x8 Layout
     */
    public GameController(Context context,int gameMode,boolean ai){
        this.fieldX=8;
        this.fieldY=8;
        cellSelected=false;
        this.context=context;
        this.gameFinished=false;
        this.gameMode=gameMode;
        this.ai=ai;
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
        switch(this.gameMode){
            case MODE_NORMAL_AUTO :
                this.p0=this.ai?new AIPlayer(0,-1):new NormalPlayer(0, -1);
                this.p1=new NormalPlayer(1, ContextCompat.getColor(this.context,R.color.colorAccent));
                break;
            case MODE_ROCKPAPERSCISSORSLIZARDSPOCK_AUTO:
                this.p0= this.ai?new LizardSpockAIPlayer(0,-1):new LizardSpockPlayer(0,-1);
                this.p1=new LizardSpockPlayer(1, ContextCompat.getColor(this.context,R.color.colorAccent));
                break;
        }
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
        boolean deselect=true;
        try {
            deselect=validateMove(this.selX, this.selY,xTarget,yTarget);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(deselect){
            this.deselect();
            this.forceRedraw();
        }
    }

    public void selectCell(int x, int y){
            Toast.makeText(this.context, "Select Cell "+y+", "+x, Toast.LENGTH_SHORT).show();
            this.cellSelected=true;
            this.selX=x;
            this.selY=y;
            RPSGameFigure[][] board=this.getRepresentationForPlayer();
            if(board[y][x]!=null) {
                if(!board[y][x].getOwner().equals(this.playerOnTurn)){
                    this.cellSelected=false;
                    Toast.makeText(this.context, "This is not your figure", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(board[y][x].getType()!=RPSFigure.FLAG)
                    this.view.highlightDestinations(this.getValidDestinations(new Coordinate(x, y), playerOnTurn));
                else{
                    this.cellSelected=false;
                    Toast.makeText(this.context, "Flags can not move", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
    }

    public void deselect(){
        forceRedraw();
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

    private boolean validateMove(int xStart, int yStart, int xTarget, int yTarget) throws InterruptedException {
        //Check if movement direction and length is fine
        if(Math.abs(xStart-xTarget)==1) {
            if (Math.abs(yStart - yTarget) == 0) {
                ;
            }
            else{
                //Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
                this.deselect();
                this.forceRedraw();
                this.selectCell(xTarget,yTarget);
                return false;
            }
        }
        else if(Math.abs(xStart-xTarget)==0) {
            if (Math.abs(yStart - yTarget) == 1) {
                ;
            } else {
               // Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
                this.deselect();
                this.forceRedraw();
                this.selectCell(xTarget,yTarget);
                return false;
            }
        }
        else {
            //Toast.makeText(this.context, "Invalid Move", Toast.LENGTH_SHORT).show();
            this.deselect();
            this.forceRedraw();
            this.selectCell(xTarget,yTarget);
            return false;
        }
        //Check if source field is occupied
        RPSGameFigure[][] gamePane=this.model.getGamePane();
        if(playerOnTurn.getId()==p1.getId()){
            if(gamePane[yStart][xStart]==null) {
                //Toast.makeText(this.context, "Invalid Move, Starting Field is empty", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        else if(gamePane[getY()-1-yStart][getX()-1-xStart]==null){
            //Toast.makeText(this.context, "Invalid Move, Starting Field is empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        //Check if figure on field is of current Player
        if(playerOnTurn.getId()==p1.getId()){
            if(gamePane[yStart][xStart].getOwner().getId()!=p1.getId()) {
                //Toast.makeText(this.context, "Invalid Move, Figure is not owned by Player "+playerOnTurn.getId(), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        else if (gamePane[getY()-1-yStart][getX()-1-xStart].getOwner().getId()!=p0.getId()){
            //Toast.makeText(this.context, "Invalid Move, Figure is not owned by Player "+playerOnTurn.getId(), Toast.LENGTH_SHORT).show();
            return true;
        }
        if(playerOnTurn.getId()==p1.getId()) {
            if (gamePane[yStart][xStart].getType() == RPSFigure.FLAG) {
                Toast.makeText(this.context, "Flags can not move", Toast.LENGTH_SHORT).show();
                return true;
            }
            //Check if target field is empty
            if (gamePane[yTarget][xTarget] == null) {
                gamePane[yTarget][xTarget] = gamePane[yStart][xStart];
                gamePane[yStart][xStart] = null;
                updateModelAndView(gamePane);
                //wait(2000);
                nextTurn();
                return true;
            } else {
                if (gamePane[yTarget][xTarget].getOwner().getId() == p0.getId()) {
                    gamePane[yTarget][xTarget] = attack(gamePane[yStart][xStart], gamePane[yTarget][xTarget]);
                    if(!this.gameFinished){
                        gamePane[yTarget][xTarget].discover();
                        gamePane[yStart][xStart] = null;
                        updateModelAndView(gamePane);
                        //nextTurn();
                    }
                    return true;
                } else {
                    this.deselect();
                    this.forceRedraw();
                    this.selectCell(xTarget,yTarget);
                    //Toast.makeText(this.context, "Invalid Move, attacking own Figures", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        else{
            if(gamePane[getY()-1-yStart][getX()-1-xStart].getType()==RPSFigure.FLAG){
                Toast.makeText(this.context, "Flags can not move", Toast.LENGTH_SHORT).show();
                return true;
            }
            //Check if target field is empty
            if(gamePane[getY()-1-yTarget][getX()-1-xTarget]==null){
                gamePane[getY()-1-yTarget][getX()-1-xTarget]=gamePane[getY()-1-yStart][getX()-1-xStart];
                gamePane[getY()-1-yStart][getX()-1-xStart]=null;
                updateModelAndView(gamePane);
                //wait(2000);
                nextTurn();
                return true;
            }
            else{
                if(gamePane[getY()-1-yTarget][getX()-1-xTarget].getOwner().getId()==p1.getId()) {
                    gamePane[getY() - 1 - yTarget][getX() - 1 - xTarget] = attack(gamePane[getY() - 1 - yStart][getX() - 1 - xStart], gamePane[getY() - 1 - yTarget][getX() - 1 - xStart]);
                    if(!this.gameFinished){
                        gamePane[getY() - 1 - yTarget][getX() - 1 - xTarget].discover();
                        gamePane[getY() - 1 - yStart][getX() - 1 - xStart] = null;
                        updateModelAndView(gamePane);
                        //nextTurn();
                    }
                    return true;
                }
                else{
                    this.deselect();
                    this.forceRedraw();
                    this.selectCell(xTarget,yTarget);
                    //Toast.makeText(this.context, "Invalid Move, attacking own Figures", Toast.LENGTH_SHORT).show();
                    return false;
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
        view.handleTurnover(playerOnTurn);
    }

    RPSGameFigure attack(RPSGameFigure attacker, RPSGameFigure attacked){
        if(attacked.getType()==RPSFigure.FLAG){
            handleGameFinished();
            return attacker;
        }
        if(attacked.getType()==attacker.getType())
            return attack(p0.getNewType(),p1.getNewType());
        if(attacked.getType().getsBeatenBy(attacker.getType())){
            view.showFightDialog(attacker, attacked, attacker, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    CountDownTimer timer = new CountDownTimer(2000,1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            nextTurn();
                        }
                    };
                    timer.start();
                }
            });
            return attacker;
        }

        else {
            view.showFightDialog(attacker,attacked,attacked, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    CountDownTimer timer = new CountDownTimer(2000,1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            nextTurn();
                        }
                    };
                    timer.start();
                }
            });
            return attacked;
        }
    }

    private void handleGameFinished() {
        this.gameFinished=true;
        for(RPSGameFigure[] row:this.getRepresentationForPlayer()){
            for(RPSGameFigure fig:row){
                if(fig!=null)
                    fig.discover();
            }
        }
        this.forceRedraw();
        view.showWinDialog();
    }

    private List<Coordinate> getValidDestinations(Coordinate origin, IPlayer player){
        ArrayList<Coordinate> result = new ArrayList<>();
        ArrayList<Coordinate> temp = new ArrayList<>();
        RPSGameFigure[][] fieldFromPlayer=this.getRepresentationForPlayer();
        Coordinate c1=new Coordinate(origin.getX()-1,origin.getY());
        Coordinate c2=new Coordinate(origin.getX()+1,origin.getY());
        Coordinate c3=new Coordinate(origin.getX(),origin.getY()-1);
        Coordinate c4=new Coordinate(origin.getX(),origin.getY()+1);
        temp.add(c1);
        temp.add(c2);
        temp.add(c3);
        temp.add(c4);
        for(Coordinate c:temp){
            if(isValid(c,fieldFromPlayer,player))
                result.add(c);
        }
        return result;
    }

    private boolean isValid(Coordinate c,RPSGameFigure[][] field, IPlayer player){
        if(c.getX()>=this.getX()||c.getY()>=this.getY()||c.getX()<0||c.getY()<0)
            return false;
        if(field[c.getY()][c.getX()]==null)
            return true;
        else if(field[c.getY()][c.getX()].getOwner().equals(player))
            return false;
        else return true;
    }

    public boolean isGameFinished(){
        return this.gameFinished;
    }
}
