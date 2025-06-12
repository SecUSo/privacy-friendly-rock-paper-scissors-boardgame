package org.secuso.privacyfriendlyrockpaperscissorsboardgame.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.R
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.checkGoodbyeGoogle
import org.secuso.privacyfriendlyrockpaperscissorsboardgame.core.GameController

class HomeActivity : BaseActivity() {
    private var mViewPager: ViewPager? = null
    private var mArrowLeft: ImageView? = null
    private var mArrowRight: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false)
        setContentView(R.layout.activity_home)
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById<View>(R.id.scroller) as ViewPager
        if (mViewPager != null) {
            mViewPager!!.adapter = mSectionsPagerAdapter
        }

        val index = mSharedPreferences!!.getInt("lastChosenPage", 0)

        mViewPager!!.currentItem = index
        mArrowLeft = findViewById<View>(R.id.arrow_left) as ImageView
        mArrowRight = findViewById<View>(R.id.arrow_right) as ImageView

        //care for initial postiton of the ViewPager
        mArrowLeft!!.visibility = if ((index == 0)) View.INVISIBLE else View.VISIBLE
        mArrowRight!!.visibility = if ((index == mSectionsPagerAdapter.count - 1)) View.INVISIBLE else View.VISIBLE
        (findViewById<View>(R.id.GameDesc) as TextView).setText(R.string.sGameDescDefault)
        //Update ViewPager on change
        mViewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mArrowLeft!!.visibility = if ((position == 0)) View.INVISIBLE else View.VISIBLE
                mArrowRight!!.visibility = if ((position == mSectionsPagerAdapter.count - 1)) View.INVISIBLE else View.VISIBLE
                val gameDescription = findViewById<View>(R.id.GameDesc) as TextView
                when (position) {
                    GameController.MODE_NORMAL_AUTO -> gameDescription.setText(R.string.sGameDescDefault)
                    GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_AUTO -> gameDescription.setText(R.string.sGameDescLizardSpock)
                    GameController.MODE_NORMAL_MANUAL -> gameDescription.setText(R.string.sGameDescDefaultManual)
                    GameController.MODE_ROCKPAPERSCISSORSLIZARDSPOCK_MANUAL -> gameDescription.setText(R.string.sGameDescLizardSpockManual)
                }
                //save position in settings
                val editor = mSharedPreferences!!.edit()
                editor.putInt("lastChosenPage", position)
                editor.apply()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        checkGoodbyeGoogle(this, layoutInflater)
    }

    fun startGame(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(AI_EXTRA, false)
        intent.putExtra(GAMEMODE_EXTRA, mViewPager!!.currentItem)
        startActivity(intent)
    }

    fun startGameAI(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(AI_EXTRA, true)
        intent.putExtra(GAMEMODE_EXTRA, mViewPager!!.currentItem)
        startActivity(intent)
    }

    fun continueGame(view: View?) {
        val intent = Intent(this, ContinueActivity::class.java)
        startActivity(intent)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.arrow_left -> mViewPager!!.arrowScroll(View.FOCUS_LEFT)
            R.id.arrow_right -> mViewPager!!.arrowScroll(View.FOCUS_RIGHT)
            else -> {}
        }
    }

    override val navigationDrawerID: Int
        get() = R.id.nav_home

    inner class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position)
        }

        override fun getCount(): Int {
            // Show 4 total pages.
            return 4
        }
    }

    class PageFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            var id = 0
            if (arguments != null) {
                id = arguments!!.getInt(ARG_SECTION_NUMBER)
            }

            val rootView = inflater.inflate(R.layout.fragment_main_menu, container, false)

            val imageView = rootView.findViewById<View>(R.id.section_label) as ImageView
            when (id) {
                1 -> imageView.setBackgroundResource(R.mipmap.rpslsfix)
                2 -> imageView.setBackgroundResource(R.mipmap.rpsman)
                3 -> imageView.setBackgroundResource(R.mipmap.rpslsman)
                else -> imageView.setBackgroundResource(R.mipmap.rpsfix)
            }
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private const val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PageFragment {
                val fragment = PageFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {
        var GAMEMODE_EXTRA: String = "MODE"
        var AI_EXTRA: String = "AI"
    }
}