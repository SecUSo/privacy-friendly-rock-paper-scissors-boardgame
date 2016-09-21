package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

/**
 * class containing a move. It contains the starting location as x and y coordinates as well as the target location. It also contains the information whether a fight was won if there was one
 */
public class Move {
    private int xStart;
    private int yStart;
    private int xTarget;
    private int yTarget;
    private boolean won;

    public Move(int xStart, int yStart, int xTarget, int yTarget) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.won = false;
    }

    void setWon() {
        this.won = true;
    }

    public boolean isWon() {
        return this.won;
    }

    public int getyStart() {
        return yStart;
    }

    public int getxStart() {
        return xStart;
    }

    public int getxTarget() {
        return xTarget;
    }

    public int getyTarget() {
        return yTarget;
    }

}
