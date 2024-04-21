package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.TaskStackBuilder
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities.SettingsActivity.GeneralPreferenceFragment
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.tutorial.TutorialActivity


/**
 * Created by Chris on 04.07.2016.
 */
open class BaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Navigation drawer:
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null

    // Helper
    private var mHandler: Handler? = null
    protected var mSharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        mHandler = Handler()

        //ActionBar ab = getSupportActionBar();
        //if (ab != null) {
        //    mActionBar = ab;
        //    ab.setDisplayHomeAsUpEnabled(true);
        //}
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    protected open val navigationDrawerID: Int
        get() = 0

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId

        return goToNavigationItem(itemId)
    }

    protected fun goToNavigationItem(itemId: Int): Boolean {
        if (itemId == navigationDrawerID) {
            // just close drawer because we are already in this activity
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
            return true
        }

        // delay transition so the drawer can close
        mHandler!!.postDelayed({ callDrawerItem(itemId) }, NAVDRAWER_LAUNCH_DELAY.toLong())

        mDrawerLayout!!.closeDrawer(GravityCompat.START)

        selectNavigationItem(itemId)

        // fade out the active activity
        val mainContent = findViewById<View>(R.id.main_content)
        mainContent?.animate()?.alpha(0f)?.setDuration(MAIN_CONTENT_FADEOUT_DURATION.toLong())
        return true
    }

    // set active navigation item
    private fun selectNavigationItem(itemId: Int) {
        for (i in 0 until mNavigationView!!.menu.size()) {
            val b = itemId == mNavigationView!!.menu.getItem(i).itemId
            mNavigationView!!.menu.getItem(i).setChecked(b)
        }
    }

    /**
     * Enables back navigation for activities that are launched from the NavBar. See
     * `AndroidManifest.xml` to find out the parent activity names for each activity.
     *
     * @param intent
     */
    private fun createBackStack(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val builder = TaskStackBuilder.create(this)
            builder.addNextIntentWithParentStack(intent)
            builder.startActivities()
        } else {
            startActivity(intent)
            finish()
        }
    }

    private fun callDrawerItem(itemId: Int) {
        val intent: Intent

        when (itemId) {
            R.id.nav_home -> {
                intent = Intent(this, HomeActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

            R.id.nav_about -> {
                intent = Intent(this, AboutActivity::class.java)
                createBackStack(intent)
            }

            R.id.nav_help -> {
                intent = Intent(this, HelpActivity::class.java)
                createBackStack(intent)
            }

            R.id.nav_tutorial -> {
                intent = Intent(this, TutorialActivity::class.java)
                intent.setAction(TutorialActivity.ACTION_SHOW_ANYWAYS)
                createBackStack(intent)
            }

            R.id.nav_settings -> {
                intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, GeneralPreferenceFragment::class.java.name)
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
                createBackStack(intent)
            }

            else -> {}
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        if (supportActionBar == null) {
            setSupportActionBar(toolbar)
        }

        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, R.string.sNavigationDrawerOpen, R.string.sNavigationDrawerClose
        )
        mDrawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        mNavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        mNavigationView!!.setNavigationItemSelectedListener(this)

        selectNavigationItem(navigationDrawerID)

        val mainContent = findViewById<View>(R.id.main_content)
        if (mainContent != null) {
            mainContent.alpha = 0f
            mainContent.animate().alpha(1f).setDuration(MAIN_CONTENT_FADEIN_DURATION.toLong())
        }
    }


    companion object {
        // delay to launch nav drawer item, to allow close animation to play
        const val NAVDRAWER_LAUNCH_DELAY: Int = 250

        // fade in and fade out durations for the main content when switching between
        // different Activities of the app through the Nav Drawer
        const val MAIN_CONTENT_FADEOUT_DURATION: Int = 150
        const val MAIN_CONTENT_FADEIN_DURATION: Int = 250
    }
}