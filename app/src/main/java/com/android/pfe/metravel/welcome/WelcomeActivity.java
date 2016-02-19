package com.android.pfe.metravel.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.pfe.metravel.R;
import com.android.pfe.metravel.common.Constants;
import com.android.pfe.metravel.common.Utils;
import com.android.pfe.metravel.settings.SettingsActivity;
import com.facebook.appevents.AppEventsLogger;

public class WelcomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = WelcomeActivity.class.getSimpleName();

    private int mCurrentFragmentId;

    private static final int CURRENT_LOCATION_FRAGMENT_ID = 1;
    private static final int FAVORITE_LOCATIONS_FRAGMENT_ID = 2;
    private static final int SETTINGS_FRAGMENT_ID = 3;

    private TextView mAccountName;
    private TextView mAccountEmail;

    private Fragment mCurrentLocationFragment;
    private Fragment mFavoriteLocationsFragment;
//    private Fragment mSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        mAccountName = (TextView) headerLayout.findViewById(R.id.account_name);
        mAccountEmail = (TextView) headerLayout.findViewById(R.id.account_email);

        showFragment(CURRENT_LOCATION_FRAGMENT_ID);

        Intent intent = getIntent();
        updateInfo();
    }

    private void updateInfo() {
        String fname = Utils.getFacebookInformation(getApplicationContext(), Constants.FB_FNAME);
        String lname = Utils.getFacebookInformation(getApplicationContext(), Constants.FB_LNAME);
        String name = fname == null ? lname : fname.concat(" ").concat(lname);
        String email = Utils.getFacebookInformation(getApplicationContext(), Constants.FB_EMAIL);
        if (name != null) {
            mAccountName.setText(name);
        }
        if (email != null) {
            mAccountEmail.setText(email);
        } else {
            mAccountEmail.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.startup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_current:
                showFragment(CURRENT_LOCATION_FRAGMENT_ID);
                break;
            case R.id.nav_favorites:
                showFragment(FAVORITE_LOCATIONS_FRAGMENT_ID);
                break;
            case R.id.nav_settings:
//                showFragment(SETTINGS_FRAGMENT_ID);
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_share:
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragmentId) {
        if (fragmentId == mCurrentFragmentId) {
            return;
        }
        mCurrentFragmentId = fragmentId;
        hideAllFragments();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (fragmentId) {
            case CURRENT_LOCATION_FRAGMENT_ID:
                if (mCurrentLocationFragment == null) {
                    mCurrentLocationFragment = new MapsFragment();
                    ft.add(R.id.container_view, mCurrentLocationFragment, Constants.CURRENT_LOCATION_FRAG);
                }
                ft.show(mCurrentLocationFragment);
                break;
            case FAVORITE_LOCATIONS_FRAGMENT_ID:
                if (mFavoriteLocationsFragment == null) {
                    mFavoriteLocationsFragment = new FavoritesFragment();
                    ft.add(R.id.container_view, mFavoriteLocationsFragment, Constants.FAVORITE_LOCATIONS_FRAG);
                }
                ft.show(mFavoriteLocationsFragment);
                break;
//            case SETTINGS_FRAGMENT_ID:
//                if (mSettingsFragment == null) {
//                    mSettingsFragment = new MapsFragment();
//                    ft.add(R.id.container_view, mSettingsFragment, Constants.SETTINGS_FRAG);
//                }
//                ft.show(mSettingsFragment);
//                break;
            default:
                break;
        }
        ft.commit();
    }

    private void hideAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mCurrentLocationFragment != null) {
            ft.hide(mCurrentLocationFragment);
        }
        if (mFavoriteLocationsFragment != null) {
            ft.hide(mFavoriteLocationsFragment);
        }
//        if (mSettingsFragment != null) {
//            ft.hide(mSettingsFragment);
//        }
        ft.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
}
