package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.List;

/**
 * Created by david on 22.08.2016.
 */
public class LizardSpockAIPlayer implements IPlayer{

    private int id;

    private int color;

    public LizardSpockAIPlayer(int id, int color){
        this.id=id;
        this.color=color;
    }


    @Override
    public List<RPSGameFigure> provideInitialAssignment(int numFigures) {
        return null;
    }

    @Override
    public void makeMove() {

    }

    @Override
    public RPSGameFigure getNewType() {
        return null;
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
}
