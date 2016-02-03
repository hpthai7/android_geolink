package com.android.pfe.geolink;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.pfe.geolink.data.GeoProvider;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hpthai7 on 31/01/16.
 */
public class AddPlaceActivity extends AppCompatActivity {

    private EditText mNameEdt;
    private EditText mAddressEdt;
    private EditText mCategoryEdt;
    private EditText mCommentEdt;
    private LatLng mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        mNameEdt = (EditText) findViewById(R.id.name);
        mAddressEdt = (EditText) findViewById(R.id.address);
        mCategoryEdt = (EditText) findViewById(R.id.category);
        mCommentEdt = (EditText) findViewById(R.id.comment);

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
                addPosition();
                Utils.showToast(AddPlaceActivity.this, "Position added");
                finish();
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

    private Uri addPosition() {
        String name = mNameEdt.getText().toString();
        String address = mAddressEdt.getText().toString();
        String category = mCategoryEdt.getText().toString();
        String comment = mCommentEdt.getText().toString();
        if (name == null || address == null) {
            return null;
        }
        ContentValues cv = new ContentValues();
        cv.put(GeoProvider.GEO_NAME, name);
        cv.put(GeoProvider.GEO_ADDRESS, address);
        cv.put(GeoProvider.GEO_CATEGORY, category);
        cv.put(GeoProvider.GEO_COMMENT, comment);
        cv.put(GeoProvider.GEO_LATITUDE, mPosition.latitude);
        cv.put(GeoProvider.GEO_LONGTITUDE, mPosition.longitude);

        ContentResolver cr = getContentResolver();
        Uri uri = cr.insert(GeoProvider.CONTENT_URI, cv);
        return uri;
    }

}
