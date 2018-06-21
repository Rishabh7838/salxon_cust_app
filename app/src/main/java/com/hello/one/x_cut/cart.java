package com.hello.one.x_cut;

import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hello.one.x_cut.Database.Database;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Request;
import com.hello.one.x_cut.Model.Style_items;
import com.hello.one.x_cut.ViewHolder.StyleItemViewHolder;
import com.hello.one.x_cut.ViewHolder.cartAdapter;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class cart extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference booking;
    private RecyclerView recyclerViewCart;
    private String categoryId = "";
    private TextView totalPrice;
    private SharedPreferences mTotalServiceSharedPrefrence;
    private FButton complete_Booking;
    private RecyclerView.LayoutManager layoutManager;
    private List<Bookings> carts;
    private cartAdapter adapter;
    private Gson gson = new Gson();
    private Map<String,Bookings> totalServicesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalPrice = (TextView) findViewById(R.id.total);
        complete_Booking = (FButton) findViewById(R.id.Complete_Booking);


        database = FirebaseDatabase.getInstance();
        booking = database.getReference("Booking");

        recyclerViewCart = (RecyclerView) findViewById(R.id.listCart);
        recyclerViewCart.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewCart.setLayoutManager(layoutManager);

        mTotalServiceSharedPrefrence = getSharedPreferences("serviceAddedPrefrence",MODE_PRIVATE);

        complete_Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request("7838257301","Rishabh",totalPrice.getText().toString(),carts);

                booking.child(String.valueOf(System.currentTimeMillis())).setValue(request);

//                new Database(getBaseContext()).cleanCart();
                Toast.makeText(cart.this, "Thank You,Service Booked", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
        loadList();


    }

    private void loadList() {


        if(!mTotalServiceSharedPrefrence.getString("bookingsAddedMapPrefrence","false").equals("false"))
        {
            String Json = mTotalServiceSharedPrefrence.getString("bookingsAddedMapPrefrence","false");
            Type type = new TypeToken<Map<String,Bookings>>(){}.getType();
            totalServicesMap = gson.fromJson(Json,type);
        }

        carts = new ArrayList<>(totalServicesMap.values());
        adapter = new cartAdapter(carts,this);
        if(adapter.getItemCount()==0)
        {
            complete_Booking.setEnabled(false);
        }
        recyclerViewCart.setAdapter(adapter);


//        carts = new Database(this).getCarts();
        //adapter = new cartAdapter(carts,this);
//        if(adapter.getItemCount()==0)
//        {
//            complete_Booking.setEnabled(false);
//        }
//        recyclerViewCart.setAdapter(adapter);
//
//        //calculate total price
//        int total = 0;
//        for(Bookings bookings:carts)
//        {
//            total+=Integer.parseInt(bookings.getPrice());
//        }
//
//        Locale locale =new Locale("en","US");
//        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
//
//        totalPrice.setText(fmt.format(total));

    }
}
