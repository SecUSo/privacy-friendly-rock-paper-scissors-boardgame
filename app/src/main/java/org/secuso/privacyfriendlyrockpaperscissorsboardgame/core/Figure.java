package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.util.List;

/**
 * Created by David Giessing on 06.05.2016.
 * The Figure Class represents the Entities participating on the board. Each Figure has weaknesses of Type Figures against which it will loose a fight.
 */
public abstract class Figure {

    protected int posX;

    protected int posY;

    protected final List<Class> weaknesses;

    protected final Player owner;

    protected Figure(List<Class> weaknesses, Player owner){
        this.weaknesses=weaknesses;
    }

    protected abstract boolean moveForward();

    protected abstract boolean moveForwardFast();

    protected abstract boolean moveForwardLeft();

    protected abstract boolean moveForwardRight();
}
