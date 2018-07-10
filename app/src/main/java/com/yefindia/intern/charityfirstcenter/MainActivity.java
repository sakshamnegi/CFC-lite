package com.yefindia.intern.charityfirstcenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yefindia.intern.charityfirstcenter.Model.UserData;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
    private final int REQUEST_LOGIN = 1000;
    CardView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // UserData is already signed in.
            // Start Logged in activity and display toast

            startActivity(new Intent(this,LoggedIn.class));
            finish();

            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }

        //Buy sell buttons
        btnLogin = (CardView) findViewById(R.id.button_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
                Log.d("MainAct ","Buy button intent done");
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN)
        {
            handleSignInResponse(resultCode, data);
            return;
        }
        Toast.makeText(this,"Unexpected onActivityResult response code",Toast.LENGTH_SHORT).show();

    }

    private void handleSignInResponse(int resultCode, Intent data){
        if(resultCode == RESULT_OK){

            addUserToDatabase();
            startActivity(new Intent(this,LoggedIn.class)
                    .putExtra("phone", IdpResponse.fromResultIntent(data)));
            finish();
            return;
        }
        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this,"SignIn Cancelled / Connection error!",Toast.LENGTH_SHORT).show();
        }
    }

    private void userLogin()
    {
        // Start sign in/sign up activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_LOGIN);

    }


    private void addUserToDatabase(){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tableUser = database.getReference("Users");
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            @NonNull
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.child(userName).exists() && dataSnapshot.child(userName).child("email").getValue().toString().equalsIgnoreCase(userEmail))
                {
                    //exists in database
                    //just display welcome message

                    Toast.makeText(MainActivity.this, "Welcome " + userName, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Not in database
                    //get userId by counting existing number of users
                    String userId;
                    Long newUserPosition = dataSnapshot.getChildrenCount() + 1;
                    if(newUserPosition<10)
                        userId = "0" + String.valueOf(newUserPosition);     //Because our database has IDs as 01, 05, etc
                    else
                        userId = String.valueOf(newUserPosition);

                    UserData newUser = new UserData(userId,userEmail);
                    tableUser.child(userName).setValue(newUser);
                    Toast.makeText(MainActivity.this, "Welcome " + userName , Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
