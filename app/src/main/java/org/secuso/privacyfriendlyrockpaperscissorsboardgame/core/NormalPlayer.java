package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by david on 17.06.2016.
 */
public class NormalPlayer implements IPlayer {

    private int id;

    public NormalPlayer(int id){
        this.id=id;
    }

    @Override
    public RPSFigure getNewType() {
        return null;
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
        for(int i =0;i<numFigures;i++){
            int type=rand.nextInt(3);
            switch (type){
                case 0:
                    figures.add(new RPSGameFigure(this.getId(),RPSFigure.ROCK));
                    break;
                case 1:
                    figures.add(new RPSGameFigure(this.getId(),RPSFigure.PAPER));
                    break;
                case 2:
                    figures.add(new RPSGameFigure(this.getId(),RPSFigure.SCISSOR));
                    break;
            }
        }
        return figures;
     }

    @Override
    public void makeMove() {
        return ;
    }
}
