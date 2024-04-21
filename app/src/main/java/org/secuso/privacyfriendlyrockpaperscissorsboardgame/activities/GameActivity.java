package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameState;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.IPlayer;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.LizardSpockPlayer;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.NormalPlayer;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSGameFigure;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSBoardLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class GameActivity extends AppCompatActivity {
    RPSBoardLayout boardLayout;
    GameController gameController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        android.app.FragmentManager fm = getFragmentManager();
        String filename=intent.getStringExtra("file");
        GameState model=null;
        if(filename==null)
            model = (GameState) fm.findFragmentByTag("gameState");
        else{
            model=gameStateFromFile(new File(filename));
            if(model != null) fm.beginTransaction().add(model, "gameState").commit();
            else model = (GameState) fm.findFragmentByTag("gameState");
        }
        if (model == null) {
            model = new GameState();
            fm.beginTransaction().add(model, "gameState").commit();
            int gameMode = intent.getIntExtra(HomeActivity.Companion.getGAMEMODE_EXTRA(), 0);
            boolean ai = intent.getBooleanExtra(HomeActivity.Companion.getAI_EXTRA(), false);
            model.setAgainstAI(ai);
            model.setGameMode(gameMode);
        }
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.gameController = new GameController((Activity)this, model);
        boardLayout = (RPSBoardLayout) findViewById(R.id.boardLayout);
        boardLayout.createBoard(this.gameController);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.app.FragmentManager fm =getFragmentManager();
        GameState model= (GameState)fm.findFragmentByTag("gameState");
        if(model.isGameFinished())
            return;
        String name="";
        Calendar c=Calendar.getInstance();
        name+=c.get(Calendar.DAY_OF_MONTH)+"_"+c.get(Calendar.MONTH)+"_"+c.get(Calendar.YEAR)+"_"+c.get(Calendar.HOUR_OF_DAY)+"_"+(c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE):c.get(Calendar.MINUTE))+"_"+model.getGameMode()+".save";
        File dir = new File(getFilesDir(),name);
        if(model.gameStateIsReady())
            model.saveToFile(dir);
    }

    GameState gameStateFromFile(File f){
        try {
            GameState gameState = new GameState();
            FileInputStream in = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();

            gameState.setGameMode(Integer.parseInt(line.split(" ")[1]));
            IPlayer[] players = new IPlayer[2];
            for (int j = 0; j < 2; j++) {
                line = reader.readLine();

                String[] player = line.split(" ");
                players[j] = (gameState.getGameMode() == GameController.MODE_NORMAL_AUTO || gameState.getGameMode() == GameController.MODE_NORMAL_MANUAL) ?
                        new NormalPlayer(Integer.parseInt(player[1]), Integer.parseInt(player[2])) :
                        new LizardSpockPlayer(Integer.parseInt(player[1]), Integer.parseInt(player[2]));
            }

            gameState.setPlayer(players[0], players[1]);
            line = reader.readLine();

            gameState.setPlayerOnTurn(players[Integer.parseInt(line.split(" ")[1])]);
            RPSGameFigure[][] gamePane = new RPSGameFigure[8][8];
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    line = reader.readLine();

                    if (line.contains("NULL"))
                        gamePane[j][k] = null;
                    else {
                        String[] values = line.split(" ");
                        IPlayer owner = players[Integer.parseInt(values[1])];
                        RPSFigure type;
                        switch (Integer.parseInt(values[2])) {
                            case 0:
                                type = RPSFigure.ROCK;
                                break;
                            case 1:
                                type = RPSFigure.PAPER;
                                break;
                            case 2:
                                type = RPSFigure.SCISSOR;
                                break;
                            case 3:
                                type = RPSFigure.SPOCK;
                                break;
                            case 4:
                                type = RPSFigure.LIZARD;
                                break;
                            default:
                                type = RPSFigure.FLAG;
                                break;
                        }
                        boolean hidden = values[3].contains("hidden");
                        gamePane[j][k] = new RPSGameFigure(owner, type);
                        if (!hidden)
                            gamePane[j][k].discover();
                    }
                }
            }
            gameState.setGamePane(gamePane);
            gameState.setGameStateOK();
            f.delete();
            return gameState;
        } catch (Exception e) {
            if(f.exists()) f.delete();
            return null;
        }
    }
}
