package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by david on 17.06.2016.
 */
public class NormalPlayer implements IPlayer {

    private int id;

    private int color;

    public NormalPlayer(int id, int color) {
        this.id = id;
        this.color = color;
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

    public int getId() {
        return id;
    }

    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        List<RPSGameFigure> figures = new ArrayList<RPSGameFigure>();
        for (int i = 0; i < 5; i++) {
            figures.add(new RPSGameFigure(this, RPSFigure.ROCK));
            figures.add(new RPSGameFigure(this, RPSFigure.PAPER));
            figures.add(new RPSGameFigure(this, RPSFigure.SCISSOR));
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

    @Override
    public String toString() {
        return this.getId()+" "+this.getColor();
    }
}
