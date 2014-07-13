package com.example.yourservices.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A discount offer.
 */
@Accessors(prefix = "m")
public class DiscountOffer implements Serializable, Comparable {

    @NotNull
    @SerializedName("businessName")
    private String mBusinessName;

    @NotNull
    @Getter
    @SerializedName("Offer")
    private String mDiscountOffer;

    @Nullable
    @Getter
    @SerializedName("Phone1")
    private String mPhoneNumber;

    @Nullable
    @Getter
    @SerializedName("website")
    private String mWebsite;

    @Nullable
    @Getter
    @SerializedName("exclusionsToOffer")
    private String mExclusionsToOffer;

    @Nullable
    @Getter
    @SerializedName("primaryCategory")
    private String mPrimaryCategory;

    @Nullable
    @Getter
    @SerializedName("subCategory")
    private String mSubCategory;

    @Nullable
    @Getter
    @SerializedName("Region")
    private String mRegion;

    @Nullable
    @Getter
    @SerializedName("Location")
    private String mAddress;

    @Nullable
    @SerializedName("Symbols")
    private String mSymbols; //P=Parking, DA=disabled access, NZ=available to N.Z.,

    @Nullable
    @SerializedName("scdLine1")
    private String mExtra1;

    @Nullable
    @SerializedName("scdLine2")
    private String mExtra2;

    @Nullable
    @SerializedName("scdLine3")
    private String mExtra3;

    @Nullable
    @SerializedName("scdLine4")
    private String mExtra4;

    @Nullable
    @SerializedName("lat")
    private String mLatitude;

    @Nullable
    @SerializedName("long")
    private String mLongitude;


    public DiscountOffer() {

    }

    public boolean hasPhoneNumber() {
        return mPhoneNumber != null && mPhoneNumber.length() > 7;
    }

    public boolean hasWebsite() {
        return mWebsite != null && mWebsite.length() > 4;
    }

    public String getBusinessName() {
        String[] words = mBusinessName.split(" ");
        StringBuilder builder = new StringBuilder();

        // Title case hack
        for (String word : words) {
            if (word.length() == 0) {
                continue;
            }
            if (builder.length() != 0) {
                builder.append(" ");
            }

            builder.append(Character.toTitleCase(word.charAt(0)));
            if (word.length() > 1) {
                builder.append(word.substring(1).toLowerCase());
            }
        }

        return builder.toString();
    }

    /**
     * Extra text in scdLine* fields (may be blank)
     */
    public String getExtraText() {
        StringBuilder builder = new StringBuilder();
        String[] lines = new String[]{mExtra1, mExtra2, mExtra3, mExtra4};
        for (String line : lines) {
            if (line == null || line.length() == 0 || line.trim().equalsIgnoreCase("n/a")) {
                continue;
            }

            if (builder.length() != 0) {
                builder.append("\n");
            }
            builder.append(line);
        }
        return builder.toString();
    }

    @NotNull
    public String getNormalisedWebsite() {
        if (mWebsite == null) {
            return "";
        }

        if (!mWebsite.startsWith("http")) {
            return "http://" + mWebsite;
        } else {
            return mWebsite;
        }
    }

    public boolean hasParking() {
        return mSymbols != null && mSymbols.contains("P");
    }

    public boolean hasDisabled() {
        return mSymbols != null && mSymbols.contains("DA");
    }

    public boolean hasNewZealand() {
        return mSymbols != null && mSymbols.contains("NZ");
    }

    @Override
    public String toString() {
        return "DiscountOffer{" +
                "mBusinessName='" + mBusinessName + '\'' +
                ", mDiscountOffer='" + mDiscountOffer + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mWebsite='" + mWebsite + '\'' +
                ", mExclusionsToOffer='" + mExclusionsToOffer + '\'' +
                ", mPrimaryCategory='" + mPrimaryCategory + '\'' +
                ", mSubCategory='" + mSubCategory + '\'' +
                ", mRegion='" + mRegion + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mSymbols='" + mSymbols + '\'' +
                ", mExtra1='" + mExtra1 + '\'' +
                ", mExtra2='" + mExtra2 + '\'' +
                ", mExtra3='" + mExtra3 + '\'' +
                ", mExtra4='" + mExtra4 + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                '}';
    }

    @Nullable
    public LatLng getLatLng() {
        if (mLatitude == null || mLongitude == null) {
            return null;
        }
        double lat;
        double lng;
        try {
            lat = Double.parseDouble(mLatitude);
            lng = Double.parseDouble(mLongitude);
        } catch (NumberFormatException e) {
            return null;
        }
        return new LatLng(lat, lng);
    }

    @Override
    public int compareTo(Object another) {
        if (another instanceof DiscountOffer) {
            DiscountOffer other = (DiscountOffer) another;
            return mBusinessName.compareToIgnoreCase(other.mBusinessName);
        } else {
            return 0;
        }
    }
}
