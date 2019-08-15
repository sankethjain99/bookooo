package com.example.bookooo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.bookooo.Database.Database;

import com.example.bookooo.ViewHolder.OrderViewHolder;
import com.example.bookooo.common.common;
import com.example.bookooo.interfac.itemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent()==null) {

            loadOrders(common.currentUser.getPhone());
        }
        else
            loadOrders(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText((model.getEmailAddress()));
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.setItemClickListener(new itemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (!isLongClick)
                        {

                        }
                        else
                        {

                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadOrders(common.currentUser.getPhone());
    }


}
