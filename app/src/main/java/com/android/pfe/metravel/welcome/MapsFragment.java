package com.android.pfe.metravel.welcome;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.pfe.metravel.R;
import com.android.pfe.metravel.common.Constants;
import com.android.pfe.metravel.common.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private GoogleApiClient mGoogleApiClient;

    private Button mCreatePoiBtn;
    private Marker mSelection;
    private LatLng mPosition;

    protected LocationRequest mLocationRequest;

    // Called when the Fragment is attached to its parent Activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Get a reference to the parent Activity.
    }

    // Called to do the initial creation of the Fragment.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the Fragment.
        createLocationRequest();
    }

    // Called once the Fragment has been created in order for it to
    // create its user interface.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Create, or inflate the Fragment's UI, and return it.
        // If this Fragment has no UI then return null.
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mCreatePoiBtn = (Button) view.findViewById(R.id.btn_create_poi);
        mCreatePoiBtn.setVisibility(View.INVISIBLE);
        mCreatePoiBtn.setOnClickListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        return view;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        int UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        int FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 20000;
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createMarker(LatLng position) {
        mMap.clear();
        mSelection = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title("Selection")
                .snippet(position.toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        if (!mSelection.isInfoWindowShown()) {
            mSelection.showInfoWindow();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mUiSettings = mMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setTiltGesturesEnabled(true);
            mUiSettings.setRotateGesturesEnabled(true);
            mUiSettings.setMapToolbarEnabled(true);

            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);
        } else {
            // Show rationale and request permission.
            Utils.showToast(getContext(), "ACCESS_FINE_LOCATION not granted");
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
//        if (!Utils.isLocationEnabled(getActivity())) {
//            new LocationRequestDialog().show(getFragmentManager(), null);
//        }
        return false;
    }

    public class LocationRequestDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            setView(inflater.inflate(R.layout.dialog_location_request, null))
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_location_request)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mCreatePoiBtn.setVisibility(View.INVISIBLE);
        mMap.clear();
        mMap.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mPosition = latLng;
        mCreatePoiBtn.setVisibility(View.VISIBLE);
        createMarker(latLng);
        mMap.setPadding(0, 0, 0, mCreatePoiBtn.getHeight());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_poi:
                Intent intent = new Intent(this.getContext(), AddLocationActivity.class);
                intent.putExtra(Constants.POS_LAT, mPosition.latitude);
                intent.putExtra(Constants.POS_LON, mPosition.longitude);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
