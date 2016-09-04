package org.secuso.privacyfriendlyrockpaperscissorsboardgame.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by david on 26.08.2016.
 */
public class SaveGame {
    private IPlayer p0, p1, playerOnTurn;
    private int ID;
    private RPSGameFigure[][] gamePane;
    private int gameMode;
    private boolean ai;
    private File saveGame;
    private int timestamp;

    public SaveGame(IPlayer p0, IPlayer p1, IPlayer playerOnTurn, int ID, RPSGameFigure[][] gamePane, int gameMode, boolean ai, long timestamp) {
        this.p0 = p0;
        this.p1 = p1;
        this.ID = ID;
        this.gamePane = gamePane;
        this.gameMode = gameMode;
        this.ai = ai;

    }

    public SaveGame(File saveGame) {
        this.saveGame = saveGame;
    }

    public void load() {

    }

    public void store() {
        String name = "savegame" + this.timestamp;
        String content = "";
        File f;
        //FileOutputStream fileOutputStream = new FileOutputStream(name,content);

    }

    public IPlayer getP0() {
        return p0;
    }

    public IPlayer getP1() {
        return p1;
    }

    public IPlayer getPlayerOnTurn() {
        return playerOnTurn;
    }

    public int getID() {
        return ID;
    }

    public RPSGameFigure[][] getGamePane() {
        return gamePane;
    }

    public int getGameMode() {
        return gameMode;
    }

    public boolean isAi() {
        return ai;
    }

}
