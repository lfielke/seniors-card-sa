package com.example.yourservices.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.yourservices.R;
import com.example.yourservices.core.Constants;
import com.example.yourservices.model.DiscountOffer;
import com.example.yourservices.util.Ln;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;

import butterknife.InjectView;
import butterknife.OnClick;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class DiscountOfferActivity extends BootstrapActivity {

    private static final LatLng DEFAULT_LOCATION = new LatLng(-34.9067875, 138.6060122);
    public static final String TAG_RATING_DIALOG = "rating_dialog";
    public static final int NUM_STARS = 5;

    private DiscountOffer mDiscountOffer;

    @InjectView(R.id.business_name)
    protected TextView mBusinessName;

    @InjectView(R.id.address)
    protected TextView mAddressTxt;

    @InjectView(R.id.primary_category)
    protected TextView mPrimaryCategory;

    @InjectView(R.id.subcategory)
    protected TextView mSubcategory;

    @InjectView(R.id.subcategory_container)
    protected View mSubcategoryContainer;

    @InjectView(R.id.offer)
    protected TextView mOfferTxt;

    @InjectView(R.id.extra_info_txt)
    protected TextView mExtraInfoTxt;

    @InjectView(R.id.call_button)
    protected View mCallButton;

    @InjectView(R.id.call_button_txt)
    protected TextView mCallButtonTxt;

    @InjectView(R.id.website_button)
    protected View mWebsiteButton;

    @InjectView(R.id.map_img)
    protected ImageView mMapImg;

    @InjectView(R.id.map_view_overlay)
    protected View mMapOverlay;

    @InjectView(R.id.header_bg)
    protected ImageView mHeaderBg;

    @InjectView(R.id.map_btn)
    protected View mMapBtn;

    @InjectView(R.id.used_label)
    protected View mUsedLabel;

    @InjectView(R.id.rating_bar)
    protected RatingBar mButtonRatingBar;

    // Icons
    @InjectView(R.id.icon_parking)
    protected View mIconParking;
    @InjectView(R.id.icon_disabled)
    protected View mIconDisabled;
    @InjectView(R.id.icon_new_zealand)
    protected View mIconNewZealand;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.discount_offer);

        if (getIntent() != null && getIntent().getExtras() != null) {
            mDiscountOffer = (DiscountOffer) getIntent().getExtras().getSerializable(Constants.Extra.DISCOUNT_OFFER_ITEM);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle(R.string.discount_details_title);

        // Bind views
        mBusinessName.setText(mDiscountOffer.getBusinessName());
        hideIfEmpty(mBusinessName);
        mAddressTxt.setText(mDiscountOffer.getAddress());
        hideIfEmpty(mAddressTxt);
        mOfferTxt.setText(mDiscountOffer.getDiscountOffer());
        hideIfEmpty(mOfferTxt);
//        mExtraInfoTxt.setText(mDiscountOffer.getExtraText());
//        mExtraInfoTxt.setText(mDiscountOffer.toString()); // TODO - Debugging only
        hideIfEmpty(mExtraInfoTxt);
        mPrimaryCategory.setText(mDiscountOffer.getPrimaryCategory());
        hideIfEmpty(mPrimaryCategory);
        mSubcategory.setText(mDiscountOffer.getSubCategory());
        hideIfEmpty(mSubcategory, mSubcategoryContainer);

        mIconParking.setVisibility(mDiscountOffer.hasParking() ? View.VISIBLE : View.GONE);
        mIconDisabled.setVisibility(mDiscountOffer.hasDisabled() ? View.VISIBLE : View.GONE);
        mIconNewZealand.setVisibility(mDiscountOffer.hasNewZealand() ? View.VISIBLE : View.GONE);


        // Phone number
        if (mDiscountOffer.hasPhoneNumber()) {
            mCallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCallClick();
                }
            });
        } else {
            mCallButton.setEnabled(false);
        }

        // Website
        if (mDiscountOffer.hasWebsite()) {
            mWebsiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onWebsiteClick();
                }
            });
        } else {
            mWebsiteButton.setEnabled(false);
        }

        // Map
        mMapOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapClick();
            }
        });
        mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapClick();
            }
        });
        LatLng mapLocation = mDiscountOffer.getLatLng();
        if (mapLocation == null) {
            mapLocation = DEFAULT_LOCATION;
        }
        Picasso.with(this)
                .load(getStaticMapUri(mapLocation))
//                .into(mMapImg);
                .into(mHeaderBg);
    }

    private void hideIfEmpty(@Nullable TextView textView, View... visibilitySlaves) {
        if (textView != null) {
            CharSequence text = textView.getText();
            int visibility = text != null && text.length() > 0 ? View.VISIBLE : View.GONE;
            textView.setVisibility(visibility);
            for (View v : visibilitySlaves) {
                v.setVisibility(visibility);
            }
        }
    }

    private static Uri getStaticMapUri(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        return Uri.parse("http://maps.google.com/maps/api/staticmap?center=" +
                latitude + "," + longitude + "&zoom=13&size=600x300&sensor=false");
    }

    private void onMapClick() {
        LatLng latLng = mDiscountOffer.getLatLng();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        String uri = String.format("https://maps.google.com/maps?f=d&daddr=%f,%f", latitude, longitude);
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }


    private void onWebsiteClick() {
        String uriString = mDiscountOffer.getNormalisedWebsite();
        Ln.d("Pressed website: {}", uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriString));
        startActivity(intent);
    }


    protected void onCallClick() {
        String uriString = "tel:" + mDiscountOffer.getPhoneNumber();
        Ln.d("Pressed call: {}", uriString);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uriString));
        startActivity(intent);
    }

    @OnClick(R.id.used_button)
    protected void onUsedClick() {
        Ln.d("Used pressed");
        DialogFragment dialogFragment = new RatingDialog();
        dialogFragment.show(getSupportFragmentManager(), TAG_RATING_DIALOG);

    }

    public class RatingDialog extends DialogFragment {

        private RatingBar mRatingBar;

        public RatingDialog() {
            // Empty constructor required for DialogFragment
        }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.dialog_rate, container);
//            mRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
//            return view;
//        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_rate, null);
            mRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            mRatingBar.setNumStars(NUM_STARS);
            builder.setView(view);

            builder.setTitle("Would you like to rate this discount?");
            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Ln.d("ok");
                            mUsedLabel.setVisibility(View.GONE);
                            mButtonRatingBar.setVisibility(View.VISIBLE);
                            mButtonRatingBar.setRating(mRatingBar.getRating());

                            // TODO - save rating to API
                        }
                    }
            );
            builder.setNegativeButton("No thanks",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Ln.d("no thanks");
                            dialog.cancel();
                        }
                    }
            );


            return builder.create();
        }
    }
}
