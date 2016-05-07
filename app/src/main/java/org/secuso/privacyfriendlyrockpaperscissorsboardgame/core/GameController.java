package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * Created by david on 06.05.2016.
 */
public class GameController {

    int fieldX;
    int fieldY;

    /**
     * Starts a new Game in Standard 8x8 Layout
     */
    public GameController(){
        this.fieldX=8;
        this.fieldY=8;
    }

    /**
     * Starts a GameController with a saved instance. This can either be a savegame or the advencing into the next level when a game is draw for now.
     * @param fieldX
     * @param fieldY
     */
    public GameController(int fieldX, int fieldY){
        this.fieldX=fieldX;
        this.fieldY=fieldY;
    }
}
