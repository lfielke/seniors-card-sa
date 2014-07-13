package com.example.yourservices.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.yourservices.R;

import butterknife.OnClick;

public class AboutSeniorsCardActivity extends BootstrapActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_seniors_card);
    }

    @OnClick(R.id.apply_btn)
    protected void onApplyClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.about_card_apply_uri)));
        startActivity(intent);
    }
}
