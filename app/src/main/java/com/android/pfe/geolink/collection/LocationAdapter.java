package com.android.pfe.geolink.collection;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.android.pfe.geolink.R;
import com.android.pfe.geolink.data.GeoProvider;

/**
 * Created by hpthai7 on 03/02/16.
 */
public class LocationAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    /**
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public LocationAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final View itemLayout = mInflater.inflate(R.layout.collection_list_item, parent, false);

        // Creates a new ViewHolder in which to store handles to each view resource. This
        // allows bindView() to retrieve stored references instead of calling findViewById for
        // each instance of the layout.
        final ViewHolder holder = new ViewHolder();
        holder.address = (TextView) itemLayout.findViewById(R.id.address);
        holder.category = (ImageView) itemLayout.findViewById(R.id.category);
        holder.name = (TextView) itemLayout.findViewById(R.id.name);
        holder.thumbnail = (ImageView) itemLayout.findViewById(R.id.thumbnail);
        itemLayout.setTag(holder);
        return itemLayout;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
// Gets handles to individual view resources
        final ViewHolder holder = (ViewHolder) view.getTag();
        final int idx = cursor.getColumnIndex(GeoProvider.KEY_ID);
        final int nameColIdx = cursor.getColumnIndex(GeoProvider.GEO_NAME);
        final int addrColIdx = cursor.getColumnIndex(GeoProvider.GEO_ADDRESS);
        holder.idx = cursor.getInt(idx);
        holder.name.setText(cursor.getString(nameColIdx));
        holder.address.setText(cursor.getString(addrColIdx));
        holder.thumbnail.setImageResource(R.drawable.ic_android_black_24dp);
        holder.category.setImageResource(R.drawable.ic_location_city_black_24dp);
    }

    public class ViewHolder {
        private int idx;
        private ImageView thumbnail;
        private ImageView category;
        private TextView name;
        private TextView address;

        public int getId() {
            return idx;
        }

    }
}
