package com.example.yourservices.model;

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
    private String mBusinessName;
    @NotNull
    private String mDiscountOffer;
    @Nullable
    private String mPhoneNumber;
    @Nullable
    private String mWebsite;

    public DiscountOffer(@NotNull String mBusinessName, @NotNull String mDiscountOffer,
                         @Nullable String mPhoneNumber, @Nullable String mWebsite) {
        this.mBusinessName = mBusinessName;
        this.mDiscountOffer = mDiscountOffer;
        this.mPhoneNumber = mPhoneNumber;
        this.mWebsite = mWebsite;
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
}
