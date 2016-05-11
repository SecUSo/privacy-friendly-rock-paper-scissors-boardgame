package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * Created by David Giessing on 06.05.2016.
 */
public class GameController {

    private int fieldX;
    private int fieldY;

    /**
     * Starts a new Game in Standard 8x8 Layout
     */
    public GameController(){
        this.setFieldX(8);
        this.setFieldY(8);
    }

    /**
     * Starts a GameController with a saved instance. This can either be a savegame or the advencing into the next level when a game is draw for now.
     * @param fieldX
     * @param fieldY
     */
    public GameController(int fieldX, int fieldY){
        this.setFieldX(fieldX);
        this.setFieldY(fieldY);
    }

    public int getFieldX() {
        return fieldX;
    }

    public void setFieldX(int fieldX) {
        this.fieldX = fieldX;
    }

    public int getFieldY() {
        return fieldY;
    }

    public void setFieldY(int fieldY) {
        this.fieldY = fieldY;
    }
}
