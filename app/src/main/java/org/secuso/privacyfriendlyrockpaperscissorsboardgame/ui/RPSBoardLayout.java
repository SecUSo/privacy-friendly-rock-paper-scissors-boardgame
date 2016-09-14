package org.secuso.privacyfriendlyrockpaperscissorsboardgame.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.GameActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.HomeActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.SettingsActivity;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.Coordinate;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.IPlayer;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.Move;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSFigure;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.RPSGameFigure;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by david on 11.06.2016.
 */
public class RPSBoardLayout extends GridLayout {
    private RPSFieldView[][] board;
    private AttributeSet attrs;
    private GameController gameController;
    private int xSel;
    private int ySel;

    public RPSBoardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public void createBoard(GameController gameController) {
        boolean black = true;
        this.board = new RPSFieldView[gameController.getY()][gameController.getX()];
        this.gameController = gameController;
        this.setRowCount(gameController.getX());
        this.setColumnCount(gameController.getY());
        for (int i = 0; i < gameController.getY(); i++) {
            black = !black;
            for (int j = 0; j < gameController.getX(); j++) {
                board[i][j] = new RPSFieldView(getContext(), this.attrs, black, j, i);
                black = !black;
                addView(board[i][j]);
            }
        }
        requestLayout();
        gameController.startGame(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.gameController.isGameFinished())
            return true;
        RPSFieldView target;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            target = this.findViewByAbsoluteLocation(event.getRawX(), event.getRawY());
            if (target != null) {
                this.xSel = target.getxIndex();
                this.ySel = target.getyIndex();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            target = this.findViewByAbsoluteLocation(event.getRawX(), event.getRawY());
            if (target != null) {
                if (xSel == target.getxIndex() && ySel == target.getyIndex()) { //This is for a Tap Action
                    //Covers Motions where UP and DOWN had the same target
                    if (this.gameController.isCellSelected())
                        if (this.xSel == this.gameController.getSelX() && this.ySel == this.gameController.getSelY())
                            this.gameController.deselect(); //Tap a selected field to deselect it
                        else this.gameController.playerMove(target.getxIndex(), target.getyIndex());
                    else this.gameController.selectCell(this.xSel, this.ySel);
                } else {   //This is for a Swipe Action
                    if (this.gameController.isCellSelected())
                        this.gameController.deselect();         //Ignore previous selected Fields.
                    this.gameController.selectCell(this.xSel, this.ySel);
                    this.gameController.playerMove(target.getxIndex(), target.getyIndex());
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Draws the figures to the Pane
     *
     * @param pane the GamePane to take the figures from, this is already oriented correctly
     */
    public void drawFigures(RPSGameFigure[][] pane, IPlayer onTurn) {
        ((TextView) ((LinearLayout) RPSBoardLayout.this.getParent()).findViewById(R.id.playerIndicator)).setText(getResources().getString(R.string.onTurn, onTurn.getId()));
        for (int i = 0; i < pane.length; i++) {
            for (int j = 0; j < pane[i].length; j++) {
                if(board[i][j].black){
                    board[i][j].setBackgroundColor(Color.BLACK);
                }
                else board[i][j].setBackgroundColor(Color.WHITE);
                if (pane[i][j] != null) {
                    if (pane[i][j].getOwner().equals(onTurn)) {
                        board[i][j].setImage(pane[i][j].getType(), pane[i][j].getOwner().getColor());
                    } else
                        board[i][j].setImage(pane[i][j].isHidden() ? RPSFigure.GHOST : pane[i][j].getType(), pane[i][j].getOwner().getColor());
                } else board[i][j].setImage(null, Color.TRANSPARENT);
            }
        }
    }

    /**
     * Returns the RPSFieldView that Contains the Location x,y
     *
     * @param x the x Direction of the Location
     * @param y the y Direction of the Location
     * @return the RPSFieldView containing the location or null if no RPSFieldView contains it
     */
    RPSFieldView findViewByAbsoluteLocation(float x, float y) {
        for (int i = 0; i < RPSBoardLayout.this.board.length; i++) {
            for (int j = 0; j < RPSBoardLayout.this.board[i].length; j++) {
                Rect r = new Rect();
                Point p = new Point();
                board[i][j].getGlobalVisibleRect(r, p);
                if (r.contains((int) x, (int) y))
                    return board[i][j];
            }
        }
        return null;
    }

    public void handleTurnover(final IPlayer player) {
        this.clearBoard();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder((Activity) this.getContext());
        dialogBuilder.setMessage(R.string.sDialogHandOverMessage);
        dialogBuilder.setTitle(R.string.sDialogHandOverTitle);
        dialogBuilder.setPositiveButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawFigures(RPSBoardLayout.this.gameController.getRepresentationForPlayer(), player);
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                handleTurnover(player);
            }
        });
        dialog.show();
    }

    private void clearBoard() {
        for (RPSFieldView[] row : this.board) {
            for (RPSFieldView view : row) {
                view.setImage(null, -1);
            }
        }
    }

    public void highlightDestinations(List<Coordinate> destinations,Coordinate selected) {
        this.board[selected.getY()][selected.getX()].setBackgroundColor(Color.GREEN);
        for (Coordinate c : destinations)
                this.board[c.getY()][c.getX()].setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.yellow,null));

    }

    public void showFightDialog(RPSGameFigure attacker, RPSGameFigure attacked, RPSGameFigure winner, DialogInterface.OnDismissListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
        final View fightDialogView = inflater.inflate(R.layout.fight_dialog_layout, null);
        builder.setView(fightDialogView);
        builder.setPositiveButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(R.string.sFightDialogTitle);
        ImageView attackerImageView = (ImageView) fightDialogView.findViewById(R.id.imageView1);
        ImageView attackedImageView = (ImageView) fightDialogView.findViewById(R.id.imageView2);
        ImageView winnerImageView = (ImageView) fightDialogView.findViewById(R.id.imageView3);
        attackedImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), attacked.getType().getImageResourceId(), null));
        attackerImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), attacker.getType().getImageResourceId(), null));
        winnerImageView.setImageDrawable(ResourcesCompat.getDrawable(this.getContext().getResources(), winner.getType().getImageResourceId(), null));
        final AlertDialog fightDialog = builder.create();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean b=prefs.getBoolean(SettingsActivity.KEY_TIMER_SWITCH,true);
        if(b){
            int seconds=Integer.valueOf(prefs.getString(SettingsActivity.KEY_TIMER_LIST,"3"));
            CountDownTimer timer = new CountDownTimer(seconds*1000+100, 100) {

                @Override
                public void onTick(long l) {
                    fightDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText(getResources().getString(R.string.sFightDialogSeconds, (int) l / 1000));
                }

                @Override
                public void onFinish() {
                    fightDialog.dismiss();
                }
            };
            timer.start();
        }
        fightDialog.setOnDismissListener(listener);
        fightDialog.show();
    }

    public void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) this.getContext());
        builder.setTitle(R.string.sWinDialogTitle);
        builder.setMessage(R.string.sWinDialogText);
        builder.setIcon(ResourcesCompat.getDrawable(this.getResources(), R.drawable.award_medal, null));
        builder.setPositiveButton(R.string.sWinDialogBack, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(RPSBoardLayout.this.getContext(), HomeActivity.class);
                RPSBoardLayout.this.getContext().startActivity(intent);

            }
        });
        builder.setNegativeButton(R.string.sWinDialogShowBoard, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog winDialog = builder.create();
        winDialog.show();
    }

    public void animateTurn(Move move, Animation.AnimationListener listener) {
        RPSFieldView start = this.board[move.getyStart()][move.getxStart()];
        int newBounds[][] = new int[4][1];
        Rect oldBounds = new Rect(start.getLeft(), start.getTop(), start.getRight(), start.getBottom());
        newBounds[0][0] = oldBounds.left;
        newBounds[1][0] = oldBounds.top - (oldBounds.bottom - oldBounds.top);
        newBounds[2][0] = oldBounds.right;
        newBounds[3][0] = oldBounds.bottom - (oldBounds.bottom - oldBounds.top);
        /*Drawable d = start.getDrawable().mutate();
        ObjectAnimator animator = ObjectAnimator.ofMultiInt(d, "bounds", newBounds);
        animator.setDuration(2000);
        animator.addListener(listener);
        animator.start();*/
        RPSFieldView finish=this.board[move.getyTarget()][move.getxTarget()];
        TranslateAnimation translation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setFillEnabled(true);
        translation.setAnimationListener(listener);
        start.setAnimation(translation);
        start.startAnimation(translation);
    }

    public void animateFight(Move move, Animation.AnimationListener listener) {
        RPSFieldView start = this.board[move.getyStart()][move.getxStart()];
        int newBounds[][] = new int[4][1];
        Rect oldBounds = new Rect(start.getLeft(), start.getTop(), start.getRight(), start.getBottom());
        newBounds[0][0] = oldBounds.left;
        newBounds[1][0] = oldBounds.top - (oldBounds.bottom - oldBounds.top);
        newBounds[2][0] = oldBounds.right;
        newBounds[3][0] = oldBounds.bottom - (oldBounds.bottom - oldBounds.top);
        /*Drawable d = start.getDrawable().mutate();
        ObjectAnimator animator = ObjectAnimator.ofMultiInt(d, "bounds", newBounds);
        animator.setDuration(2000);
        animator.addListener(listener);
        animator.start();*/
        TranslateAnimation translation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        translation.setDuration(2000);
        translation.setFillAfter(true);
        translation.setFillEnabled(true);
        translation.setAnimationListener(listener);
        start.setAnimation(translation);
        start.startAnimation(translation);
    }

    public void getNewType(final int gameMode, final IPlayer player, final boolean attacker) {
        this.clearBoard();
        AlertDialog.Builder builder = new AlertDialog.Builder((Activity) this.getContext());
        ArrayList<CharSequence> items = new ArrayList<CharSequence>();
        items.add(RPSFigure.ROCK.getName());
        items.add(RPSFigure.PAPER.getName());
        items.add(RPSFigure.SCISSOR.getName());
        if (gameMode == GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_AUTO || gameMode == GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_MANUAL) {
            items.add(RPSFigure.LIZARD.getName());
            items.add(RPSFigure.SPOCK.getName());
        }
        CharSequence[] itemsArray = new CharSequence[items.size()];
        builder.setSingleChoiceItems(items.toArray(itemsArray), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RPSBoardLayout.this.gameController.handleDrawRoutine(i, attacker);
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(getResources().getString(R.string.sDrawDialogTitle, player.getId()));
        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                getNewType(gameMode, player, attacker);
            }
        });
        dialog.show();
    }

    public void showAssignmentDialog(final IPlayer player, int gameMode){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
        final View assignmentDialogView = inflater.inflate(R.layout.starting_types_layout, null);
        final List<SeekBar> seekbars= new ArrayList<>();
        List<TextView> textViews= new ArrayList<>();
        final Map<RPSFigure,SeekBar> figureToSeekbar= new HashMap<>();
        final Map<RPSFigure,TextView> figureToTextView= new HashMap<>();
        seekbars.add((SeekBar)assignmentDialogView.findViewById(R.id.seekbarRock));
        textViews.add((TextView)assignmentDialogView.findViewById(R.id.assignmentRock));
        figureToSeekbar.put(RPSFigure.ROCK,(SeekBar)assignmentDialogView.findViewById(R.id.seekbarRock));
        figureToTextView.put(RPSFigure.ROCK,(TextView)assignmentDialogView.findViewById(R.id.assignmentRock));
        seekbars.add((SeekBar)assignmentDialogView.findViewById(R.id.seekbarPaper));
        textViews.add((TextView)assignmentDialogView.findViewById(R.id.assignmentPaper));
        figureToSeekbar.put(RPSFigure.PAPER,(SeekBar)assignmentDialogView.findViewById(R.id.seekbarPaper));
        figureToTextView.put(RPSFigure.PAPER,(TextView)assignmentDialogView.findViewById(R.id.assignmentPaper));
        seekbars.add((SeekBar)assignmentDialogView.findViewById(R.id.seekbarScissor));
        textViews.add((TextView)assignmentDialogView.findViewById(R.id.assignmentScissor));
        figureToSeekbar.put(RPSFigure.SCISSOR,(SeekBar)assignmentDialogView.findViewById(R.id.seekbarScissor));
        figureToTextView.put(RPSFigure.SCISSOR,(TextView)assignmentDialogView.findViewById(R.id.assignmentScissor));
        if(gameMode==GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_MANUAL){
            seekbars.add((SeekBar)assignmentDialogView.findViewById(R.id.seekbarLizard));
            textViews.add((TextView)assignmentDialogView.findViewById(R.id.assignmentLizard));
            figureToSeekbar.put(RPSFigure.LIZARD,(SeekBar)assignmentDialogView.findViewById(R.id.seekbarLizard));
            figureToTextView.put(RPSFigure.LIZARD,(TextView)assignmentDialogView.findViewById(R.id.assignmentLizard));
            seekbars.add((SeekBar)assignmentDialogView.findViewById(R.id.seekbarSpock));
            textViews.add((TextView)assignmentDialogView.findViewById(R.id.assignmentSpock));
            figureToSeekbar.put(RPSFigure.SPOCK,(SeekBar)assignmentDialogView.findViewById(R.id.seekbarSpock));
            figureToTextView.put(RPSFigure.SPOCK,(TextView)assignmentDialogView.findViewById(R.id.assignmentSpock));
        }
        final Queue <SeekBar> seekBarQueue = new LinkedBlockingQueue<>(seekbars.size());
        final Queue <SeekBar> lastModified = new LinkedBlockingQueue<>(1);
        for(SeekBar s: seekbars){
            seekBarQueue.add(s);
        }
        for(int i=0;i<seekbars.size();i++){
            seekbars.get(i).setProgress(15/seekbars.size());
            seekbars.get(i).setVisibility(VISIBLE);
            seekbars.get(i).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if(b){
                        int totalProgress=0;
                        for(SeekBar s: seekbars){
                            totalProgress+=s.getProgress();
                        }
                        int progressToCompensate=15-totalProgress;
                        int summand=(progressToCompensate>0)?1:-1;
                        SeekBar last=lastModified.poll();
                        lastModified.add(seekBar);
                        if(last==null)
                            last=seekBar;
                        while(progressToCompensate!=0){
                            SeekBar toModify=seekBarQueue.poll();
                            if(toModify.equals(seekBar)||toModify.equals(last)){
                                seekBarQueue.add(toModify);
                                continue;
                            }
                            toModify.incrementProgressBy(summand);
                            seekBarQueue.add(toModify);
                            progressToCompensate-=summand;
                        }
                        if(seekBar.getProgress()==15){
                            for(SeekBar s: seekbars){
                                if(!s.equals(seekBar))
                                    s.setProgress(0);
                            }
                        }
                    }
                    for(RPSFigure fig:RPSFigure.values()){
                        SeekBar s=figureToSeekbar.get(fig);
                        if(s==null)
                            continue;
                        TextView view=figureToTextView.get(fig);
                        switch(fig){
                            case ROCK: view.setText(getResources().getString(R.string.sAssignmentRock,s.getProgress()));
                                break;
                            case PAPER: view.setText(getResources().getString(R.string.sAssignmentPaper,s.getProgress()));
                                break;
                            case SCISSOR: view.setText(getResources().getString(R.string.sAssignmentScissor,s.getProgress()));
                                break;
                            case LIZARD: view.setText(getResources().getString(R.string.sAssignmentLizard,s.getProgress()));
                                break;
                            case SPOCK: view.setText(getResources().getString(R.string.sAssignmentSpock,s.getProgress()));
                                break;
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            textViews.get(i).setVisibility(VISIBLE);
        }
        builder.setView(assignmentDialogView);
        for(RPSFigure fig:RPSFigure.values()){
            SeekBar s=figureToSeekbar.get(fig);
            if(s==null)
                continue;
            TextView view=figureToTextView.get(fig);
            switch(fig){
                case ROCK: view.setText(getResources().getString(R.string.sAssignmentRock,s.getProgress()));
                    break;
                case PAPER: view.setText(getResources().getString(R.string.sAssignmentPaper,s.getProgress()));
                    break;
                case SCISSOR: view.setText(getResources().getString(R.string.sAssignmentScissor,s.getProgress()));
                    break;
                case LIZARD: view.setText(getResources().getString(R.string.sAssignmentLizard,s.getProgress()));
                    break;
                case SPOCK: view.setText(getResources().getString(R.string.sAssignmentSpock,s.getProgress()));
                    break;
            }
        }
        builder.setPositiveButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<RPSFigure,Integer> team=new HashMap<RPSFigure, Integer>();
                for(Map.Entry<RPSFigure,SeekBar> p: figureToSeekbar.entrySet()){
                    team.put(p.getKey(),p.getValue().getProgress());
                }
                RPSBoardLayout.this.gameController.submitStartingTeam(team, player);
                dialogInterface.dismiss();
            }
        });
        builder.setTitle(getResources().getString(R.string.sAssignmentTitle,player.getId()));
        AlertDialog dialog= builder.create();
        dialog.show();
    }


}
