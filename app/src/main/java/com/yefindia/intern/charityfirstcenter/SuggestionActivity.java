package com.yefindia.intern.charityfirstcenter;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yefindia.intern.charityfirstcenter.Model.Suggestion;

public class SuggestionActivity extends AppCompatActivity {

    EditText edtSuggestion;
    DatabaseReference suggestionDatabase;
    CardView submitSuggestion;
    String suggestionString;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        //init firebase
        suggestionDatabase = FirebaseDatabase.getInstance().getReference("Suggestions");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //init views
        edtSuggestion = (EditText) findViewById(R.id.edt_suggestion);
        submitSuggestion = (CardView) findViewById(R.id.submit_suggestion);

        submitSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestionString = edtSuggestion.getText().toString().trim();
                if(suggestionString.equalsIgnoreCase("")){
                    edtSuggestion.setError("Cannot be empty");
                    return;
                }
                addSuggestionToDatabase(suggestionString);
            }
        });

    }

    private void addSuggestionToDatabase(String suggestionString) {
        final Suggestion newSuggestion = new Suggestion(suggestionString, currentUser.getDisplayName(),currentUser.getEmail());
        suggestionDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long suggestionPosition = dataSnapshot.getChildrenCount() + 1;
                String suggestionId;
                if(suggestionPosition<10)
                    suggestionId = "0" + String.valueOf(suggestionPosition);
                else
                    suggestionId = String.valueOf(suggestionPosition);
                suggestionDatabase.child(suggestionId).setValue(newSuggestion);
                Toast.makeText(SuggestionActivity.this,"Thank you. Your feedback is valuable to us.",Toast.LENGTH_SHORT).show();
                edtSuggestion.setText("");
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
