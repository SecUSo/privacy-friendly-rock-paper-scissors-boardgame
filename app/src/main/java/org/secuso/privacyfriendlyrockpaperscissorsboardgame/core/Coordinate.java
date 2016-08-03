package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * Created by david on 03.08.2016.
 */
public class Coordinate {

    private int x;
    private int y;

    public Coordinate(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
}
