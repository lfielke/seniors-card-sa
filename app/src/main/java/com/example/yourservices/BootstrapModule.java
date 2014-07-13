package com.example.yourservices;

import android.accounts.AccountManager;
import android.content.Context;

import com.example.yourservices.authenticator.ApiKeyProvider;
import com.example.yourservices.authenticator.BootstrapAuthenticatorActivity;
import com.example.yourservices.authenticator.LogoutService;
import com.example.yourservices.core.BootstrapService;
import com.example.yourservices.core.Constants;
import com.example.yourservices.core.PostFromAnyThreadBus;
import com.example.yourservices.core.RestAdapterRequestInterceptor;
import com.example.yourservices.core.RestErrorHandler;
import com.example.yourservices.core.TimerService;
import com.example.yourservices.core.UserAgentProvider;
import com.example.yourservices.ui.BootstrapTimerActivity;
import com.example.yourservices.ui.CheckInsListFragment;
import com.example.yourservices.ui.DiscountListFragment;
import com.example.yourservices.ui.DiscountMapFragment;
import com.example.yourservices.ui.DiscountOfferActivity;
import com.example.yourservices.ui.MainActivity;
import com.example.yourservices.ui.MainMenuActivity;
import com.example.yourservices.ui.NavigationDrawerFragment;
import com.example.yourservices.ui.NewsActivity;
import com.example.yourservices.ui.NewsListFragment;
import com.example.yourservices.ui.SuggestActivity;
import com.example.yourservices.ui.UserActivity;
import com.example.yourservices.ui.UserListFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                MainMenuActivity.class,
                MainActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NavigationDrawerFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                DiscountListFragment.class,
                DiscountOfferActivity.class,
                DiscountMapFragment.class,
                SuggestActivity.class,
                TimerService.class
        }
)
public class BootstrapModule {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new PostFromAnyThreadBus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

    @Provides
    BootstrapService provideBootstrapService(RestAdapter restAdapter) {
        return new BootstrapService(restAdapter);
    }

    @Provides
    BootstrapServiceProvider provideBootstrapServiceProvider(RestAdapter restAdapter, ApiKeyProvider apiKeyProvider) {
        return new BootstrapServiceProvider(restAdapter, apiKeyProvider);
    }

    @Provides
    ApiKeyProvider provideApiKeyProvider(AccountManager accountManager) {
        return new ApiKeyProvider(accountManager);
    }

    @Provides
    Gson provideGson() {
        /**
         * GSON instance to use for all request  with date format set up for proper parsing.
         * <p/>
         * You can also configure GSON with different naming policies for your API.
         * Maybe your API is Rails API and all json values are lower case with an underscore,
         * like this "first_name" instead of "firstName".
         * You can configure GSON as such below.
         * <p/>
         *
         * public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd")
         *         .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
         */
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    }

    @Provides
    RestErrorHandler provideRestErrorHandler(Bus bus) {
        return new RestErrorHandler(bus);
    }

    @Provides
    RestAdapterRequestInterceptor provideRestAdapterRequestInterceptor(UserAgentProvider userAgentProvider,
                                                                       Context context) {
        String parseAppId = context.getString(R.string.api_key_parse_app_id);
        String parseClientKey = context.getString(R.string.api_key_parse_rest_key);

        return new RestAdapterRequestInterceptor(userAgentProvider, parseAppId, parseClientKey);
    }

    @Provides
    RestAdapter provideRestAdapter(RestErrorHandler restErrorHandler, RestAdapterRequestInterceptor restRequestInterceptor, Gson gson) {
        return new RestAdapter.Builder()
                .setEndpoint(Constants.Http.URL_BASE)
                .setErrorHandler(restErrorHandler)
                .setRequestInterceptor(restRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    // Location
    @Provides
    ReactiveLocationProvider provideReactiveLocationProvider(Context context) {
        return new ReactiveLocationProvider(context);
    }

}
