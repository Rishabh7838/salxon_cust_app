package com.hello.one.x_cut;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hello.one.x_cut.Database.Database;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Style_items;
import com.squareup.picasso.Picasso;

public class StyleDetailCart extends AppCompatActivity {

    private TextView style_name,style_description,style_price;
    ImageView styleImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton  numberButton;
    private Long timeStampLong;
    private String timeStampString,serviceId;
    private Style_items Current_style_items;
    String styleId="";

    private FirebaseDatabase database;
    private DatabaseReference style;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_detail_cart);

        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        styleImage = (ImageView) findViewById(R.id.img_style);



        style_description = (TextView) findViewById(R.id.style_description);
        style_name = (TextView) findViewById(R.id.style_name);
        style_price = (TextView) findViewById(R.id.style_price);

        database = FirebaseDatabase.getInstance();
        style = database.getReference("Styledetails");


        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                timeStampLong = System.currentTimeMillis()/1000;
//                timeStampString = timeStampLong.toString();
//                new Database(getBaseContext()).addCart(new Bookings(styleId,Current_style_items.getName(),
//                        timeStampString,
//                        Current_style_items.getPrice(),
//                        Current_style_items.getDiscount()));

                Toast.makeText(StyleDetailCart.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });



        //get intent here

        if(getIntent()!=null){
            styleId=getIntent().getStringExtra("StyleId");
            if(!styleId.isEmpty())
            {
                getDetailItemStyle(styleId);
            }
        }

    }

    private void getDetailItemStyle(String styleId) {

        style.child(styleId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                 Current_style_items = dataSnapshot.getValue(Style_items.class);


                Picasso.with(getBaseContext()).load(Current_style_items.getImage()).into(styleImage);
                collapsingToolbarLayout.setTitle(Current_style_items.getName());
                style_price.setText(Current_style_items.getPrice());
                style_name.setText(Current_style_items.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
