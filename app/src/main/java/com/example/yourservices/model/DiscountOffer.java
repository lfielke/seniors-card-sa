package com.example.yourservices.model;

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
@Getter
public class DiscountOffer implements Serializable {

    @NotNull
    @SerializedName("businessName")
    private String mBusinessName;

    @NotNull
    @SerializedName("detailsOfOffer")
    private String mDiscountOffer;

    @Nullable
    @SerializedName("Phone1")
    private String mPhoneNumber;

    @Nullable
    @SerializedName("website")
    private String mWebsite;

    @Nullable
    @SerializedName("exclusionsToOffer")
    private String mExclusionsToOffer;

    @Nullable
    @SerializedName("primaryCategory")
    private String mPrimaryCategory;

    @Nullable
    @SerializedName("subCategory")
    private String mSubCategory;

    @Nullable
    @SerializedName("symbols")
    private String mSymbols; //P=Parking, DA=disabled access, NZ=available to N.Z.,


    public DiscountOffer() {

    }

    public boolean hasPhoneNumber() {
        return mPhoneNumber != null && mPhoneNumber.length() > 7;
    }

    public boolean hasWebsite() {
        return mWebsite != null && mWebsite.length() > 4;
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
}
