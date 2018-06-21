package com.hello.one.x_cut;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.hello.one.x_cut.BottomSheets.findSalonBottomSheet;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.SphericalUtil;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.Model.OfferSlider;
import com.hello.one.x_cut.Model.Salon_Service;
import com.hello.one.x_cut.Model.findSalonLayout;
import com.hello.one.x_cut.ViewHolder.StyleViewHolder;
//import com.hello.one.x_cut.ViewHolder.styleAdapter;
import com.hello.one.x_cut.ViewHolder.findSalonAdapter;
import com.hello.one.x_cut.ViewHolder.findSalonServiceViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

//import static android.R.id.categoryList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback{

    FirebaseDatabase database;
    DatabaseReference category;
    private FirebaseRecyclerAdapter<Category,StyleViewHolder> adapter;
    private TextView UserName;
    private Button mCancelButton;
    private GoogleMap mMap;
    private Uri resultUri;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    public static Location mLastLocation;
    private String activeCustomerid;
    private ProgressBar styleItemProgressBar;
    private RecyclerView recyclerViewStyle , findSalonRecyclerView;
    private FloatingActionButton ontheSpotBookingFab,Cartfab,myLocationFab,searchLocationFab;
   // private findSalonBottomSheet bottomSheetFragment;
    private RecyclerView.LayoutManager layoutManager,findSalonLayoutManager;
    private List<Category> categoryList;
    private List<findSalonLayout> findSalonList;
    private AppBarLayout appBarLayout;
    private ImageView openDrawerImage;
    private ProgressBar sliderPB,bottomSheetPB;
    private int radius = 1;
    private int salonFindRange = 1; //10km
    private final static int salonRangeLimit = 10;
    private static String  salonFindviewInfoKey ;
    private Button viewInfo;
    private Boolean isSalonFind=false,isPreimiumSalonFind=false,isRegularSalonFind=false;
    private Map<String,Object> allMaleServices = new HashMap<String,Object>();
    private Map<String,Object> allFemaleServices = new HashMap<String,Object>();
    private Map<String,String> offerImageList = new HashMap<String,String>();
    private SliderLayout offerSlider;
    private Map<String,Object> customerMaleServices;
    private Map<String,Object> customerFemaleServices;
    private Map<Integer,String> regularSalonIdsMap;
    private Map<Integer,String> premiumSalonIdsMap;
    private Map<Float,String> allAvailableMap;
    private TreeMap<Float,String> nearestSalonMap;
    private static int onTheSpotMaleServicePresent;
    private static int onTheSpotFemaleServicePresent;
    private View bottomSheets,findSalxonRV;
    private SharedPreferences CurrentUser;
    private PlaceAutocompleteFragment autocompleteFragment;
    private AutocompleteFilter typeFilter;
    private CardView autoCompleteFragmentCV,sliderCV;
    private String currentSalonId;
    private GeoQuery geoQueryOnTheSpot;
    private String findSalonName;
    int countPremiumSalon =0,countRegularSalon=0;
    String State = "";
    private Marker yourPlaceMarker;
    private LinearLayout openDrawerLayout;
    private DatabaseReference onTheSpotBookingRequest;
    private  ValueEventListener onTheSpotListener;
    private CoordinatorLayout.Behavior behavior;
    private LatLng center;
    private findSalonAdapter mFindSalonAdapter;
    private Category Cat;
    private CircleImageView profileImage;
    private int count=0;

//    private FirebaseRecyclerAdapter<Salon_Service,findSalonServiceViewHolder> adapter;
//    FirebaseDatabase database;
//    DatabaseReference service_name;
//    private RecyclerView recyclerMaleService , recyclerFemaleService;
//    private RecyclerView.LayoutManager layoutManagerMale,layoutManagerFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        customerMaleServices = new HashMap<String,Object>();
        customerFemaleServices = new HashMap<String,Object>();
        regularSalonIdsMap = new HashMap<Integer, String>();
        premiumSalonIdsMap = new HashMap<Integer, String>();
        allAvailableMap = new HashMap<>();

        findSalxonRV = findViewById(R.id.appBar_findsalonRV);
        autoCompleteFragmentCV = (CardView) findViewById(R.id.place_autocomplete_cardView);
        sliderCV = (CardView) findViewById(R.id.sliderCV);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        mCancelButton = (Button) findViewById(R.id.Cancel_Booking);
        sliderPB = (ProgressBar) findViewById(R.id.sliderPB);
        bottomSheetPB = (ProgressBar) findViewById(R.id.bottomSheetPB);
        openDrawerLayout = (LinearLayout) findViewById(R.id.opendrawerImageLayout);

        openDrawerImage = (ImageView) findViewById(R.id.opendrawerImage);
        activeCustomerid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        styleItemProgressBar = (ProgressBar) findViewById(R.id.styleItemProgressBar);

        final FrameLayout v = (FrameLayout) findViewById(R.id.mapRelativeLayout);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

         Cartfab = (FloatingActionButton) findViewById(R.id.fab);
        Cartfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        myLocationFab = (FloatingActionButton) findViewById(R.id.my_location_fab);
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation!=null){
                LatLng latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16);
                    mMap.animateCamera(cameraUpdate);
                }
                else
                    Toast.makeText(MainActivity.this, "Enable your GPS", Toast.LENGTH_SHORT).show();
            }
        });
        searchLocationFab = (FloatingActionButton) findViewById(R.id.select_location_fab);
        searchLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sliderCV.setVisibility(View.GONE);
                //autoCompleteFragmentCV.setVisibility(View.VISIBLE);
                View clickFragment = autocompleteFragment.getView();
                clickFragment.findViewById(R.id.place_autocomplete_search_input).performClick();

            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(MainActivity.this, "cancel button working", Toast.LENGTH_SHORT).show();
                final DatabaseReference cancel_booking = FirebaseDatabase.getInstance().getReference("OnTheSpotRequest").child(activeCustomerid);
                cancel_booking.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            cancel_booking.removeValue();
                            cancelOnTheSpotBookingRequest();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        //AutoplaceComplete fragment


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View nv = navigationView.getHeaderView(0);

        TextView mCustomerNameHead= (TextView)nv.findViewById(R.id.userNameNavigationBar);
        TextView mCustomerNunmberHead= (TextView)nv.findViewById(R.id.userAddressNavigationBar);
        profileImage = (CircleImageView) nv.findViewById(R.id.profile_imageView);
        openDrawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

         CurrentUser = getSharedPreferences("CurrentUser",MODE_PRIVATE);


            mCustomerNameHead.setText(CurrentUser.getString("CurrenUserName","No Name"));
            mCustomerNunmberHead.setText(CurrentUser.getString("CurrenUserNumber","No Number"));
        if(!CurrentUser.getString("CurrentProfileImage","false").equals("false"))
        {
            Picasso.with(getBaseContext()).load(CurrentUser.getString("CurrentProfileImage","false")).noFade().into(profileImage);
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);


            }
        });

        navigationView.setNavigationItemSelectedListener(this);


        //set user name
        View headerView = navigationView.getHeaderView(0);
        UserName = (TextView) headerView.findViewById(R.id.userName);
        //   UserName.setText(Common.currentUser.getName());

        //Load style menu

//        View view = LayoutInflater.from(getContext()).inflate(R.layout.findsalon_bottomsheet_view,null);
//        recyclerMaleService = (RecyclerView) view.findViewById(R.id.findsalonmaleservice_rv);
//        recyclerFemaleService = (RecyclerView) view.findViewById(R.id.findsalonfemaleservice_rv);
//        recyclerMaleService.setHasFixedSize(true);
//        layoutManagerMale = new LinearLayoutManager(MainActivity.this);
//        recyclerMaleService.setLayoutManager(layoutManager);

        recyclerViewStyle = (RecyclerView) findViewById(R.id.recycler_menu);
        recyclerViewStyle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewStyle.setLayoutManager(layoutManager);
        categoryList = new ArrayList<>();

        findSalonRecyclerView = (RecyclerView) findViewById(R.id.findSalon_recycler_menu);
        findSalonRecyclerView.setHasFixedSize(true);
        findSalonLayoutManager = new LinearLayoutManager(this);
        findSalonRecyclerView.setLayoutManager(findSalonLayoutManager);
        findSalonList = new ArrayList<>();

//        bottomSheetFragment = new findSalonBottomSheet();
//        bottomSheetFragment.show(getSupportFragmentManager(),MainActivity.class.getSimpleName());
    //    loadBottomSheetMenu();
        loadStyleMenu();

         bottomSheets = findViewById(R.id.relativeBottomSheetLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheets.getParent()).getLayoutParams();
         behavior = params.getBehavior();
        if(behavior==null)
        {
            //Toast.makeText(this, "Null behavior", Toast.LENGTH_SHORT).show();
        }

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    Toast.makeText(MainActivity.this, "Bottom sheet working", Toast.LENGTH_SHORT).show();

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            State = "DRAGGING";
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            State = "Settling";
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            State = "Expanded";
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            State = "Collapsed";
                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            State = "Hidden";
                            break;
                        }
                    }
                    Toast.makeText(MainActivity.this, "Bottom sheet state change to : " + State, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                    Toast.makeText(MainActivity.this, "sliding working", Toast.LENGTH_SHORT).show();
                    appBarLayout.setAlpha(slideOffset);
                    v.setAlpha(slideOffset);
                }
            });

        }
        else
        {
           Toast.makeText(this, "Not a child of bottomsheet", Toast.LENGTH_SHORT).show();
        }

        ontheSpotBookingFab = (FloatingActionButton) findViewById(R.id.onTheSpotBookingFab);
        ontheSpotBookingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("OnTheSpotRequest");
//                GeoFire GF = new GeoFire(ref);
//                if(mLastLocation!=null) {
//                    GF.setLocation(activeCustomerid, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
//                }
//                else
//                    Toast.makeText(MainActivity.this, "Your current location is not available", Toast.LENGTH_SHORT).show();

                Intent ServiceActivity = new Intent(MainActivity.this,SalonServices.class);
                startActivity(ServiceActivity);
            }
        });

 onTheSpotBookingRequest = FirebaseDatabase.getInstance().getReference("OnTheSpotRequest").child(activeCustomerid);
        onTheSpotListener =   onTheSpotBookingRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Location").exists())
                {
                    findOnTheSpotSalon(dataSnapshot);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Load All services avaliable in salxon in a hashMap
        DatabaseReference malesevices = FirebaseDatabase.getInstance().getReference("Services");
        malesevices.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Male").exists()){
                    allMaleServices = (Map<String, Object>) dataSnapshot.child("Male").getValue();
                }
                if(dataSnapshot.child("Female").exists()){
                    allFemaleServices = (Map<String, Object>) dataSnapshot.child("Female").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setUpOfferSlider();
    }

    private void setUpOfferSlider() {
        offerSlider = (SliderLayout) findViewById(R.id.offerSlider);

        final DatabaseReference banner = FirebaseDatabase.getInstance().getReference().child("Banner");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot banner : dataSnapshot.getChildren()){
                    OfferSlider offerSlider = banner.getValue(OfferSlider.class);
                    offerImageList.put(offerSlider.getName()+"_"+offerSlider.getId(),offerSlider.getImage());

                }
                for(String key : offerImageList.keySet()){
                    String[] keySplit = key.split("_");
                    final String sliderImageName = keySplit[0];
                    String sliderImageId = keySplit[1];

                    //create slider
                    TextSliderView textSliderView = new TextSliderView(getBaseContext());
                    textSliderView
                            .description(sliderImageName)
                            .image(offerImageList.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    //set an offer Intent
                                    Toast.makeText(MainActivity.this, sliderImageName, Toast.LENGTH_SHORT).show();

                                }
                            });

                    offerSlider.addSlider(textSliderView);
                    sliderPB.setVisibility(View.GONE);
                    banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     offerSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        offerSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        offerSlider.setCustomAnimation(new DescriptionAnimation());
        offerSlider.setDuration(4000);
    }

    private void cancelOnTheSpotBookingRequest() {
       // Toast.makeText(this, "Request canceled", Toast.LENGTH_SHORT).show();

//        geoQueryOnTheSpot.removeAllListeners();
        onTheSpotBookingRequest.removeEventListener(onTheSpotListener);

        bottomSheets.setVisibility(View.VISIBLE);
        ontheSpotBookingFab.setVisibility(View.VISIBLE);
        Cartfab.setVisibility(View.VISIBLE);
        findSalxonRV.setVisibility(View.GONE);

    }
    //        private void findOnTheSpotSalon(final DataSnapshot onTheSpotDataSnapshot) {
//
//        //Get On the spot male and female salon
//
//
//        if (onTheSpotDataSnapshot.child("Services").child("Male").exists()) {
//            customerMaleServices = (Map<String, Object>) onTheSpotDataSnapshot.child("Services").child("Male").getValue();
//            Log.e("size of map", "" + customerMaleServices.size());
//            // Toast.makeText(this, "Male service size = "+customerMaleServices.size(), Toast.LENGTH_SHORT).show();
//        }
//        if (onTheSpotDataSnapshot.child("Services").child("Female").exists()) {
//            customerFemaleServices = (Map<String, Object>) onTheSpotDataSnapshot.child("Services").child("Female").getValue();
//            Log.e("size of map2", "" + customerFemaleServices.size());
//            // Toast.makeText(this, "female service size = "+customerFemaleServices.size(), Toast.LENGTH_SHORT).show();
//        }
//
//
//        DatabaseReference salons = FirebaseDatabase.getInstance().getReference("salonAvailable");
//        GeoFire gfSalon = new GeoFire(salons);
//
//        geoQueryOnTheSpot = gfSalon.queryAtLocation(new GeoLocation(SalonServices.currentLatitude, SalonServices.currentLongitude),
//                radius);
//
//        geoQueryOnTheSpot.removeAllListeners();
//        if(geoQueryOnTheSpot!=null){
//        geoQueryOnTheSpot.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(final String key, GeoLocation location) {
//                //if found
//                if (countPremiumSalon < 2) {
//                    DatabaseReference findPerimiumSalon = FirebaseDatabase.getInstance().getReference("users").child("salon").child("p").child(key);
//                    findPerimiumSalon.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            //    Map<String, Object> salonMaleServiceMap = new HashMap<String,Object>();
//                            if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey())) {
//                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                                    if (isSalonFind) {
//                                        return;
//                                    }
//                                    //Toast.makeText(MainActivity.this, "finding premium salon = "+key, Toast.LENGTH_SHORT).show();
//                                    if (!customerMaleServices.isEmpty() && !customerFemaleServices.isEmpty()) {
//                                        onTheSpotMaleServicePresent = 0;
//                                        onTheSpotFemaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            // Toast.makeText(MainActivity.this, "unisex premium", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
//                                                if (customerMaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
//                                                if (customerFemaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            Log.e("size ofservice", "" + onTheSpotMaleServicePresent);
//                                            if (onTheSpotMaleServicePresent == customerMaleServices.size() && onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
//                                                //avaliablePremiumSalonIds[countPremiumSalon] = dataSnapshot.getKey();
//                                                if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
//                                                //Toast.makeText(MainActivity.this, "preium id = "+premiumSalonIdsMap.get(0), Toast.LENGTH_SHORT).show();
//                                                countPremiumSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//
//                                            }
//                                        }
//
//                                    } else if (!customerMaleServices.isEmpty()) {
//                                        onTheSpotMaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("male") || dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            //Toast.makeText(MainActivity.this, "male Premium", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
//                                                if (customerMaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            if (onTheSpotMaleServicePresent == customerMaleServices.size()) {
//                                                //avaliablePremiumSalonIds[countPremiumSalon] = dataSnapshot.getKey();
//                                                if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
//                                                countPremiumSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//                                            }
//                                        }
//                                    } else if (!customerFemaleServices.isEmpty()) {
//                                        onTheSpotFemaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("female") || dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            //Toast.makeText(MainActivity.this, "Female premium", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
//                                                if (customerFemaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            if (onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
//                                                //avaliablePremiumSalonIds[countPremiumSalon] = dataSnapshot.getKey();
//                                                if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
//                                                countPremiumSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                if (countRegularSalon < 2) {
//                    DatabaseReference findRegularSalon = FirebaseDatabase.getInstance().getReference("users").child("salon").child("r").child(key);
//                    findRegularSalon.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            //    Map<String, Object> salonMaleServiceMap = new HashMap<String,Object>();
//
//                            if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey())) {
//                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                                    if (isSalonFind) {
//                                        return;
//                                    }
//                                    // Toast.makeText(MainActivity.this, "finding regular salon = "+key, Toast.LENGTH_SHORT).show();
//                                    if (!customerMaleServices.isEmpty() && !customerFemaleServices.isEmpty()) {
//                                        onTheSpotMaleServicePresent = 0;
//                                        onTheSpotFemaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            //Toast.makeText(MainActivity.this, "unisex regular", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
//                                                if (customerMaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
//                                                if (customerFemaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            if (onTheSpotMaleServicePresent == customerMaleServices.size() && onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
//                                                //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
//                                                if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
//                                                countRegularSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//
//                                            }
//
//                                        }
//                                    } else if (!customerMaleServices.isEmpty()) {
//                                        onTheSpotMaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("male") || dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            //Toast.makeText(MainActivity.this, "Male Regular", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
//                                                if (customerMaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            if (onTheSpotMaleServicePresent == customerMaleServices.size()) {
//                                                //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
//                                                if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
//                                                countRegularSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//                                            }
////                                        if (currentSalonId != null) {
////                                            getSalonInformation("Regular", currentSalonId);
////                                            isRegularSalonFind = true;
////                                        }
//
//                                        }
//                                    } else if (!customerFemaleServices.isEmpty()) {
//                                        onTheSpotFemaleServicePresent = 0;
//                                        if (dataSnapshot.child("type").getValue().equals("female") || dataSnapshot.child("type").getValue().equals("unisex")) {
//                                            //  Toast.makeText(MainActivity.this, "Female Regular", Toast.LENGTH_SHORT).show();
//                                            for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
//                                                if (customerFemaleServices.get(child.getKey()) != null) {
//                                                    onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
//                                                }
//                                            }
//                                            if (onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
//                                                //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
//                                                if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey()))
//                                                    regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
//                                                countRegularSalon++;
//                                                currentSalonId = dataSnapshot.getKey();
//                                            }
//
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//                //if salon not found increase the distance
//                //   Toast.makeText(MainActivity.this, "onGeoQueryReady", Toast.LENGTH_SHORT).show();
//                if (radius <= 50 && (premiumSalonIdsMap.size() < 3 || regularSalonIdsMap.size() < 3)) {
//                    radius++;
//                    findOnTheSpotSalon(onTheSpotDataSnapshot);
//                } else {
//                    // Toast.makeText(MainActivity.this, "Calling getsaloninformation", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MainActivity.this, "premium id size = " + premiumSalonIdsMap.size() + "regular id size = " + regularSalonIdsMap.size(), Toast.LENGTH_SHORT).show();
//                    getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
//
//                }
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//
//            }
//        });
//    }
//    }
double salonLat,salonLon;
    LatLng salonLatlng,myLatLng;
    Location salonLocation = new Location("");
    Location myLocation = new Location("");
    float distance;
    List<Object> locationList = new ArrayList(),myLocationList = new ArrayList<>();
    private void findOnTheSpotSalon(final DataSnapshot onTheSpotDataSnapshot) {

        if (onTheSpotDataSnapshot.child("Services").child("Male").exists()) {
            customerMaleServices = (Map<String, Object>) onTheSpotDataSnapshot.child("Services").child("Male").getValue();


             //Toast.makeText(this, "Male service size = "+customerMaleServices.size(), Toast.LENGTH_SHORT).show();
        }
        myLocationList = (List<Object>) onTheSpotDataSnapshot.child("Location").child("l").getValue();
        myLocation.setLatitude(Double.parseDouble(myLocationList.get(0).toString()));
        myLocation.setLongitude(Double.parseDouble(myLocationList.get(1).toString()));
        if (onTheSpotDataSnapshot.child("Services").child("Female").exists()) {
            customerFemaleServices = (Map<String, Object>) onTheSpotDataSnapshot.child("Services").child("Female").getValue();
             //Toast.makeText(this, "female service size = "+customerFemaleServices.size(), Toast.LENGTH_SHORT).show();
        }
        DatabaseReference salons = FirebaseDatabase.getInstance().getReference("salonAvailable");
        salons.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot salon : dataSnapshot.getChildren()) {
//                    if(count<=salon.getChildrenCount()){
                    locationList = (List<Object>) salon.child("l").getValue();


                    salonLat = Double.parseDouble(locationList.get(0).toString());
                    salonLon = Double.parseDouble(locationList.get(1).toString());

                    salonLatlng = new LatLng(salonLat, salonLon);
                    salonLocation.setLatitude(salonLatlng.latitude);
                    salonLocation.setLongitude(salonLatlng.longitude);


                    distance = myLocation.distanceTo(salonLocation)/1000;

                    if (distance < 50) {

                        allAvailableMap.put(distance,salon.getKey());

                    }
                }
                nearestSalonMap = new TreeMap<>(allAvailableMap);
                if(nearestSalonMap.size()>0) {
                    //Toast.makeText(MainActivity.this, "Total nearest salon = "+nearestSalonMap.size(), Toast.LENGTH_SHORT).show();
                    for (Map.Entry<Float, String> entry : nearestSalonMap.entrySet()) {
                       // Toast.makeText(MainActivity.this, ""+entry.getKey(), Toast.LENGTH_SHORT).show();

                        DatabaseReference findPerimiumSalon = FirebaseDatabase.getInstance().getReference("users").child("salon").child("p").child(entry.getValue());
                        findPerimiumSalon.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Toast.makeText(MainActivity.this, "Finding salon", Toast.LENGTH_SHORT).show();
                                if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                        // Toast.makeText(MainActivity.this, "finding premium salon = "+salon.getKey(), Toast.LENGTH_SHORT).show();
                                        if (!customerMaleServices.isEmpty() && !customerFemaleServices.isEmpty()) {
                                            onTheSpotMaleServicePresent = 0;
                                            onTheSpotFemaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("unisex")) {
                                                // Toast.makeText(MainActivity.this, "unisex premium", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
                                                    if (customerMaleServices.containsValue(child.getKey())) {
                                                        onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
                                                    if (customerFemaleServices.containsValue(child.getKey())) {
                                                        onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                if (onTheSpotMaleServicePresent == customerMaleServices.size() && onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
                                                    if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                                        if (regularSalonIdsMap.get(0) != null)
                                                            regularSalonIdsMap.remove(0);
                                                        premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
                                                    }

                                                }
                                            }

                                        } else if (!customerMaleServices.isEmpty()) {
                                            onTheSpotMaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("male") || dataSnapshot.child("type").getValue().equals("unisex")) {
                                                //Toast.makeText(MainActivity.this, "male Premium", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
                                                    if (customerMaleServices.get(child.getKey()) != null) {
                                                        onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                // Toast.makeText(MainActivity.this, "service present = " + onTheSpotMaleServicePresent, Toast.LENGTH_SHORT).show();
                                                if (onTheSpotMaleServicePresent == customerMaleServices.size()) {
                                                    //avaliablePremiumSalonIds[countPremiumSalon] = dataSnapshot.getKey();
                                                    if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                                        if (regularSalonIdsMap.get(0) != null)
                                                            regularSalonIdsMap.remove(0);
                                                        premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
                                                    }


                                                }
                                            }
                                        } else if (!customerFemaleServices.isEmpty()) {
                                            onTheSpotFemaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("female") || dataSnapshot.child("type").getValue().equals("unisex")) {
                                                //Toast.makeText(MainActivity.this, "Female premium", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
                                                    if (customerFemaleServices.get(child.getKey()) != null) {
                                                        onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                if (onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
                                                    //avaliablePremiumSalonIds[countPremiumSalon] = dataSnapshot.getKey();
                                                    if (!premiumSalonIdsMap.containsValue(dataSnapshot.getKey())) {

                                                        premiumSalonIdsMap.put(countPremiumSalon, dataSnapshot.getKey());
                                                        if (regularSalonIdsMap.get(0) != null)
                                                            regularSalonIdsMap.remove(0);
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
                                                    }


                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference findRegularSalon = FirebaseDatabase.getInstance().getReference("users").child("salon").child("r").child(entry.getValue());
                        findRegularSalon.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //    Map<String, Object> salonMaleServiceMap = new HashMap<String,Object>();

                                if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                                        // Toast.makeText(MainActivity.this, "finding regular salon = "+key, Toast.LENGTH_SHORT).show();
                                        if (!customerMaleServices.isEmpty() && !customerFemaleServices.isEmpty()) {
                                            onTheSpotMaleServicePresent = 0;
                                            onTheSpotFemaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("unisex")) {
                                                //Toast.makeText(MainActivity.this, "unisex regular", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
                                                    if (customerMaleServices.get(child.getKey()) != null) {
                                                        onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
                                                    if (customerFemaleServices.get(child.getKey()) != null) {
                                                        onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                if (onTheSpotMaleServicePresent == customerMaleServices.size() && onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
                                                    //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
                                                    if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                                        regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
                                                        if (premiumSalonIdsMap.get(0) != null)
                                                            premiumSalonIdsMap.remove(0);
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);

                                                    }


                                                }

                                            }
                                        } else if (!customerMaleServices.isEmpty()) {
                                            onTheSpotMaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("male") || dataSnapshot.child("type").getValue().equals("unisex")) {
                                                //Toast.makeText(MainActivity.this, "Male Regular", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("male").getChildren()) {
                                                    if (customerMaleServices.get(child.getKey()) != null) {
                                                        onTheSpotMaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                if (onTheSpotMaleServicePresent == customerMaleServices.size()) {
                                                    //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
                                                    if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                                        regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
                                                        if (premiumSalonIdsMap.get(0) != null)
                                                            premiumSalonIdsMap.remove(0);
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
                                                    }
                                                }


                                            }
                                        } else if (!customerFemaleServices.isEmpty()) {
                                            onTheSpotFemaleServicePresent = 0;
                                            if (dataSnapshot.child("type").getValue().equals("female") || dataSnapshot.child("type").getValue().equals("unisex")) {
                                                //  Toast.makeText(MainActivity.this, "Female Regular", Toast.LENGTH_SHORT).show();
                                                for (DataSnapshot child : dataSnapshot.child("services").child("female").getChildren()) {
                                                    if (customerFemaleServices.get(child.getKey()) != null) {
                                                        onTheSpotFemaleServicePresent++; //checking weather all services are available in salon or not
                                                    }
                                                }
                                                if (onTheSpotFemaleServicePresent == customerFemaleServices.size()) {
                                                    //avaliableRegularSalonIds[countRegularSalon] = dataSnapshot.getKey();
                                                    if (!regularSalonIdsMap.containsValue(dataSnapshot.getKey())) {
                                                        regularSalonIdsMap.put(countRegularSalon, dataSnapshot.getKey());
                                                        if (premiumSalonIdsMap.get(0) != null)
                                                            premiumSalonIdsMap.remove(0);
                                                        getSalonInformation(premiumSalonIdsMap, regularSalonIdsMap);
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//                    count++;
//                }

    }


    private void getSalonInformation(Map<Integer,String> premiumIds,Map<Integer,String> regularIds) {
      Toast.makeText(this, "premium id size "+premiumIds.size(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "regular id size "+regularIds.size(), Toast.LENGTH_SHORT).show();
        if(premiumIds.size()==0 && regularIds.size()==0)
            Toast.makeText(this, "No salon present", Toast.LENGTH_SHORT).show();

        else {
            if (!regularIds.isEmpty()) {
               // Toast.makeText(this, "regular id size "+regularIds.size(), Toast.LENGTH_SHORT).show();
                DatabaseReference regularReference = FirebaseDatabase.getInstance().getReference("users").child("salon").child("r");
                for(int i =0;i<regularIds.size();i++){
                    String regularId = regularIds.get(i)+"";
                regularReference.child(regularId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("Id",dataSnapshot.getKey());
                      //  Toast.makeText(MainActivity.this, "adapter of regular", Toast.LENGTH_SHORT).show();
                        findSalonLayout item = dataSnapshot.getValue(findSalonLayout.class);
                        findSalonList.add(item);
                        mFindSalonAdapter = new findSalonAdapter(findSalonList, MainActivity.this);
                        findSalonRecyclerView.setAdapter(mFindSalonAdapter);

                        findSalxonRV.setVisibility(View.VISIBLE);
                        //Toast.makeText(MainActivity.this, "findsalonlistsize reg"+findSalonList.size(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }

            }  if (!premiumIds.isEmpty()) {

                DatabaseReference premiumReference = FirebaseDatabase.getInstance().getReference("users").child("salon").child("p");
            for(int i =0;i<premiumIds.size();i++){
                String premiumId = premiumIds.get(i)+"";
//                if(avaliablePremiumSalonIds[i]!=null) {
                premiumReference.child(premiumId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.v("Premium Id",dataSnapshot.getKey());
                        //Toast.makeText(MainActivity.this, "adapter of premium", Toast.LENGTH_SHORT).show();
                        findSalonLayout item = dataSnapshot.getValue(findSalonLayout.class);
                        findSalonList.add(item);
                      //  Toast.makeText(MainActivity.this, "findsalonlistsize pre"+findSalonList.size(), Toast.LENGTH_SHORT).show();
                        mFindSalonAdapter = new findSalonAdapter(findSalonList, MainActivity.this);
                        findSalonRecyclerView.setAdapter(mFindSalonAdapter);
                        findSalxonRV.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                }
//
//            }
            }
          //  Toast.makeText(this, "size of salon list = "+findSalonList.size(), Toast.LENGTH_SHORT).show();
            bottomSheets.setVisibility(View.GONE);
            ontheSpotBookingFab.setVisibility(View.GONE);
            Cartfab.setVisibility(View.GONE);
//            mFindSalonAdapter = new findSalonAdapter(findSalonList, MainActivity.this);
//            findSalonRecyclerView.setAdapter(mFindSalonAdapter);
            findSalxonRV.setVisibility(View.VISIBLE);
        }
    }
//    private void loadBottomSheetMenu() {
//        Toast.makeText(this, "loadbottomsheetmenu", Toast.LENGTH_SHORT).show();
//            category.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        category.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                Log.v("Child", "AA gye andur");
//                                Category item = dataSnapshot.getValue(Category.class);
//                                categoryList.add(item);
//                                adapter = new styleAdapter(categoryList,MainActivity.this);
//                                recyclerViewStyle.setAdapter(adapter);
//
//                            }
//
//                            @Override
//                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                                Category item = dataSnapshot.getValue(Category.class);
//                                categoryList.remove(item);
//                                adapter = new styleAdapter(categoryList,MainActivity.this);
//                                recyclerViewStyle.setAdapter(adapter);
//
//                            }
//
//                            @Override
//                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.v("Fragment", "Error");
//                                adapter = new styleAdapter(categoryList,MainActivity.this);
//                                recyclerViewStyle.setAdapter(adapter);
//                            }
//                        });
//                    } else {
//                        Toast.makeText(MainActivity.this, "child dont exist", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }

    private void loadStyleMenu() {
    adapter = new FirebaseRecyclerAdapter<Category, StyleViewHolder>(Category.class,R.layout.style_item,StyleViewHolder.class,category) {
            @Override
            protected void populateViewHolder(final StyleViewHolder viewHolder, Category model, int position) {

                bottomSheetPB.setVisibility(View.GONE);
                viewHolder.styleTextName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.styleImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.styleItemProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
                final Category click = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {
                        //Get categoryId of clicked item

                        Intent itemList = new Intent(MainActivity.this,styleDetails.class);
                        //because category id is key, so we just get key of this  item
                        itemList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(itemList);



                    }
                });
            }
        };

        recyclerViewStyle.setAdapter(adapter);
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location lastLocation : locationResult.getLocations()){
                if(getApplicationContext()!=null){
                    mLastLocation = lastLocation;

                    LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                    mMap.getUiSettings().setScrollGesturesEnabled(true);
                    if(count==0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        count=1;
                    }
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                    //AutocompleteFragment
                    typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .setTypeFilter(3)
                            .build();


                    center = new LatLng(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude());
                    //distane is in meters
                    // heading 0 = north,90=east , 180 = south , 270 = west
                    LatLng northSide = SphericalUtil.computeOffset(center, 10000, 0);
                    //LatLng eastSide = SphericalUtil.computeOffset(center,100000,90);
                    LatLng southSide = SphericalUtil.computeOffset(center, 10000, 180);
                    //LatLng westSide = SphericalUtil.computeOffset(center,100000,270);


                    LatLngBounds bounds = LatLngBounds.builder()
                            .include(northSide)
                            .include(southSide)
                            .build();



                    autocompleteFragment.setHint("Enter area of your choice");

                    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(Place place) {

                            //destination = place.getAddress().toString();
                            LatLng chooseLocationLatLng = place.getLatLng();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(chooseLocationLatLng,16);
                            mMap.animateCamera(cameraUpdate);
                            if(yourPlaceMarker!=null){
                                yourPlaceMarker.remove();
                            }

                                yourPlaceMarker =
                                        mMap.addMarker(new MarkerOptions().position(chooseLocationLatLng).title("Salons near " + place.getName())
                                                .icon(BitmapDescriptorFactory.
                                                        defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

//                            sliderCV.animate()
//                                    .alpha(0.0f).setDuration(300)
//                                    .setListener(new AnimatorListenerAdapter() {
//                                        @Override
//                                        public void onAnimationEnd(Animator animation) {
//                                            super.onAnimationEnd(animation);
//
//                                        }
//                                    });
                            //autoCompleteFragmentCV.setVisibility(View.GONE);
                            //sliderCV.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError(Status status) {

                        }
                    });
                    autocompleteFragment.setBoundsBias(bounds);
                    autocompleteFragment.setFilter(typeFilter);
                    Marker customerMarker =
                            mMap.addMarker(new MarkerOptions().position(latLng).title("You")
                            .icon(BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    if(customerMarker!=null)
                    {
                        customerMarker.remove();
                    }
                    loadAllSalxonSalon();


                }
            }
        }
    };

    GeoQuery loadAllSalongeoQuery;
    private void loadAllSalxonSalon() {

        //Load All avaliable salon in distance of 10km


        final DatabaseReference salonLocation = FirebaseDatabase.getInstance().getReference("salonAvailable");
        GeoFire gfSalon = new GeoFire(salonLocation);

        loadAllSalongeoQuery = gfSalon.queryAtLocation(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                salonFindRange);

        loadAllSalongeoQuery.removeAllListeners();
        loadAllSalongeoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                //Now put Driver on Map

                salonLocation.child(key).child("l").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            List<Object> map = (List<Object>) dataSnapshot.getValue();
                            double LocationLat = 0;
                            double LocationLon = 0;
                            if (map.get(0) != null) {
                                LocationLat = Double.parseDouble(map.get(0).toString());
                            }
                            if (map.get(1) != null) {
                                LocationLon = Double.parseDouble(map.get(1).toString());
                            }

                            FirebaseDatabase.getInstance().getReference("users").child("salon").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.child("p").child(key).child("name").exists())
                                            findSalonName = dataSnapshot.child("p").child(key).child("name").getValue().toString();
                                            else if(dataSnapshot.child("r").child(key).child("name").exists())
                                                findSalonName = dataSnapshot.child("r").child(key).child("name").getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );

                           LatLng salonLatlng = new LatLng(LocationLat,LocationLon);
                            mMap.addMarker(new MarkerOptions().position(salonLatlng).title(findSalonName)
                                    .icon(BitmapDescriptorFactory.
                                            defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //if salon not found increase the distance
                if(salonFindRange<=50)
                {
                salonFindRange++;
                    loadAllSalxonSalon();
                }
                else
                {
                    loadAllSalongeoQuery.removeAllListeners();
                }



            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try{
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style_map)
            );
            if(!isSuccess)
                Log.e("Error","Map style load failed");
        }
        catch (Resources.NotFoundException ex){
            ex.printStackTrace();
        }
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if(i== GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    ontheSpotBookingFab.animate()
                            .alpha(0.0f)
                            .setDuration(350);
                    Cartfab.animate()
                            .alpha(0.0f)
                            .setDuration(350);
                    myLocationFab.animate()
                            .alpha(0.0f)
                            .setDuration(350);
                    searchLocationFab.animate()
                            .alpha(0.0f)
                            .setDuration(350);
                    ontheSpotBookingFab.setVisibility(View.GONE);
                    Cartfab.setVisibility(View.GONE);
                    myLocationFab.setVisibility(View.GONE);
                    searchLocationFab.setVisibility(View.GONE);
                }
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                ontheSpotBookingFab.animate().alpha(1.0f)
                        .setDuration(350);
                Cartfab.animate().alpha(1.0f)
                        .setDuration(350);
                ontheSpotBookingFab.setVisibility(View.VISIBLE);
                Cartfab.setVisibility(View.VISIBLE);
                myLocationFab.animate().alpha(1.0f)
                        .setDuration(350);
                myLocationFab.setVisibility(View.VISIBLE);
                searchLocationFab.animate().alpha(1.0f)
                        .setDuration(350);
                searchLocationFab.setVisibility(View.VISIBLE);
            }
        });
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }else{
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                checkLocationPermission();
            }
        }
    }


    private void checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("give permission")
                        .setMessage("give permission message")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //item.setActionView(R.layout.switch_actionbar);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

//



        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Logout) {
            FirebaseAuth.getInstance().signOut();
            Intent Go = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(Go);
            finish();
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_advance) {
            Intent booking = new Intent(MainActivity.this,ViewAdvanceBooking.class);
            startActivity(booking);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        offerSlider.stopAutoCycle();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == Activity.RESULT_OK)
        {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            profileImage.setImageURI(resultUri);
            if(resultUri!=null)
            {
                final DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference("users").child("Customers").child(activeCustomerid);
                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_images").child(activeCustomerid);
                Bitmap bitmap = null;
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,20,baos);
                byte[] byteData = baos.toByteArray();
                UploadTask uploadTask = filePath.putBytes(byteData);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Profile failure",Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                });

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();

                        Map imageMap = new HashMap();
                        imageMap.put("ProfileImageUrl",downloadUri.toString());
                        currentUserRef.updateChildren(imageMap);
                        //   finish();
                    }
                });

                Picasso.with(getBaseContext()).load(resultUri).noFade().into(profileImage);

                CurrentUser = getSharedPreferences("CurrentUser",MODE_PRIVATE);
                SharedPreferences.Editor editor = CurrentUser.edit();
                editor.putString("CurrentProfileImage",resultUri.toString());
                editor.apply();

            }

            // finish();
        }
    }
}
