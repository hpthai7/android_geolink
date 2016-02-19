package com.android.pfe.metravel.welcome;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.android.pfe.metravel.R;
import com.android.pfe.metravel.common.Constants;
import com.android.pfe.metravel.common.Utils;
import com.android.pfe.metravel.data.GeoProvider;
import com.android.pfe.metravel.login.LoginActivity;

public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GEO_LOADER = 0;
    private static final boolean DBG = false;
    private static final String TAG = FavoritesFragment.class.getSimpleName();

    private ListView mListView;
    private LocationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(GEO_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mListView = (ListView) view.findViewById(R.id.location_list);
        return view;
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String fbid = Utils.getFacebookInformation(getActivity().getApplicationContext(), Constants.FB_ID);
        if (fbid == null) {
            return null;
        }
        CursorLoader loader = null;
        String[] projection = null;
        String selection = GeoProvider.GEO_FBID + " = ?";
        String[] selectionArgs = new String[] { fbid };
        String sortOrder = null;
        switch (id) {
            case GEO_LOADER:
                loader = new CursorLoader(getActivity(),
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
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, final Cursor data) {
        switch (loader.getId()) {
            case GEO_LOADER:
                if (mAdapter == null) {
                    mAdapter = new LocationAdapter(getActivity(), data, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            LocationAdapter.ViewHolder viewHolder = (LocationAdapter.ViewHolder) view.getTag();
                            int idx = viewHolder.getId();
//                            Intent intent = new Intent(CollectionActivity.this, StartupActivity.class);
//                            intent.putExtra(GeoProvider.KEY_ID, idx);
//                            startActivity(intent);
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

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
