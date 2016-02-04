package com.android.pfe.geolink.collection;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.android.pfe.geolink.R;
import com.android.pfe.geolink.data.GeoProvider;

public class CollectionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GEO_LOADER = 0;
    private static final boolean DBG = false;
    private static final String TAG = CollectionActivity.class.getSimpleName();

    private ListView mListView;
    private LocationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
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

        getLoaderManager().initLoader(GEO_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        switch (id) {
            case GEO_LOADER:
                loader = new CursorLoader(this,
                        GeoProvider.CONTENT_URI,
                        projection,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            default:
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case GEO_LOADER:
                if (mAdapter == null) {
                    mAdapter = new LocationAdapter(this, data, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    mListView = (ListView) findViewById(R.id.location_list);
                    mListView.setAdapter(mAdapter);

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LocationAdapter.ViewHolder viewHolder = (LocationAdapter.ViewHolder) view.getTag();
                            int idx = viewHolder.getId();
                        }
                    });
                } else {
                    // An underlying video having its thumbnail created
                    // onLoadFinished is called once again
                    // mAdapter.notifyDataSetChanged();
                    mAdapter.swapCursor(data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
