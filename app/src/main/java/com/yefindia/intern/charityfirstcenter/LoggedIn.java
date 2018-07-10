package com.yefindia.intern.charityfirstcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yefindia.intern.charityfirstcenter.Interface.ItemClickListener;
import com.yefindia.intern.charityfirstcenter.Model.Category;
import com.yefindia.intern.charityfirstcenter.ViewHolder.MenuViewHolder;

public class LoggedIn extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference categoryDatabase;
    FirebaseUser currentUser;

    TextView headerUsername, headerEmail;

    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        categoryDatabase = database.getReference("Category");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Username and Email on header
        View headerView = navigationView.getHeaderView(0);
        headerUsername = (TextView) headerView.findViewById(R.id.drawer_username);
        headerUsername.setText(currentUser.getDisplayName());
        headerEmail = (TextView) headerView.findViewById(R.id.drawer_email);
        headerEmail.setText(currentUser.getEmail());

        //Load Category
        recyclerMenu = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        loadMenu();

    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,categoryDatabase) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);

                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //get category id and send to next activity
                        Intent productList = new Intent(LoggedIn.this,ProductList.class);
                        //Category id is set as key in firebase database
                        productList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(productList);

                        Toast.makeText(LoggedIn.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        };
        recyclerMenu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sell) {
            Intent sellIntent = new Intent(LoggedIn.this, SellActivity.class);
            startActivity(sellIntent);

        } else if (id == R.id.nav_purpose) {
            Intent purposeIntent = new Intent(LoggedIn.this,PurposeActivity.class);
            startActivity(purposeIntent);


        } else if (id == R.id.nav_suggestions) {
            Intent suggIntent = new Intent(LoggedIn.this, SuggestionActivity.class);
            startActivity(suggIntent);

        }
        else if (id == R.id.nav_signout) {
            //Sign out user
            userSignOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void userSignOut(){
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(LoggedIn.this,
                                        "Successfully signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();

                                // Close activity
                                finish();
                                startActivity(new Intent(LoggedIn.this,MainActivity.class));
                            }
                        });

        }


    // TODO Change action bar header title
}

