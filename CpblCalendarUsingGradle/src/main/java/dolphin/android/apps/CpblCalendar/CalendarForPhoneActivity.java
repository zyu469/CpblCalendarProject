package dolphin.android.apps.CpblCalendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;

import dolphin.android.apps.CpblCalendar.provider.ActionBarDrawerToggle;
import dolphin.android.apps.CpblCalendar.provider.CpblCalendarHelper;
import dolphin.android.apps.CpblCalendar.provider.Game;

//import com.espian.showcaseview.OnShowcaseEventListener;
//import com.espian.showcaseview.ShowcaseView;

/**
 * Created by dolphin on 2013/6/3.
 * <p/>
 * CalendarActivity for phone version, with a ActionBarDrawer pane.
 */
public class CalendarForPhoneActivity extends CalendarActivity
        implements CalendarActivity.OnQueryCallback/*, OnShowcaseEventListener*/ {
    private DrawerLayout mDrawerLayout;
    private View mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mProgressView;
    private TextView mProgressText;//[84]dolphin++

//    TabHost mTabHost;
//    ViewPager mViewPager;
//    FragmentTabsAdapter mTabsAdapter;

    private ArrayList<Game> mGameList = null;

//    private ShowcaseView mShowcaseView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_phone);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //used to avoid clicking objects behind drawer
            }
        });

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        if (Calendar.getInstance().get(Calendar.YEAR) < 2014) {//[70]dolphin++ lock drawer
            // enable ActionBar app icon to behave as action to toggle nav drawer
            getSActionBar().setDisplayHomeAsUpEnabled(true);
            getSActionBar().setHomeButtonEnabled(true);

            // ActionBarDrawerToggle ties together the the proper interactions
            // between the sliding drawer and the action bar app icon
            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                       /* host Activity */
                    mDrawerLayout,              /* DrawerLayout object */
                    R.drawable.ic_drawer_light, /* nav drawer image to replace 'Up' caret */
                    R.string.app_name,     /* "open drawer" description for accessibility */
                    R.string.app_name     /* "close drawer" description for accessibility */
            ) {
                public void onDrawerClosed(View view) {
                    mDrawerLayout.setEnabled(true);
                    //getSupportActionBar().setTitle("closed");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                public void onDrawerOpened(View drawerView) {
                    mDrawerLayout.setEnabled(false);
                    //getSupportActionBar().setTitle("opened");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }

        //[70]dolphin++ lock drawer
        //http://stackoverflow.com/a/17165256/2673859
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mProgressView = findViewById(android.R.id.progress);
        mProgressText = (TextView) findViewById(android.R.id.message);
//        //setup tab-viewpager
//        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
//        mTabHost.setup();
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mTabsAdapter = new FragmentTabsAdapter(this, mTabHost, mViewPager);
        initQueryPane();
        //[18]dolphin++ check tutorial
        //[19]dolphin++ debug in engineer build
//        if (!isTutorialDone()) {
//            ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
//            co.hideOnClickOutside = false;
//            co.block = true;
//            //co.shotType = ShowcaseView.TYPE_ONE_SHOT;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                mShowcaseView = ShowcaseView.insertShowcaseView(android.R.id.home,
//                        this, getString(R.string.phone_activity_showcase_title),
//                        getString(R.string.phone_activity_showcase_detail), co);
//            } else {
//                mShowcaseView = ShowcaseView.insertShowcaseView(24.0f, 62.0f,
//                        this, getString(R.string.phone_activity_showcase_title),
//                        getString(R.string.phone_activity_showcase_detail), co);
//            }
//            try {//in case that will have exception, directly set tutorial flag true
//                mShowcaseView.setOnShowcaseEventListener(this);
//            } catch (Exception e) {
//                Log.e(TAG, "show tutorial exception: " + e.getMessage());
//                setTutorialDone();
//            }
//        } else {
//            mShowcaseView = null;
//        }

        //[39]dolphin++ for rotation
        final int kind = (savedInstanceState != null)
                ? savedInstanceState.getInt(KEY_GAME_KIND)
                : CpblCalendarHelper.getSuggestedGameKind(this);//[66]++
        final int field = (savedInstanceState != null)
                ? savedInstanceState.getInt(KEY_GAME_FIELD) : 0;
        final int year = (savedInstanceState != null)
                ? savedInstanceState.getInt(KEY_GAME_YEAR) : 0;
        final int month = (savedInstanceState != null)
                ? savedInstanceState.getInt(KEY_GAME_MONTH)
                : Calendar.getInstance().get(Calendar.MONTH);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mSpinnerField.setSelection(field);
                mSpinnerKind.setSelection(kind);
                mSpinnerYear.setSelection(year);
                mSpinnerMonth.setSelection(month);
                mButtonQuery.performClick();//load at beginning
            }
        });

//        mTabsAdapter.addTab(mTabHost.newTabSpec("upcoming").setIndicator(getString(R.string.tab_title_upcoming)),
//                GameListFragment.class, genFragmentExtras(false));
//        mTabsAdapter.addTab(mTabHost.newTabSpec("previous").setIndicator(getString(R.string.tab_title_previous)),
//                GameListFragment.class, genFragmentExtras(true));
//        if (savedInstanceState != null) {
//            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
//        }
    }

//    private Bundle genFragmentExtras(boolean bPast)
//    {
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("past", bPast);
//        return bundle;
//    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.splash, menu);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {//[29]dolphin++
            if (menu.findItem(R.id.action_leader_board) != null)
                menu.removeItem(R.id.action_leader_board);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        boolean visible = !drawerOpen & !IsQuery();
        MenuItem item = menu.findItem(R.id.menu_action_more);
        if (item != null) {//[25]dolphin++
            item.setVisible(visible);
        } else {
            menu.findItem(R.id.action_settings).setVisible(visible);
            menu.findItem(R.id.action_refresh).setVisible(visible);
        }
        if (Calendar.getInstance().get(Calendar.YEAR) < 2014) {//[70]dolphin++
            item = menu.findItem(R.id.action_leader_board);//[26]dolphin++
            if (item != null)
                item.setVisible(visible);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        //[21]dolphin-- not to hide the tutorial
        //if (mShowcaseView != null && mShowcaseView.isShown()) {//[18]dolphin++
        //    mShowcaseView.hide();
        //}
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        //do common actions in CalendarActivity
        return super.onOptionsItemSelected(item);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)//[70]dolphin++ check NullPointer
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        if (mDrawerToggle != null)//[70]dolphin++ check NullPointer
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public SherlockFragmentActivity getSFActivity() {
        return this;
    }

    @Override
    public OnQueryCallback getOnQueryCallback() {
        return this;
    }

    @Override
    public void onQueryStart() {
        mDrawerLayout.closeDrawer(mDrawerList);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
        onLoading(true);//[30]dolphin++
    }

    @Override
    public void onQueryStateChange(String msg) {
        Log.d(TAG, "onQueryUpdate: " + msg);
        if (mProgressText != null)
            mProgressText.setText(msg);
    }

    @Override
    public void onQuerySuccess(CpblCalendarHelper helper, ArrayList<Game> gameArrayList) {
        //Log.d(TAG, "onSuccess");
        updateGameListFragment(gameArrayList);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }

    private void updateGameListFragment(ArrayList<Game> gameArrayList) {
        mGameList = gameArrayList;//as a temp storage

        FragmentManager fmgr = getSupportFragmentManager();
        if (fmgr != null) {//update the fragment
            FragmentTransaction trans = fmgr.beginTransaction();
            GameListFragment frag1 =
                    (GameListFragment) fmgr.findFragmentById(R.id.content_frame);
            if (frag1 != null) {
                frag1.updateAdapter(gameArrayList);
                trans.commitAllowingStateLoss();//[30]dolphin++
            }
        }

        if (mProgressView != null)
            mProgressView.setVisibility(View.GONE);
        if (mProgressText != null)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//                mProgressText.animate().alpha(0).setDuration(500).start();
//            else
            mProgressText.setVisibility(View.GONE);
        setSupportProgressBarIndeterminateVisibility(false);//hide loading animation
    }

    @Override
    public void onQueryError() {
        //Log.e(TAG, "onError");
        onLoading(false);//[30]dolphin++
        updateGameListFragment(new ArrayList<Game>());//[59]++ no data
        Toast.makeText(getSFActivity(), R.string.query_error,
                Toast.LENGTH_SHORT).show();//[31]dolphin++
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }

    public void onLoading(boolean is_load) {
        FragmentManager fmgr = getSupportFragmentManager();
        if (fmgr != null) {
            FragmentTransaction trans = fmgr.beginTransaction();
            GameListFragment frag1 =
                    (GameListFragment) fmgr.findFragmentById(R.id.content_frame);
            if (frag1 != null)
                frag1.setListShown(is_load);
            //try {
            trans.commitAllowingStateLoss();//[30]dolphin++
            //} catch (IllegalStateException e) {
            //    Log.e(TAG, "onLoading: " + e.getMessage());
            //}
        }

        if (mProgressView != null)
            mProgressView.setVisibility(is_load ? View.VISIBLE : View.GONE);
        if (mProgressText != null)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//                mProgressText.animate().alpha(is_load ? 0 : 1).setDuration(500).start();
//            else
            mProgressText.setVisibility(is_load ? View.VISIBLE : View.GONE);
        setSupportProgressBarIndeterminateVisibility(is_load);
    }

//    @Override
//    protected void onResumeFragments() {
//        //http://stackoverflow.com/a/12450060
////        if (mGameList != null)//[30]dolphin++
////            updateGameListFragment(mGameList);
//        super.onResumeFragments();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //[22]dolphin++ need get the data from server again!
        // because the favorite team preference may remove some teams
        //onQuerySuccess(mGameList);//reload the fragment when possible preferences change
        mButtonQuery.performClick();//[22]dolphin++
    }

//    @Override
//    public void onShowcaseViewShow(ShowcaseView showcaseView) {
//        //Log.d(TAG, "onShowcaseViewShow");
//    }
//
//    @Override
//    public void onShowcaseViewHide(ShowcaseView showcaseView) {
//        //Log.d(TAG, "onShowcaseViewHide");
//        setTutorialDone();
//        mShowcaseView = null;//don't use it later
//    }
//
//    @Override
//    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
//        //Log.d(TAG, "onShowcaseViewDidHide");
//        setTutorialDone();
//        mShowcaseView = null;//don't use it later
//    }

    @Override
    public void OnFragmentAttached() {
        //[33]dolphin++
    }
}