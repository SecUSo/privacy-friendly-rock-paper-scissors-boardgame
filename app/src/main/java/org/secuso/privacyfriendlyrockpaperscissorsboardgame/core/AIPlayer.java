package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Default AI for the normal modes. Not implemented yet
 */
public class AIPlayer implements IPlayer {

    private int id;

    private int color;

    public AIPlayer(int id, int color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        //TODO: implement
        return null;
    }

    @Override
    public void makeMove() {
        //TODO: implement
    }

    @Override
    public RPSFigure getNewType() {
        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        rand.setSeed(cal.getTimeInMillis());
        int type = rand.nextInt(3);
        switch (type) {
            case 0:
                return RPSFigure.ROCK;
            case 1:
                return RPSFigure.PAPER;
            default:
                return RPSFigure.SCISSOR;
        }
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public boolean equals(IPlayer player) {
        return false;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public boolean isAi() {
        return true;
    }
}
