package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import android.graphics.Color;

import java.util.List;

/**
 * Created by David Giessing
 */
public interface IPlayer {
    /**
     * Creates the starting assignment for a player
     *
     * @param numFigures the number of figures that is allowed for the game
     * @return the starting assignment for the player
     */
    List<RPSGameFigure> provideInitialAssignment(int numFigures);

    /**
     * Creates a Move object that has to be handeled by the controller
     *
     * @return a move object
     */
    void makeMove();

    /**
     * Returns a new RPSFigure type. for example when a draw occurs.
     *
     * @return the new RPSFigure type
     */
    RPSFigure getNewType();

    /**
     * Returns the players ID
     *
     * @return the players ID
     */
    int getId();

    boolean equals(IPlayer player);

    int getColor();

    boolean isAi();
}
