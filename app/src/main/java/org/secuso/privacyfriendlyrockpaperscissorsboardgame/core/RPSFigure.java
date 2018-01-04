package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;

/**
 * The RPSFigure Class represents the Entities participating on the board. Each RPSFigure has weaknesses of Type Figures against which it will loose a fight.
 */
public enum RPSFigure {

    ROCK(R.drawable.rock),
    PAPER(R.drawable.paper),
    SCISSOR(R.drawable.scissor),
    LIZARD(R.drawable.lizard),
    SPOCK(R.drawable.spock),
    FLAG(R.drawable.flag),
    GHOST(R.drawable.ghost);

    int imageID;

    RPSFigure(int imageID) {
        this.imageID = imageID;
    }

    public int getImageResourceId() {
        return imageID;
    }

    public boolean getsBeatenBy(RPSFigure enemy) {
        switch (this) {
            case ROCK:
                return enemy == PAPER || enemy == SPOCK;
            case PAPER:
                return enemy == SCISSOR || enemy == LIZARD;
            case SCISSOR:
                return enemy == ROCK || enemy == SPOCK;
            case SPOCK:
                return enemy == PAPER || enemy == LIZARD;
            case LIZARD:
                return enemy == SCISSOR || enemy == ROCK;
            case FLAG:
                return true;
            default:
                return false;
        }
    }

    public String getName() {
        switch (this) {
            case ROCK:
                return "Rock";
            case PAPER:
                return "Paper";
            case SCISSOR:
                return "Scissor";
            case SPOCK:
                return "Spock";
            case LIZARD:
                return "Lizard";
            case FLAG:
                return "Flag";
            default:
                return "Unknown Type";
        }
    }
    public String toString(){
        switch(this){
            case ROCK:
                return "0";
            case PAPER:
                return "1";
            case SCISSOR:
                return "2";
            case SPOCK:
                return "3";
            case LIZARD:
                return "4";
            default:
                return "5";
        }
    }


}