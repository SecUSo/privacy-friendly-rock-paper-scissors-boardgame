package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.app.Fragment;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The Model class of the MVC Pattern used in the game. Is a fragment to allow for saving its state on changes in orientation and others.
 */
public class GameState extends Fragment {
    /**
     * The Game Pane as follows. The y Axis 0,1 are the start of player 0. The Y axis 6 and 7  are player 1s starting poistions.
     */
    private RPSGameFigure[][] gamePane;
    private IPlayer[] players;
    private IPlayer playerOnTurn;
    private boolean resume;
    private boolean againstAI;
    private boolean gameFinished;
    private int gameMode;

    public GameState() {
        super();
        this.resume = false;
        this.gameFinished = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setGamePane(RPSGameFigure[][] gamePane) {
        this.gamePane = gamePane;

    }

    public void setPlayerOnTurn(IPlayer player) {
        this.playerOnTurn = player;
    }

    public IPlayer getPlayerOnTurn() {
        return this.playerOnTurn;
    }

    public void setPlayer(IPlayer p0, IPlayer p1) {
        this.players = new IPlayer[2];
        players[0] = p0;
        players[1] = p1;
    }

    public IPlayer[] getPlayers() {
        return players;
    }

    public RPSGameFigure[][] getGamePane() {
        return gamePane;
    }

    public void setGameStateOK() {
        this.resume = true;
    }

    public boolean gameStateIsReady() {
        return this.resume;
    }

    public void setAgainstAI(boolean againstAI) {
        this.againstAI = againstAI;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getGameMode() {
        return gameMode;
    }

    public boolean isAgainstAI() {
        return againstAI;
    }

    public boolean isGameFinished() {
        return this.gameFinished;
    }

    public void gameIsFinished() {
        this.gameFinished = true;
    }

    /**
     * Writes the model to a file
     * @param f the file
     */
    public void saveToFile(File f) {
        FileOutputStream out= null;
        try {
            out = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            return;
        }
        String result = "";
        result+="mode "+gameMode+"\n";
        result+="P0 "+players[0].toString()+"\n";
        result+="P1 "+players[1].toString()+"\n";
        result+="onTurn "+playerOnTurn.getId()+"\n";
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                result+="x"+i+"y"+j+" ";
                if(gamePane[i][j]==null)
                    result+="NULL\n";
                else result+=gamePane[i][j].toString()+"\n";
            }
        }
        try {
            out.write(result.getBytes());
        } catch (IOException e) {
            return;
        }
    }
}
