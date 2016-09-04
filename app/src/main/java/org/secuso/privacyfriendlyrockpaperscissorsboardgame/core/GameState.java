package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by David Giessing on 07.05.2016.
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
}
