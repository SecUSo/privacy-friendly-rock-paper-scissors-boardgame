package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.SettingsActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSGameFigure;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by david on 10.08.2016.
 */
public class FightDialog extends Dialog {

    RPSGameFigure attacker;
    RPSGameFigure attacked;
    RPSGameFigure winner;

    public FightDialog(Context context, RPSGameFigure attacker, RPSGameFigure attacked, RPSGameFigure winner, OnDismissListener listener) {
        super(context);
        this.setCancelable(true);
        this.setContentView(R.layout.fight_dialog_layout);
        this.attacked = attacked;
        this.attacker = attacker;
        this.winner = winner;
        this.setOnDismissListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.sFightDialogTitle);
        ImageView attackerImageView = (ImageView) this.findViewById(R.id.imageView1);
        ImageView attackedImageView = (ImageView) this.findViewById(R.id.imageView2);
        ImageView winnerImageView = (ImageView) this.findViewById(R.id.imageView3);
        //WindowManager.LayoutParams params = this.getWindow().getAttributes();
        attackedImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), attacked.getType().getImageResourceId(), null));
        attackerImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), attacker.getType().getImageResourceId(), null));
        winnerImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), winner.getType().getImageResourceId(), null));

        SharedPreferences shared = getContext().getSharedPreferences(SettingsActivity.KEY_TIMER_SWITCH,Context.MODE_PRIVATE );
        boolean b= shared.getBoolean(SettingsActivity.KEY_TIMER_SWITCH,false);
        Toast.makeText(getContext(),""+b, Toast.LENGTH_SHORT);
        if(b){
            this.setOnShowListener(new OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    CountDownTimer timer = new CountDownTimer(3100, 100) {

                        @Override
                        public void onTick(long l) {
                            //TextView seconds = (TextView)FightDialog.this.findViewById(R.id.secondsView);
                            //seconds.setText(String.valueOf(((int)l/1000)+1));
                        }

                        @Override
                        public void onFinish() {
                            //TextView seconds = (TextView)FightDialog.this.findViewById(R.id.secondsView);
                            //seconds.setText(String.valueOf(0));
                            FightDialog.this.dismiss();
                        }
                    };
                    timer.start();
                }
            });
        }
    }
}
