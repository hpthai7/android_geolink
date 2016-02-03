package com.android.pfe.geolink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hpthai7 on 31/01/16.
 */
public class AddPlaceActivity extends AppCompatActivity {

    private LatLng mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_add_place);

        // If you are calling  setSupportActionBar() you don't need to use toolbar.inflateMenu()
        // because the Toolbar is acting as your ActionBar. All menu related callbacks are via the default ones
        // toolbar.inflateMenu(R.menu.menu_add_place);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        getCoordinate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.action_send:
                Utils.showToast(AddPlaceActivity.this, "Send onOptionsItemSelected");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void getCoordinate() {
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra(Constants.POS_LAT, 0);
        double lon = intent.getDoubleExtra(Constants.POS_LON, 0);
        mPosition = new LatLng(lat, lon);
    }

}
