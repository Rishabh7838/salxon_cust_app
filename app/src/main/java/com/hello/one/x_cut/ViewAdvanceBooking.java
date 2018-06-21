package com.hello.one.x_cut;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.ViewHolder.AdvanceBookingViewHolder;
//import com.hello.one.x_cut.ViewHolder.styleAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

public class ViewAdvanceBooking extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference viewBooking;
    private FirebaseRecyclerAdapter<com.hello.one.x_cut.Model.Request, AdvanceBookingViewHolder> adapter;
    private RecyclerView recyclerViewBooking;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advance_booking);

        database = FirebaseDatabase.getInstance();
        viewBooking = database.getReference("Booking");

        recyclerViewBooking = (RecyclerView) findViewById(R.id.viewBooking);
        recyclerViewBooking.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewBooking.setLayoutManager(layoutManager);


        loadBookings("7838257301");

    }

    private void loadBookings(String s) {
        adapter = new FirebaseRecyclerAdapter<com.hello.one.x_cut.Model.Request, AdvanceBookingViewHolder>(com.hello.one.x_cut.Model.Request.class, R.layout.viewbooking, AdvanceBookingViewHolder.class,
                viewBooking.orderByChild("phone").equalTo(s)) {
            @Override
            protected void populateViewHolder(AdvanceBookingViewHolder viewHolder, final com.hello.one.x_cut.Model.Request model, int position) {

            viewHolder.BookingId.setText(adapter.getRef(position).getKey());
            viewHolder.BookingStatus.setText(convertCodeToStatus(model.getStatus()));
                viewHolder.BookingPhone.setText(model.getPhone());
                viewHolder.BookingName.setText(model.getName());
                viewHolder.BookingPrice.setText(model.getTotal());

                final com.hello.one.x_cut.Model.Request click = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {
                        Toast.makeText(ViewAdvanceBooking.this, ""+model.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

                    }


        };
        recyclerViewBooking.setAdapter(adapter);


    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Service Booked";
            else if(status.equals("1"))
                return "Payment Done";
        else
            return "Service received";

    }
}