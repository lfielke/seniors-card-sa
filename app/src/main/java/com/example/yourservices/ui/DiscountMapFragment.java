package com.example.yourservices.ui;


import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yourservices.BootstrapServiceProvider;
import com.example.yourservices.Injector;
import com.example.yourservices.R;
import com.example.yourservices.model.DiscountOffer;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscountMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscountMapFragment extends Fragment {

    private static final String ARG_DISCOUNT_OFFER = "discountOffer";

    public static final LatLng INITIAL_MAP_CENTRE = new LatLng(-34.9544556, 138.6004172); // Adelaide
    public static final int INITIAL_MAP_ZOOM = 10;

    @Inject
    protected BootstrapServiceProvider mServiceProvider;

    @Nullable
    private DiscountOffer mSelectedDiscountOffer;

    @InjectView(R.id.map)
    protected MapView mMapView;

    @InjectView(R.id.tap_to_show_details)
    protected View mTapToShowDetails;

    @InjectView(R.id.details_container)
    protected View mDetailsContainer;

    @InjectView(R.id.title)
    protected TextView mTitleTxt;

    @InjectView(R.id.details)
    protected TextView mDetailsTxt;

    @Nullable
    private GoogleMap mMap;

    @NotNull
    private List<DiscountOffer> mDiscountOffers = new ArrayList<DiscountOffer>();


    private Map<Marker, DiscountOffer> mMarkersDiscountsMap = new HashMap<Marker, DiscountOffer>();


    // Loader
    private LoaderManager.LoaderCallbacks<List<DiscountOffer>> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<DiscountOffer>>() {
        @Override
        public Loader<List<DiscountOffer>> onCreateLoader(int id, Bundle args) {
            return new ThrowableLoader<List<DiscountOffer>>(getActivity(), null) {
                @Override
                public List<DiscountOffer> loadData() throws Exception {
                    try {
                        if (getActivity() != null) {
                            return mServiceProvider.getService(getActivity()).getDiscountOffers();
                        } else {
                            return Collections.emptyList();
                        }
                    } catch (final OperationCanceledException e) {
                        final Activity activity = getActivity();
                        if (activity != null)
                            activity.finish();
                        return new ArrayList<DiscountOffer>();
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<DiscountOffer>> loader, List<DiscountOffer> data) {
            final Exception exception = getException(loader);
            if (exception != null) {
                Toaster.showLong(getActivity(), "Loading discounts failed");
                return;
            }

//            Toaster.showLong(getActivity(), "Loaded " + data.size());

            mDiscountOffers = data;
            updatePins();
        }

        @Override
        public void onLoaderReset(Loader<List<DiscountOffer>> loader) {
            // Intentionally left blank
        }
    };


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static DiscountMapFragment newInstance(DiscountOffer discountOffer) {
        DiscountMapFragment fragment = new DiscountMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DISCOUNT_OFFER, discountOffer);
        fragment.setArguments(args);
        return fragment;
    }

    public DiscountMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        if (getArguments() != null) {
            Object discountOffer = getArguments().getSerializable(ARG_DISCOUNT_OFFER);
            if (discountOffer instanceof DiscountOffer) {
                mSelectedDiscountOffer = (DiscountOffer) discountOffer;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discount_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.inject(this, getView());

        MapsInitializer.initialize(getActivity());
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();


        if (mMap != null) {
            mMap.setMyLocationEnabled(true);

            // We have to wait until layout, so set initial bounds in this listener
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INITIAL_MAP_CENTRE, INITIAL_MAP_ZOOM));
                    mMap.setOnCameraChangeListener(null);
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    DiscountOffer discountOffer = mMarkersDiscountsMap.get(marker);
                    showDiscountOfferDetails(discountOffer);
                    return false;
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    showDiscountOfferDetails(null);
                }
            });
        }
//        mMapView.setClickable(true);
//
//        mMapView.getMap().setCenter(getPoint(40.76793169992044,
//                -73.98180484771729));
//        mMapView.getController().setZoom(17);
//        mMapView.setBuiltInZoomControls(true);
//
//        Drawable marker=getResources().getDrawable(R.drawable.marker);
//
//        marker.setBounds(0, 0, marker.getIntrinsicWidth(),
//                marker.getIntrinsicHeight());
//
//        mMapView.getOverlays().add(new SitesOverlay(marker));
//
//        MyLoctionOverLay me=new MyLocationOverlay(getActivity(), mMapView);
//        mMapView.getOverlays().add(me);

        getLoaderManager().initLoader(0, null, mLoaderCallbacks);
    }

    private void showDiscountOfferDetails(@Nullable DiscountOffer discountOffer) {
        if (discountOffer != null) {
            mTitleTxt.setText(discountOffer.getBusinessName());
            mDetailsTxt.setText(discountOffer.getDiscountOffer());
            mTapToShowDetails.setVisibility(View.GONE);
            mDetailsContainer.setVisibility(View.VISIBLE);
        } else {
            mTitleTxt.setText("");
            mDetailsTxt.setText("");
            mTapToShowDetails.setVisibility(View.VISIBLE);
            mDetailsContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    private void updatePins() {
        if (mMap != null) {

            for (DiscountOffer discountOffer : mDiscountOffers) {
                LatLng latLng = discountOffer.getLatLng();
                if (latLng != null) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(discountOffer.getBusinessName()));
                    mMarkersDiscountsMap.put(marker, discountOffer);
                }
            }
        }
    }


    protected Exception getException(final Loader<List<DiscountOffer>> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<List<DiscountOffer>>) loader).clearException();
        } else {
            return null;
        }
    }
}
