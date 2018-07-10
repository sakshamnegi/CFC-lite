package com.yefindia.intern.charityfirstcenter;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SellActivity extends AppCompatActivity {
    FloatingActionButton emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        emailButton = (FloatingActionButton) findViewById(R.id.email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","yefindia@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sell Product on Charity First Center App");
                if(emailIntent.resolveActivity(getPackageManager())!=null)
                    startActivity(emailIntent);
                else
                    Toast.makeText(SellActivity.this,"No email application found. Try sending manually",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
