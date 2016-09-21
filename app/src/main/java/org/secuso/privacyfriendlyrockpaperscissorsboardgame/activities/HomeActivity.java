package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R;
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController;

public class HomeActivity extends BaseActivity {
    private ViewPager mViewPager;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    public static String GAMEMODE_EXTRA = "MODE";
    public static String AI_EXTRA = "AI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);
        setContentView(R.layout.activity_home);
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.scroller);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        int index = mSharedPreferences.getInt("lastChosenPage", 0);
        boolean welcomeScreenShown = mSharedPreferences.getBoolean("welcomeScreen",false);
        if(!welcomeScreenShown){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.sWelcomeTitle);
            builder.setMessage(R.string.sWelcome);
            builder.setPositiveButton(R.string.sDialogHandOverOkButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mSharedPreferences.edit().putBoolean("welcomeScreen",true).apply();
                }
            });
            builder.setNegativeButton(R.string.sHelp, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    goToNavigationItem(R.id.nav_help);
                }
            });
            Dialog dialog=builder.create();
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mSharedPreferences.edit().putBoolean("welcomeScreen",true).apply();
                }
            });
            dialog.show();
        }
        mViewPager.setCurrentItem(index);
        mArrowLeft = (ImageView) findViewById(R.id.arrow_left);
        mArrowRight = (ImageView) findViewById(R.id.arrow_right);

        //care for initial postiton of the ViewPager
        mArrowLeft.setVisibility((index == 0) ? View.INVISIBLE : View.VISIBLE);
        mArrowRight.setVisibility((index == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);
        ((TextView) HomeActivity.this.findViewById(R.id.GameDesc)).setText(R.string.sGameDescDefault);
        //Update ViewPager on change
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mArrowLeft.setVisibility((position == 0) ? View.INVISIBLE : View.VISIBLE);
                mArrowRight.setVisibility((position == mSectionsPagerAdapter.getCount() - 1) ? View.INVISIBLE : View.VISIBLE);
                TextView gameDescription = (TextView) HomeActivity.this.findViewById(R.id.GameDesc);
                switch (position) {
                    case GameController.MODE_NORMAL_AUTO:
                        gameDescription.setText(R.string.sGameDescDefault);
                        break;
                    case GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_AUTO:
                        gameDescription.setText(R.string.sGameDescLizardSpock);
                        break;
                    case GameController.MODE_NORMAL_MANUAL:
                        gameDescription.setText(R.string.sGameDescDefaultManual);
                        break;
                    case GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_MANUAL:
                        gameDescription.setText(R.string.sGameDescLizardSpockManual);
                        break;
                }
                //save position in settings
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt("lastChosenPage", position);
                editor.apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(AI_EXTRA, false);
        intent.putExtra(GAMEMODE_EXTRA, this.mViewPager.getCurrentItem());
        startActivity(intent);

    }

    public void startGameAI(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(AI_EXTRA, true);
        intent.putExtra(GAMEMODE_EXTRA, this.mViewPager.getCurrentItem());
        startActivity(intent);

    }

    public void continueGame(View view){
        Intent intent = new Intent(this,ContinueActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_left:
                mViewPager.arrowScroll(View.FOCUS_LEFT);
                break;
            case R.id.arrow_right:
                mViewPager.arrowScroll(View.FOCUS_RIGHT);
                break;
            default:
        }
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_home;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }

    public static class PageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PageFragment() {

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int id = 0;
            if (getArguments() != null) {
                id = getArguments().getInt(ARG_SECTION_NUMBER);
            }

            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);

            ImageView imageView = (ImageView) rootView.findViewById(R.id.section_label);
            switch(id){
                case 1:
                    imageView.setBackgroundResource(R.drawable.rpslsfix);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.drawable.rpsman);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.drawable.rpslsman);
                    break;
                default:
                    imageView.setBackgroundResource(R.drawable.rpsfix);
                    break;
            }
            return rootView;
        }
    }
}
