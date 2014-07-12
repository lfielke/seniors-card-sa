package com.example.yourservices.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.yourservices.R;
import com.example.yourservices.core.Constants;
import com.example.yourservices.model.DiscountOffer;
import com.example.yourservices.util.Ln;

import butterknife.InjectView;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class DiscountOfferActivity extends BootstrapActivity {

    private DiscountOffer mDiscountOffer;

    @InjectView(R.id.business_name)
    protected TextView mBusinessName;

    @InjectView(R.id.offer)
    protected TextView mOffer;

    @InjectView(R.id.call_button)
    protected View mCallButton;

    @InjectView(R.id.call_button_txt)
    protected TextView mCallButtonTxt;

    @InjectView(R.id.website_button)
    protected View mWebsiteButton;

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
        mOffer.setText(mDiscountOffer.getDiscountOffer());

        // Phone number
        if (mDiscountOffer.hasPhoneNumber()) {
//            mCallButtonTxt.setText(mDiscountOffer.getPhoneNumber());
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
}
