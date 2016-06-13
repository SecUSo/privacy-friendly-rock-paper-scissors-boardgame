package org.secuso.privacyfriendlyrockpaperscissorsboardgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui.RPSBoardLayout;

public class GameActivity extends AppCompatActivity {
    RPSBoardLayout boardLayout;
    GameController gameController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.gameController=new GameController(8,8,getApplicationContext());
        boardLayout = (RPSBoardLayout) findViewById(R.id.boardLayout);
        boardLayout.createBoard(this.gameController);
    }
}
