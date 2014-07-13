package com.example.yourservices.ui;

import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.yourservices.BootstrapServiceProvider;
import com.example.yourservices.R;
import com.example.yourservices.core.BootstrapService;
import com.example.yourservices.events.NavItemSelectedEvent;
import com.example.yourservices.util.Ln;
import com.example.yourservices.util.SafeAsyncTask;
import com.example.yourservices.util.UIUtils;
import com.google.android.gms.location.LocationRequest;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Initial activity for the application.
 * <p/>
 * If you need to remove the authentication from the application please see
 * {@link com.example.yourservices.authenticator.ApiKeyProvider#getAuthKey(android.app.Activity)}
 */
public class MainActivity extends BootstrapFragmentActivity {

    @Inject
    protected BootstrapServiceProvider serviceProvider;

    private boolean userHasAuthenticated = false;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private NavigationDrawerFragment navigationDrawerFragment;

    // ReactiveLocation
    @Inject
    protected ReactiveLocationProvider locationProvider;

    private Observable<Location> lastKnownLocationObservable;
    private Observable<Location> locationUpdatesObservable;
    private Subscription lastKnownLocationSubscription;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        if (isTablet()) {
            setContentView(R.layout.main_activity_tablet);
        } else {
            setContentView(R.layout.main_activity);
        }

        // View injection with Butterknife
        ButterKnife.inject(this);

        // Set up navigation drawer
        title = drawerTitle = getTitle();

        if (!isTablet()) {
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerToggle = new ActionBarDrawerToggle(
                    this,                    /* Host activity */
                    drawerLayout,           /* DrawerLayout object */
                    R.drawable.ic_drawer,    /* nav drawer icon to replace 'Up' caret */
                    R.string.navigation_drawer_open,    /* "open drawer" description */
                    R.string.navigation_drawer_close) { /* "close drawer" description */

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(title);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(drawerTitle);
                    supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            // Set the drawer toggle as the DrawerListener
            drawerLayout.setDrawerListener(drawerToggle);

            navigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

            // Set up the drawer.
            navigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        checkAuth();

        setupLocation();

    }

    private void setupLocation() {
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();

        locationUpdatesObservable = locationProvider.getUpdatedLocation(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setNumUpdates(5)
                        .setInterval(100)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastKnownLocationSubscription = lastKnownLocationObservable
                .map(new LocationToStringFunc())
                .subscribe(new DisplayTextOnViewAction());
    }

    @Override
    protected void onStop() {
        super.onStop();
        lastKnownLocationSubscription.unsubscribe();
    }

    private static class LocationToStringFunc implements Func1<Location, String> {
        @Override
        public String call(Location location) {
            return location.getLatitude() + " " + location.getLongitude() + " (" + location.getAccuracy() + ")";
        }
    }

    private class DisplayTextOnViewAction implements Action1<String> {
//        private final TextView target;

//        private DisplayTextOnViewAction(TextView target) {
//            this.target = target;
//        }

        @Override
        public void call(String s) {
//            target.setText(s);
            Ln.d("Location update: %s", s);
//            MainActivity.this.setTitle(s);
        }
    }

    private boolean isTablet() {
        return UIUtils.isTablet(this);
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (!isTablet()) {
            // Sync the toggle state after onRestoreInstanceState has occurred.
            drawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isTablet()) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }


    private void initScreen() {
        if (userHasAuthenticated) {

            Ln.d("Foo");
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new CarouselFragment())
//                    .replace(R.id.container, new SupportMapFragment())
                    .commit();
        }

    }

    private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                final BootstrapService svc = serviceProvider.getService(MainActivity.this);
                return svc != null;
            }

            @Override
            protected void onException(final Exception e) throws RuntimeException {
                super.onException(e);
                if (e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(final Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                //menuDrawer.toggleMenu();
                return true;
//            case R.id.timer:
//                navigateToTimer();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }

    @Subscribe
    public void onNavigationItemSelected(NavItemSelectedEvent event) {

        Ln.d("Selected: %1$s", event.getItemPosition());

        switch (event.getItemPosition()) {
            case 0:
                // Home
                // do nothing as we're already on the home screen.
                break;
            case 1:
                // Timer
                navigateToTimer();
                break;
        }
    }
}
