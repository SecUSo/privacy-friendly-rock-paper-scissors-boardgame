package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * AI Player for the extended game. Not implemented
 */
public class LizardSpockAIPlayer implements IPlayer {

    private int id;

    private int color;

    public LizardSpockAIPlayer(int id, int color) {
        this.id = id;
        this.color = color;
    }


    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        //TODO:implement
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
        int type = rand.nextInt(5);
        switch (type) {
            case 0:
                return RPSFigure.ROCK;
            case 1:
                return RPSFigure.PAPER;
            case 2:
                return RPSFigure.SCISSOR;
            case 3:
                return RPSFigure.LIZARD;
            default:
                return RPSFigure.SPOCK;
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
