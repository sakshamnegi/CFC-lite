package com.yefindia.intern.charityfirstcenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yefindia.intern.charityfirstcenter.Interface.ItemClickListener;
import com.yefindia.intern.charityfirstcenter.Model.Product;
import com.yefindia.intern.charityfirstcenter.ViewHolder.ProductViewHolder;

public class ProductList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference productDatabase;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product,ProductViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        productDatabase = database.getReference("Products");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        //Get categoryId from intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null)
        {
            loadListProduct(categoryId);
        }




    }

    private void loadListProduct(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class,
                R.layout.product_item,
                ProductViewHolder.class,
                productDatabase.orderByChild("categoryId").equalTo(categoryId)) //To select from products where id = category id
        {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.productName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.productImage);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //Start new Activity
                        Intent productDetail = new Intent(ProductList.this,ProductDetails.class);
                        productDetail.putExtra("ProductId",adapter.getRef(position).getKey());  //Send ProductId to new activity
                        startActivity(productDetail);
                    }
                });


            }
        };
        //Set adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

}
