package com.yefindia.intern.charityfirstcenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PurposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
