package com.yefindia.intern.charityfirstcenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yefindia.intern.charityfirstcenter.Model.Product;
import com.yefindia.intern.charityfirstcenter.Model.Seller;

public class ProductDetails extends AppCompatActivity {

    TextView productName, productPrice,productDescription;
    TextView sellerOrganization, sellerAddress, sellerPhone;
    ImageView productImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnChat;

    String productId = "";

    FirebaseDatabase database;
    DatabaseReference productDatabase;
    DatabaseReference sellerDatabase;
    DatabaseReference usersDatabase;
    FirebaseUser currentUser;

    //SellerID on displayed product for chat
    String mSellerId = "";

    //current user's userId for chat
    String mCurrentUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        productDatabase = database.getReference("Products");
        sellerDatabase = database.getReference("Sellers");
        usersDatabase = database.getReference("Users");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //get current user id
        usersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUser.getDisplayName()).child("email").getValue().toString().equalsIgnoreCase(currentUser.getEmail()))
                {
                    //user spotted in database
                    //get userId
                    mCurrentUserId = dataSnapshot.child(currentUser.getDisplayName()).child("userId").getValue().toString();
                    Toast.makeText(ProductDetails.this, "UserId for chat " + mCurrentUserId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Init views

        productDescription = (TextView) findViewById(R.id.product_description);
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        productImage = (ImageView) findViewById(R.id.img_product);

        sellerOrganization = (TextView) findViewById(R.id.seller_organization);
        sellerAddress = (TextView) findViewById(R.id.seller_address);
        sellerPhone = (TextView) findViewById(R.id.seller_phone);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        btnChat = (FloatingActionButton) findViewById(R.id.chat_button);
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSellerId.equals(mCurrentUserId))
                {
                    //current user is the seller of this product
                    //mock them for forgetting or thinking they're too smart
                    Toast.makeText(ProductDetails.this,"You are the seller for this product! DUH...",Toast.LENGTH_LONG).show();
                    return;
                }

                //Send seller id and current user id to chat activity
                Intent chatIntent = new Intent(ProductDetails.this,Chat.class);
                //Category id is set as key in firebase database
                chatIntent.putExtra("ChatSellerId",mSellerId).putExtra("ChatUserId",mCurrentUserId);
                startActivity(chatIntent);

            }
        });

        //Get Product Id from intent
        if(getIntent() != null)
            productId = getIntent().getStringExtra("ProductId");
        if(!productId.isEmpty() )
        {
            getDetailProduct(productId);
        }

    }

    private void getDetailProduct(final String productId) {
        productDatabase.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);

                //set product details on layout views
                Picasso.with(getBaseContext()).load(product.getImage())
                        .into(productImage);

                collapsingToolbarLayout.setTitle(product.getName());

                productPrice.setText(product.getPrice());
                productName.setText(product.getName());
                productDescription.setText(product.getDescription());

                //Set the sellerID on product to function for chat button
                mSellerId = product.getSellerId();
                Toast.makeText(ProductDetails.this, "SellerId for chat " + mSellerId, Toast.LENGTH_SHORT).show();

                sellerDatabase.child(mSellerId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Seller seller = dataSnapshot.getValue(Seller.class);

                        sellerOrganization.setText(seller.getOrganization());
                        sellerAddress.setText(seller.getAddress());
                        sellerPhone.setText(seller.getPhone());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
