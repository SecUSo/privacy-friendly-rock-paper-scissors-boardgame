package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameState;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSBoardLayout;

public class GameActivity extends AppCompatActivity {
    RPSBoardLayout boardLayout;
    GameController gameController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.app.FragmentManager fm = getFragmentManager();
        GameState model = (GameState) fm.findFragmentByTag("gameState");
        if(model==null){
            model=new GameState();
            fm.beginTransaction().add(model,"gameState").commit();
            Intent intent= getIntent();
            int gameMode= intent.getIntExtra(HomeActivity.GAMEMODE_EXTRA,0);
            boolean ai = intent.getBooleanExtra(HomeActivity.AI_EXTRA,false);
            model.setAgainstAI(ai);
            model.setGameMode(gameMode);
        }
            setContentView(R.layout.activity_game);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            this.gameController=new GameController(this.getApplicationContext(),model);
            boardLayout = (RPSBoardLayout) findViewById(R.id.boardLayout);
            boardLayout.createBoard(this.gameController);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
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
}
