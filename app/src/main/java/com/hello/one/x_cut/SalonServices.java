package com.hello.one.x_cut;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.Model.HaircutModel;
import com.hello.one.x_cut.Model.ParticularServiceModel;
import com.hello.one.x_cut.Model.TopService;
import com.hello.one.x_cut.ViewHolder.HaircutViewHolder;
import com.hello.one.x_cut.ViewHolder.ParticularServiceViewHolder;
import com.hello.one.x_cut.ViewHolder.SalonservicesViewHolder;
import com.hello.one.x_cut.ViewHolder.StyleViewHolder;
import com.hello.one.x_cut.ViewHolder.TopServiceViewHolder;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalonServices extends AppCompatActivity {
    private ToggleButton GenderButton;
    private TextView mGender;
    private ImageView topServiceImage;
    private FirebaseDatabase database;
    private DatabaseReference services;
    private Location mchooseLocation;
    private FirebaseRecyclerAdapter<com.hello.one.x_cut.Model.SalonServices,SalonservicesViewHolder> adapter;
    private FirebaseRecyclerAdapter<HaircutModel,HaircutViewHolder> particularSubServiceAdapter;
    private FirebaseRecyclerAdapter<TopService,TopServiceViewHolder> topServiceAdapter;
    private FirebaseRecyclerAdapter<ParticularServiceModel,ParticularServiceViewHolder> particularServiceAdapter;
    private RecyclerView recyclerViewStyle,particularServiceRecyclerView;
    private LatLng chooseLocationLatLng;
    private AppBarLayout appBarLayout;
    private SharedPreferences mServiceAddedSharedPrefrence,mBookingAddedSharedPrefrence;
    private RecyclerView.LayoutManager layoutManager;
    private Gson gson = new Gson();
    private Button findSalon,serviceDetailsButton,cartButton;
    public static double currentLatitude,currentLongitude;
    private Boolean oneTimeEntry = true;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private MaterialSearchBar serviceSearchBar;
    private Map<String,String> maleChoosenServices= new HashMap<String,String>();
    private Map<String,String> femaleChoosenServices= new HashMap<String,String>();
    private Map<String,Integer> serviceAddedMap = new HashMap<>();
    private Map<String,Bookings> bookingsMap = new HashMap<>();
    private static int countService=0;
    //private static int onOff=0;
    private List<FirebaseRecyclerAdapter<HaircutModel,HaircutViewHolder>> particularSubServiceAdapterList = new ArrayList();
    //private FirebaseRecyclerAdapter<HaircutModel,HaircutViewHolder> particularSubServiceAdapterList[];
    private Boolean maleParticularService = false,femaleParticularService = false;

    private RelativeLayout mainActivityLayout;
    int rotationAngle = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon_services);


        GenderButton = (ToggleButton) findViewById(R.id.toggleGender);
        mGender = (TextView) findViewById(R.id.GenderStatus);
        serviceSearchBar = (MaterialSearchBar) findViewById(R.id.serviceSearchBar);
        mainActivityLayout = (RelativeLayout) findViewById(R.id.salonserviceRelativeLayout);
        findSalon = (Button) findViewById(R.id.findOnTheSpotSalonButton);
        cartButton = (Button) findViewById(R.id.serviceCartButton);
        serviceDetailsButton = (Button) findViewById(R.id.ServiceDetailsButton);
        recyclerViewStyle = (RecyclerView) findViewById(R.id.service_recycler_menu);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayoutSalonService);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSalonService);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("");

        mServiceAddedSharedPrefrence = getSharedPreferences("serviceAddedPrefrence",MODE_PRIVATE);
        mBookingAddedSharedPrefrence = getSharedPreferences("bookingsAddedMapPrefrence",MODE_PRIVATE);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View vi = inflater.inflate(R.layout.top_service_view, null);
//        particularServiceRecyclerView = (RecyclerView) vi.findViewById(R.id.particularTypeServices);
//        topServiceImage = (ImageView) vi.findViewById(R.id.top_Service_Image);
        recyclerViewStyle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewStyle.setLayoutManager(layoutManager);



        database = FirebaseDatabase.getInstance();
        services = database.getReference("Services");


//

        if(oneTimeEntry){
            oneTimeEntry = false;

            mainActivityLayout.setBackgroundResource(R.drawable.male_color);
            loadMaleRecyclerView();
            //loadMaleParticularServices();
//            hairCutView();
        }
        GenderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mainActivityLayout.setBackgroundResource(R.drawable.female_color);
                    mGender.setText("Female");
                    maleParticularService = false;
                    loadFemaleRecyclerView();
                }
                else {
                    mainActivityLayout.setBackgroundResource(R.drawable.male_color);
                    mGender.setText("Male");
                    femaleParticularService = false;
                    loadMaleRecyclerView();
                    //loadMaleParticularServices();
                }
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCart = new Intent(SalonServices.this,cart.class);
                startActivity(goCart);
            }
        });
        findSalon.setOnClickListener(new View.OnClickListener() {
            DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("OnTheSpotRequest").child(currentUser);
            @Override
            public void onClick(View v) {
                if(maleChoosenServices.isEmpty() && femaleChoosenServices.isEmpty())
                    Toast.makeText(SalonServices.this, "Please choose Some service ", Toast.LENGTH_SHORT).show();
                else
                {
                    if(!maleChoosenServices.isEmpty())
                    {
                        Reference.child("Services").child("Male").setValue(maleChoosenServices);
                    }
                     if(!femaleChoosenServices.isEmpty())
                    {
                        Reference.child("Services").child("Female").setValue(femaleChoosenServices);
                    }
                    if(chooseLocationLatLng==null) {
                        mchooseLocation = MainActivity.mLastLocation;
                        if (mchooseLocation != null) {
                            currentLatitude = mchooseLocation.getLatitude();
                            currentLongitude = mchooseLocation.getLongitude();
                        }
                             else
                                Toast.makeText(SalonServices.this, "Your current location is not available", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        currentLatitude = chooseLocationLatLng.latitude;
                        currentLongitude = chooseLocationLatLng.longitude;
                    }
                    GeoFire GF = new GeoFire(Reference);
                    GF.setLocation("Location", new GeoLocation(currentLatitude, currentLongitude));
                    Intent mainactivity = new Intent(SalonServices.this,MainActivity.class);
                    startActivity(mainactivity);
                    finish();


                }
            }
        });

    }

    private void loadMaleRecyclerView() {

//
//        adapter = new FirebaseRecyclerAdapter<com.hello.one.x_cut.Model.SalonServices, SalonservicesViewHolder>(com.hello.one.x_cut.Model.SalonServices.class,R.layout.serviceview
//                ,SalonservicesViewHolder.class
//                ,services.child("Male")) {
//            @Override
//            protected void populateViewHolder(final SalonservicesViewHolder viewHolder, com.hello.one.x_cut.Model.SalonServices model, final int position) {
//                viewHolder.serviceName.setText(model.getName());
//                viewHolder.serviceDescription.setText(model.getDescription());
//                viewHolder.serviceOffer.setText(model.getOffer());
//                Picasso.with(getBaseContext()).load(model.getImageUrl()).into(viewHolder.serviceImage);
//                final String maleServiceKey = adapter.getRef(position).getKey();
//                if(maleChoosenServices.get(maleServiceKey)!=null)
//                {
//                    viewHolder.addServiceButton.setText("Service Added");
//                    viewHolder.addServiceButton.setBackgroundResource(R.drawable.service_added_color);
//                }
//
//                viewHolder.addServiceButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(viewHolder.addServiceButton.getText().equals("Add")){
//                            viewHolder.addServiceButton.setText("Service Added");
//                            viewHolder.addServiceButton.setBackgroundResource(R.drawable.service_added_color);
//                            maleChoosenServices.put(maleServiceKey,maleServiceKey);
//                        }
//                        else if(viewHolder.addServiceButton.getText().equals("Service Added")){
//                            viewHolder.addServiceButton.setText("Add");
//                            viewHolder.addServiceButton.setBackgroundResource(R.drawable.red_color);
//                            if(maleChoosenServices.get(maleServiceKey)!=null)
//                                maleChoosenServices.remove(maleServiceKey);
//                        }
//
//                    }
//                });
//
//                viewHolder.serviceDetailButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent go = new Intent(SalonServices.this,serviceDetailsActivity.class);
//                                go.putExtra("serviceid",adapter.getRef(position).getKey());
//                        go.putExtra("servicetype","Male");
//                        startActivity(go);
//
//                    }
//                });
//                final com.hello.one.x_cut.Model.SalonServices click = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void Onclick(View view, int position, boolean isLongClick) {
//
//                        Log.e("Service Button","Click On CardView");
//
//                    }
//                });
//            }
//        };
//
//        recyclerViewStyle.setAdapter(adapter);

        topServiceAdapter = new FirebaseRecyclerAdapter<TopService, TopServiceViewHolder>(TopService.class, R.layout.top_service_view
                , TopServiceViewHolder.class
                , services.child("Male")) {
            @Override
            protected void populateViewHolder(final TopServiceViewHolder viewHolder, TopService model, int position) {

            //    Toast.makeText(SalonServices.this, "Image = "+model.getTopServiceImage()+", Name = "+model.getTopServiceName(), Toast.LENGTH_SHORT).show();
                Picasso.with(getBaseContext()).load(model.getTopServiceImage()).into(viewHolder.topServiceImage);
                viewHolder.topServiceName.setText(model.getTopServiceName());
                viewHolder.topServiceDescription.setText(model.getTopServiceDescription());
              // particularServiceRecyclerView = viewHolder.particularServiveRecyclerView;
//                viewHolder.particularServiveRecyclerView.setHasFixedSize(true);
//                viewHolder.particularServiveRecyclerView.setLayoutManager(new LinearLayoutManager(SalonServices.this));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {

                        maleParticularService = true;
                        loadMaleParticularServices();

//                        if(rotationAngle==0){
//                            viewHolder.topServiceImage.setVisibility(View.GONE);
//                           // particularServiceRecyclerView.setVisibility(View.VISIBLE);
//                            //loadMaleParticularServices(particularServiceRecyclerView);
//                        }
//                        else if(rotationAngle==180){
//                            viewHolder.topServiceImage.setVisibility(View.VISIBLE);
////                            particularServiceRecyclerView.setVisibility(View.VISIBLE);
//                        }
//                        ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.topServiceArrow, "rotation", rotationAngle, rotationAngle + 180);
//                        animator.setDuration(300);
//                        animator.start();
//                        rotationAngle += 180;
//                        rotationAngle = rotationAngle % 360;
//                        Toast.makeText(SalonServices.this, "key = "+topServiceAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();


                    }
                });
            }
        };
        recyclerViewStyle.setAdapter(topServiceAdapter);
    }

    private void hairCutView(String serviceName, final int pos) {
        particularSubServiceAdapterList.add(pos,new FirebaseRecyclerAdapter<HaircutModel
                , HaircutViewHolder>(HaircutModel.class,
                R.layout.service_expandable_view
                , HaircutViewHolder.class
                , services.child("Male").child("Hair_Services").child("Services").child(serviceName).child("Sub_Services")) {
            @Override
            protected void populateViewHolder(final HaircutViewHolder viewHolder, final HaircutModel model, final int position) {

                viewHolder.haircutServiceName.setText(model.getHairCutName());

                //Toast.makeText(SalonServices.this, "adapter size = "+particularSubServiceAdapter.getItemCount(), Toast.LENGTH_SHORT).show();
                if(!mServiceAddedSharedPrefrence.getString("serviceAddedMapPrefrence","false").equals("false"))
                {
                    String Json = mServiceAddedSharedPrefrence.getString("serviceAddedMapPrefrence","false");
                    Type type = new TypeToken<Map<String,Integer>>(){}.getType();
                    serviceAddedMap = gson.fromJson(Json,type);
                }
                if(serviceAddedMap.size()>0){
                    if(serviceAddedMap.get(particularSubServiceAdapterList.get(pos).getRef(position).getKey())!=null){
                        viewHolder.serviceStatus.setText("Add Service");
                        viewHolder.serviceStatus.setTextColor(getResources().getColor(R.color.buttonSignIn));
                        viewHolder.addServiceButton.setNumber(serviceAddedMap.get(particularSubServiceAdapterList.get(pos).getRef(position).getKey()).toString());
                        viewHolder.Background_card.setBackgroundResource(R.drawable.service_added_color);
                    }
                }

                viewHolder.addServiceButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                        int checkServiceChange = newValue-oldValue;
                            countService+=checkServiceChange;
                        Toast.makeText(SalonServices.this, "Quantity = "+countService, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor1 = mBookingAddedSharedPrefrence.edit();
                        SharedPreferences.Editor editor = mServiceAddedSharedPrefrence.edit();
                        if(newValue>0){

                            serviceAddedMap.put(particularSubServiceAdapterList.get(pos).getRef(position).getKey(),newValue);
                            bookingsMap.put(particularSubServiceAdapterList.get(pos).getRef(position).getKey(),new Bookings(newValue+"",model.getHairCutName(),"","",""));
                            String json = gson.toJson(serviceAddedMap);
                            String json2 = gson.toJson(bookingsMap);


                            editor.putString("serviceAddedMapPrefrence",json);
                            editor1.putString("bookingsAddedMapPrefrence",json2);
                            editor1.apply();
                            editor.apply();
                            viewHolder.serviceStatus.setText("Added");
                            viewHolder.serviceStatus.setTextColor(getResources().getColor(R.color.slideBackground));



                        }
                        else if(newValue<=0){
                            viewHolder.addServiceButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            viewHolder.serviceStatus.setText("Add Service");
                            serviceAddedMap.remove(particularSubServiceAdapterList.get(pos).getRef(position).getKey());
                            String json = gson.toJson(serviceAddedMap);
                            editor.putString("serviceAddedMapPrefrence",json);
                            editor.apply();
                            bookingsMap.remove(particularSubServiceAdapterList.get(pos).getRef(position).getKey());
                            String json2 = gson.toJson(bookingsMap);
                            editor1.putString("bookingsAddedMapPrefrence",json2);
                            editor1.apply();
                        }
                    }

                });

                if(countService>=0)
                    cartButton.setText("Cart is Happy("+countService+")");
                else if(countService==0)
                    cartButton.setText("Cart isn't Happy(0)");

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {
                        Log.e("particular service","clicked");

                        Toast.makeText(SalonServices.this, "itemListmerWorking", Toast.LENGTH_SHORT).show();
                    }
                });
          //      Toast.makeText(SalonServices.this, "Inside particula service", Toast.LENGTH_SHORT).show();
            }
        });



    }
//LinearLayoutManager hairLayout =
//RecyclerView particularServiceRecyclerView,FirebaseRecyclerAdapter<TopService, TopServiceViewHolder> topServiceAdapter,int position
    private void loadMaleParticularServices() {
//        if(topServiceAdapter.getItemCount()>0)
//        topServiceAdapter.cleanup();
        recyclerViewStyle.removeAllViews();
        if(particularServiceAdapter==null) {
            particularServiceAdapter = new FirebaseRecyclerAdapter<ParticularServiceModel, ParticularServiceViewHolder>(ParticularServiceModel.class,
                    R.layout.particular_service_view
                    , ParticularServiceViewHolder.class
                    , services.child("Male").child("Hair_Services").child("Services")) {
                @Override
                protected void populateViewHolder(final ParticularServiceViewHolder viewHolder, ParticularServiceModel model, int position) {
//                    Toast.makeText(SalonServices.this, "ImageParticular = "+model.getParticularServiceImage()+", NameParticular = "+model.getParticularServiceName(), Toast.LENGTH_SHORT).show();
                    Picasso.with(getBaseContext()).load(model.getParticularServiceImage()).into(viewHolder.particularServiceImage);
                    viewHolder.particularServiceName.setText(model.getParticularServiceName());
                    hairCutView(particularServiceAdapter.getRef(position).getKey(),position);


                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void Onclick(View view, int position, boolean isLongClick) {
                            Log.e("particular service","clicked");
                            ObjectAnimator animator = ObjectAnimator.ofFloat(viewHolder.particularServiceArrow, "rotation", rotationAngle, rotationAngle + 180);
                        animator.setDuration(300);
                        animator.start();
                        rotationAngle += 180;
                        rotationAngle = rotationAngle % 360;


                            if(particularSubServiceAdapterList.size()>0) {

                                viewHolder.haircutRV.setLayoutManager(new LinearLayoutManager(SalonServices.this));
                                viewHolder.haircutRV.setHasFixedSize(true);
                                viewHolder.haircutRV.setAdapter(particularSubServiceAdapterList.get(position));
                                //Toast.makeText(SalonServices.this, "adapter " + particularSubServiceAdapter.getItemCount(), Toast.LENGTH_SHORT).show();

                                if (viewHolder.particularServiceImage.getVisibility()==View.VISIBLE) {
                                    viewHolder.particularServiceImage.animate()
                                    .alpha(0.0f)
                                    .setDuration(250);
                                    viewHolder.particularServiceImage.setVisibility(View.GONE);
                                    viewHolder.haircutRV.animate().alpha(1.0f)
                                            .setDuration(350);
                                    viewHolder.haircutRV.setVisibility(View.VISIBLE);
//                                    onOff = 1;
                                } else if (viewHolder.particularServiceImage.getVisibility()==View.GONE) {
                                    viewHolder.particularServiceImage.animate().alpha(1.0f)
                                            .setDuration(350);
                                   viewHolder.particularServiceImage.setVisibility(View.VISIBLE);
                                    viewHolder.haircutRV.animate().alpha(0.0f)
                                            .setDuration(250);
                                    viewHolder.haircutRV.setVisibility(View.GONE);
//                                    onOff = 0;
                                }
                            }
                        }
                    });
                    Toast.makeText(SalonServices.this, "Inside particula service", Toast.LENGTH_SHORT).show();
                }
            };
            recyclerViewStyle.setAdapter(particularServiceAdapter);
        }
        else if(particularServiceAdapter.getItemCount()>0)
            recyclerViewStyle.setAdapter(particularServiceAdapter);
       // viewHolder.particularServiveRecyclerView.setAdapter(particularServiceAdapter);



    }


    private void loadFemaleRecyclerView() {

        adapter = new FirebaseRecyclerAdapter<com.hello.one.x_cut.Model.SalonServices, SalonservicesViewHolder>(com.hello.one.x_cut.Model.SalonServices.class,R.layout.serviceview
                ,SalonservicesViewHolder.class
                ,services.child("Female")) {
            @Override
            protected void populateViewHolder(final SalonservicesViewHolder viewHolder, com.hello.one.x_cut.Model.SalonServices model, final int position) {
                viewHolder.serviceName.setText(model.getName());
                viewHolder.serviceDescription.setText(model.getDescription());
                viewHolder.serviceOffer.setText(model.getOffer());
                Picasso.with(getBaseContext()).load(model.getImageUrl()).into(viewHolder.serviceImage);
                final String femaleServiceKey = adapter.getRef(position).getKey();

                if(femaleChoosenServices.get(femaleServiceKey)!=null)
                {
                    viewHolder.addServiceButton.setText("Service Added");
                    viewHolder.addServiceButton.setBackgroundResource(R.drawable.service_added_color);
                }
                viewHolder.addServiceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(viewHolder.addServiceButton.getText().equals("Add")){
                            viewHolder.addServiceButton.setText("Service Added");
                            viewHolder.addServiceButton.setBackgroundResource(R.drawable.service_added_color);
                            femaleChoosenServices.put(femaleServiceKey,femaleServiceKey);
                        }
                        else if(viewHolder.addServiceButton.getText().equals("Service Added")){
                            viewHolder.addServiceButton.setText("Add");
                            viewHolder.addServiceButton.setBackgroundResource(R.drawable.red_color);
                            if(femaleChoosenServices.get(femaleServiceKey)!=null)
                                femaleChoosenServices.remove(femaleServiceKey);
                        }
                    }
                });

                viewHolder.serviceDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent go = new Intent(SalonServices.this,serviceDetailsActivity.class);
                        go.putExtra("serviceid",adapter.getRef(position).getKey());
                        go.putExtra("servicetype","Female");
                        startActivity(go);

                    }
                });
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {

                        Log.e("Service Button","Click On CardView");

                    }
                });
            }
        };

        recyclerViewStyle.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if(maleParticularService==true){
//            if(particularServiceAdapter.getItemCount()>0)
//                particularServiceAdapter.cleanup();
           // loadMaleRecyclerView();
            recyclerViewStyle.removeAllViews();
            recyclerViewStyle.setAdapter(topServiceAdapter);
            maleParticularService=false;
        }
        else {
            super.onBackPressed();
        }
    }
}



