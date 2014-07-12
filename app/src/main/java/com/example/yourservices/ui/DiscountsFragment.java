package com.example.yourservices.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yourservices.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.ButterKnife;

/**
 * Fragment which houses the View pager.
 */
public class DiscountsFragment extends Fragment {

    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.inject(this, getView());
        setUpMapIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mGoogleMap == null) {
            mGoogleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map_frag)).getMap();
            // Check if we were successful in obtaining the map.
            if (mGoogleMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
                // Create a LatLngBounds that includes Australia.

                // Wait for first event, which occurs after layout
                mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition arg0) {

                        LatLngBounds mapBounds = new LatLngBounds(
//                new LatLng(-44, 113), new LatLng(-10, 154));
                                new LatLng(-35.294933, 138.2746), new LatLng(-33.9114, 139.373270));

                        // Set the camera to the greatest possible zoom level that includes the
                        // bounds
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(mapBounds, 0);
                        mGoogleMap.moveCamera(cameraUpdate);


                        // Remove listener to prevent position reset on camera move.
                        mGoogleMap.setOnCameraChangeListener(null);
                    }
                });
            }
        }
    }

}