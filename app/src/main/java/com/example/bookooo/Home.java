package com.example.bookooo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.bookooo.Database.Database;
import com.example.bookooo.ViewHolder.MenuViewHolder;
import com.example.bookooo.common.common;
import com.example.bookooo.interfac.itemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    CounterFab fab;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    SwipeRefreshLayout swipeRefreshLayout;


    HashMap<String,String> image_list;
    SliderLayout mSlider;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (common.isConnectedToInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(getBaseContext(), "Check your Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (common.isConnectedToInternet(getBaseContext()))
                    loadMenu();
                else {
                    Toast.makeText(getBaseContext(), "Check your Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });


        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        Paper.init(this);

        fab =(CounterFab)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this,Cart.class);
                startActivity(cartIntent);
            }
        });

        fab.setCount(new Database(this).getCountCart());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFulName);
        txtFullName.setText(common.currentUser.getName());
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        //layoutManager = new LinearLayoutManager(this);
        //recycler_menu.setLayoutManager(layoutManager);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        updateToken(FirebaseInstanceId.getInstance().getToken());


        setupSlider();



    }

    private void setupSlider() {
        mSlider = (SliderLayout)findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banners = database.getReference("Banner");
        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Banner banner = postSnapshot.getValue(Banner.class);
                    image_list.put(banner.getName()+"@@@"+banner.getId(),banner.getImage());
                }
                for (String key:image_list.keySet()){
                    String[] keySplit = key.split("@@@");
                    String nameOfBook = keySplit[0];
                    String idOfBook = keySplit[1];


                    final TextSliderView textSiderView = new TextSliderView(getBaseContext());
                    textSiderView
                            .description(nameOfBook)
                            .image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    
                                }
                            });
                    textSiderView.bundle(new Bundle());
                    textSiderView.getBundle().putString("BookId",idOfBook);
                    mSlider.addSlider(textSiderView);

                    banners.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());

    }

    private void updateToken(String token) {


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,false);
        tokens.child(common.currentUser.getPhone()).setValue(data);
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {


            @Override

            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Category clickitem = model;
                viewHolder.setItemClickListener(    new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent bookList = new Intent(Home.this,BookList.class);
                        bookList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(bookList);

                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSlider.stopAutoCycle();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.refresh) {
            loadMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_sell) {
            Intent sell = new Intent(Home.this,Buy.class);

            startActivity(sell);

        } else if (id == R.id.nav_donate) {
            Intent donate = new Intent(Home.this,Donate.class);
            startActivity(donate);

        } else if (id == R.id.nav_buy) {
            Intent buy = new Intent(Home.this,Home.class);
            startActivity(buy);
        }

         else if (id == R.id.nav_logout) {

            Paper.book().destroy();
            Intent signIn = new Intent(Home.this,SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this,OrderStatus.class);
            startActivity(orderIntent);
        }else if(id==R.id.nav_cart){
            Intent cartIntent = new Intent(Home.this,Cart.class);
            startActivity(cartIntent);
        }else if(id==R.id.nav_about){
            Intent about = new Intent(Home.this,About.class);
            startActivity(about);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
