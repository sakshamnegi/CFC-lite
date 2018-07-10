package com.yefindia.intern.charityfirstcenter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yefindia.intern.charityfirstcenter.Model.Message;

public class Chat extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference messagesDatabase,sellersDatabase;
    FirebaseUser currentUser;

    FirebaseListAdapter<Message> adapter;
    ListView listViewOfMessages;

    ImageButton sendMessageButton;

    String senderUserId = "";
    String receiverUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //messages listview
        listViewOfMessages = (ListView)findViewById(R.id.list_of_messages);

        //Init firebase
        database = FirebaseDatabase.getInstance();
        messagesDatabase = database.getReference("Messages");
        sellersDatabase = database.getReference("Sellers");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Get seller and user IDs for chat from ProductDetails Activity
        if(getIntent() != null) {
            senderUserId = getIntent().getStringExtra("ChatUserId");
            receiverUserId = getIntent().getStringExtra("ChatSellerId");

        }

        //TODO set action bar title

        //display messages between particular two users
        // with senderUserId and receiverUserId
        displayChatMessages();

        //On click listener for send message image button
         sendMessageButton = (ImageButton)findViewById(R.id.sendMessageImagebutton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText messageText = (EditText) findViewById(R.id.messageEditText);
                String messageString = messageText.getText().toString().trim();

                if (messageString.equals("")) {
                    Toast.makeText(Chat.this, "You must enter a message", Toast.LENGTH_SHORT).show();
                } else {
                    //message is entered, send
                    //push a new instance
                    // of Message to the Messages database
                    final Message newMessage = new Message(messageString,senderUserId,receiverUserId);
                    messagesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String newMessageId;
                           long newMessagePosition = dataSnapshot.getChildrenCount() + 1;

                           if (newMessagePosition < 10)
                                newMessageId = "0" + String.valueOf(newMessagePosition);

                           else
                                newMessageId = String.valueOf(newMessagePosition);

                            messagesDatabase.child(newMessageId).setValue(newMessage);

                            messageText.setText("");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
    }

    private void displayChatMessages() {

        adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.individual_message,messagesDatabase) {
            @Override
            protected void populateView(View v, Message model, int position) {

                //Get reference to the views of individual_message.xml
                TextView msgText = (TextView) v.findViewById(R.id.message_text);
                TextView msgUser = (TextView) v.findViewById(R.id.message_user);
                TextView msgTime = (TextView) v.findViewById(R.id.message_time);

                //current two users have messages
                //Set the text of these views
                if((model.getSenderUserId().equals(senderUserId) && model.getReceiverUserId().equals(receiverUserId)) ||
                        //or other way round
                model.getSenderUserId().equals(receiverUserId) && model.getReceiverUserId().equals(senderUserId))
                {
                    msgText.setText(model.getMessageText());
                    msgUser.setText(currentUser.getDisplayName());

                    //Format the date time befor showing it
                    msgTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }

                else if(model.getSenderUserId().equals(receiverUserId) && model.getReceiverUserId().equals(senderUserId)){
                    msgText.setText(model.getMessageText());
                    String sellerString = sellersDatabase.child(senderUserId).child("organization").toString();
                    msgUser.setText(sellerString);

                    //Format the date time befor showing it
                    msgTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }
                else{

                }

            }
        };

        listViewOfMessages.setAdapter(adapter);


    }
}

