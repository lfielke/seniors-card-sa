package com.example.yourservices.core;


import retrofit.RequestInterceptor;

import static com.example.yourservices.core.Constants.Http.HEADER_PARSE_APP_ID;
import static com.example.yourservices.core.Constants.Http.HEADER_PARSE_REST_API_KEY;

public class RestAdapterRequestInterceptor implements RequestInterceptor {

    private UserAgentProvider userAgentProvider;
    private final String mParseAppId;
    private final String mParseClientKey;

    public RestAdapterRequestInterceptor(UserAgentProvider userAgentProvider,
                                         String parseAppId,
                                         String parseClientKey) {
        this.userAgentProvider = userAgentProvider;
        mParseAppId = parseAppId;
        mParseClientKey = parseClientKey;
    }

    @Override
    public void intercept(RequestFacade request) {

        // Add header to set content type of JSON
        request.addHeader("Content-Type", "application/json");

        // Add auth info for PARSE, normally this is where you'd add your auth info for this request (if needed).
        request.addHeader(HEADER_PARSE_REST_API_KEY, mParseClientKey);
        request.addHeader(HEADER_PARSE_APP_ID, mParseAppId);

        // Add the user agent to the request.
        request.addHeader("User-Agent", userAgentProvider.get());
    }
}
