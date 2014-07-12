package com.example.yourservices.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.yourservices.Injector;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.Views;

/**
 * Base class for all Bootstrap Activities that need fragments.
 */
public class BootstrapFragmentActivity extends ActionBarActivity {

    @Inject
    protected Bus eventBus;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public void setContentView(final int layoutResId) {
        super.setContentView(layoutResId);

        Views.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    // Google Play Services stuff
//
//    private final static int GOOGLE_PLAY_SERVICES_REQUEST_CODE = 9000;
//
//    /**
//     * A DialogFragment that displays the Google Play Services error
//     */
//    public static class ErrorDialogFragment extends DialogFragment {
//        // Global field to contain the error dialog
//        private Dialog mDialog;
//
//        // Default constructor. Sets the dialog field to null
//        public ErrorDialogFragment() {
//            super();
//            mDialog = null;
//        }
//
//        // Set the dialog to display
//        public void setDialog(Dialog dialog) {
//            mDialog = dialog;
//        }
//
//        // Return a Dialog to the DialogFragment.
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            return mDialog;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//
//            case GOOGLE_PLAY_SERVICES_REQUEST_CODE:
//            /*
//             * If the result code is Activity.RESULT_OK, try
//             * to connect again
//             */
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        break;
//                }
//
//            default:
//                super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    private boolean isGooglePlayServicesConnected() {
//        // Check that Google Play services is available
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//
//        // If Google Play services is available
//        if (ConnectionResult.SUCCESS == resultCode) {
//            // In debug mode, log the status
//            Ln.d("Google Play services is available.");
//            // Continue
//            return true;
//            // Google Play services was not available for some reason
//        } else {
//            // Get the error code
//            int errorCode = ConnectionResult.getErrorCode();
//            // Get the error dialog from Google Play services
//            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
//                    errorCode,
//                    this,
//                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
//
//            // If Google Play services can provide an error dialog
//            if (errorDialog != null) {
//                // Create a new DialogFragment for the error dialog
//                ErrorDialogFragment errorFragment =
//                        new ErrorDialogFragment();
//                // Set the dialog in the DialogFragment
//                errorFragment.setDialog(errorDialog);
//                // Show the error dialog in the DialogFragment
//                errorFragment.show(getSupportFragmentManager(),
//                        "Location Updates");
//            }
//        }
//    }
}
