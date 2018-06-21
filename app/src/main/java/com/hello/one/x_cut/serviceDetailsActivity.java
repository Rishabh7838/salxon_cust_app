package com.hello.one.x_cut;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hello.one.x_cut.Model.Style_items;
import com.squareup.picasso.Picasso;

public class serviceDetailsActivity extends AppCompatActivity {


    private TextView service_name,service_description,service_price;
    private ImageView serviceImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnOk;
    private Long timeStampLong;
    private String serviceType,serviceId = "";
    private com.hello.one.x_cut.Model.SalonServices Current_service_items;
    private FirebaseDatabase database;
    private DatabaseReference service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_serviceLayput);
        btnOk = (FloatingActionButton) findViewById(R.id.btnServiceOk);
        serviceImage = (ImageView) findViewById(R.id.img_service);

        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);
        

        service_description = (TextView) findViewById(R.id.service_description);
        service_name = (TextView) findViewById(R.id.service_name);
        service_price = (TextView) findViewById(R.id.service_price);

        database = FirebaseDatabase.getInstance();
        service = database.getReference("Services");

        if(getIntent()!=null){
            serviceId=getIntent().getStringExtra("serviceid");
            serviceType = getIntent().getStringExtra("servicetype");
            if(!serviceId.isEmpty() && !serviceType.isEmpty())
            {
                getDetailIService(serviceId,serviceType);
            }
        }
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        
    }

    private void getDetailIService(String serviceId,String serviceType) {

        service.child(serviceType).child(serviceId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Current_service_items = dataSnapshot.getValue(com.hello.one.x_cut.Model.SalonServices.class);


                Picasso.with(getBaseContext()).load(Current_service_items.getImageUrl()).into(serviceImage);
                collapsingToolbarLayout.setTitle(Current_service_items.getName());
                service_price.setText("0.00");
                service_name.setText(Current_service_items.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        
        
    }


