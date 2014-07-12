package com.example.yourservices.core;

import retrofit.http.GET;


/**
 * Interface for defining the discount service to communicate with Parse.com
 */
public interface DiscountService {

    @GET(Constants.Http.URL_DISCOUNTS_FRAG)
    DiscountOfferWrapper getDiscountOffers();

}
