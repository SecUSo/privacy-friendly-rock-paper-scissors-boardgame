package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * Created by David Giessing on 07.05.2016.
 */
public class GameState {
    private RPSFigure[][] gamePane;
    private Player[] players;
    private int playerOnTurn;

    public GameState(int x, int y){
        players= new Player[2];
        players[0]=new Player(0);
        players[1]=new Player(1);
        gamePane= new RPSFigure[x][y];
    }

}
