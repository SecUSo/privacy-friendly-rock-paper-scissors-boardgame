package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Player for Lizard Spock modes
 */
public class LizardSpockPlayer implements IPlayer {

    private int id;

    private int color;

    public LizardSpockPlayer(int id, int color) {
        this.id = id;
        this.color = color;
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

    public int getId() {
        return id;
    }

    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        List<RPSGameFigure> figures = new ArrayList<RPSGameFigure>();
        for (int i = 0; i < 3; i++) {
            figures.add(new RPSGameFigure(this, RPSFigure.ROCK));
            figures.add(new RPSGameFigure(this, RPSFigure.PAPER));
            figures.add(new RPSGameFigure(this, RPSFigure.SCISSOR));
            figures.add(new RPSGameFigure(this, RPSFigure.LIZARD));
            figures.add(new RPSGameFigure(this, RPSFigure.SPOCK));
        }
        figures.add(new RPSGameFigure(this, RPSFigure.FLAG));
        Calendar cal = Calendar.getInstance();
        Random rand= new Random();
        rand.setSeed(cal.getTimeInMillis());
        Collections.shuffle(figures,rand);
        return figures;
    }

    @Override
    public void makeMove() {
    }

    public boolean equals(IPlayer player) {
        return this.getId() == player.getId();
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public boolean isAi() {
        return false;
    }
}
