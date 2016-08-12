package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by david on 17.06.2016.
 */
public class NormalPlayer implements IPlayer {

    private int id;

    private int color;

    public NormalPlayer(int id, int color){
        this.id=id;
        this.color=color;
    }

    @Override
    public RPSGameFigure getNewType() {
        Random rand= new Random();
        Calendar cal = Calendar.getInstance();
        rand.setSeed(cal.getTimeInMillis());
        int type=rand.nextInt(3);
            switch (type) {
                case 0:
                    return new RPSGameFigure(this, RPSFigure.ROCK);
                case 1:
                    return new RPSGameFigure(this, RPSFigure.PAPER);
                default:
                    return new RPSGameFigure(this, RPSFigure.SCISSOR);
            }
    }

    public int getId() {
        return id;
    }

    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        List<RPSGameFigure> figures = new ArrayList<RPSGameFigure>();
        Random rand= new Random();
        Calendar cal = Calendar.getInstance();
        rand.setSeed(cal.getTimeInMillis());
        for(int i =0;i<15;i++){
            int type=rand.nextInt(3);
            switch (type){
                case 0:
                    figures.add(new RPSGameFigure(this,RPSFigure.ROCK));
                    break;
                case 1:
                    figures.add(new RPSGameFigure(this,RPSFigure.PAPER));
                    break;
                case 2:
                    figures.add(new RPSGameFigure(this,RPSFigure.SCISSOR));
                    break;
            }
        }
        figures.add(rand.nextInt(15),new RPSGameFigure(this,RPSFigure.FLAG));
        return figures;
     }

    @Override
    public void makeMove() {
    }

    public boolean equals(IPlayer player){
        return this.getId()==player.getId();
    }

    public int getColor(){
        return this.color;
    }
}
