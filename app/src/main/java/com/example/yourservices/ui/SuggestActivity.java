package com.example.yourservices.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.yourservices.R;
import com.example.yourservices.util.Ln;

import butterknife.InjectView;
import butterknife.OnClick;

public class SuggestActivity extends BootstrapActivity {

    @InjectView(R.id.business_name)
    protected EditText mBusinessName;

    @InjectView(R.id.address)
    protected EditText mAddress;

    @InjectView(R.id.services)
    protected EditText mServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.suggest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.submit_btn)
    protected void onSubmitClick(View v) {
        Ln.d("Submit pressed");

        // TODO - save suggestion to Parse
        finish();
    }
}
