package com.example.yourservices.core;


import com.example.yourservices.model.DiscountOffer;

import java.util.List;

/**
 * Parse returns an array in a "results" object.
 */
public class DiscountOfferWrapper {
    private List<DiscountOffer> results;

    public List<DiscountOffer> getResults() {
        return results;
    }
}
