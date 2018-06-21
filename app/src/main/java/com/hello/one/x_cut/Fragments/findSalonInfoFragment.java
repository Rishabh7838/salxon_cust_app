package com.hello.one.x_cut.Fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Salon_Service;
import com.hello.one.x_cut.R;
import com.hello.one.x_cut.ViewHolder.findSalonServiceViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by one on 4/19/2018.
 */

public class findSalonInfoFragment extends DialogFragment {
    private TextView findSaloInfoName;
    private ImageView findSalonInfoImage;
    private Button okDialogButton,totalButton;
    private RecyclerView recyclerMaleService , recyclerFemaleService;
    private RecyclerView.LayoutManager layoutManagerMale,layoutManagerFemale;
    private String dialogTitle,dialogImage,type,key;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private int total_amount = 0;
    DatabaseReference service_name = FirebaseDatabase.getInstance().getReference("users").child("salon");
    private Map<String,Object> maleServiceKey, femaleServiceKey;
    private FirebaseRecyclerAdapter<Salon_Service,findSalonServiceViewHolder> Maleadapter,Femaleadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        View rootView = inflater.inflate(R.layout.find_salon_service_information,container);
        findSaloInfoName = (TextView) rootView.findViewById(R.id.findSalonInfoName);
        findSalonInfoImage = (ImageView) rootView.findViewById(R.id.findSalonInfoImage);
        okDialogButton = (Button) rootView.findViewById(R.id.okServiceDialogButton);
        totalButton = (Button) rootView.findViewById(R.id.totalAmountButton);
        okDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();

            }
        });
        findSaloInfoName.setText(getArguments().getString("title"));
      //  Glide.with(this.getActivity()).load(getArguments().getString("image")).into(findSalonInfoImage);
        Picasso.with(this.getActivity()).load(getArguments().getString("image")).fit().into(findSalonInfoImage);
        //Toast.makeText(this.getActivity(), "image2 = "+getArguments().getString("image"), Toast.LENGTH_SHORT).show();
        type = getArguments().getString("type");
        key = getArguments().getString("key");

        recyclerMaleService = (RecyclerView) rootView.findViewById(R.id.findsalonmaleservice_rv);
        recyclerFemaleService = (RecyclerView) rootView.findViewById(R.id.findsalonfemaleservice_rv);
        recyclerMaleService.setHasFixedSize(true);
        layoutManagerMale = new LinearLayoutManager(this.getActivity());
        layoutManagerFemale = new LinearLayoutManager(this.getActivity());
        recyclerFemaleService.setLayoutManager(layoutManagerFemale);
        recyclerMaleService.setLayoutManager(layoutManagerMale);

        DatabaseReference allServiceKey = FirebaseDatabase.getInstance().getReference("OnTheSpotRequest").child(currentUserId).child("Services");
        allServiceKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 maleServiceKey = new HashMap<>();
                if(dataSnapshot.child("Male").exists()){
                    maleServiceKey = (Map) dataSnapshot.child("Male").getValue();
                    Toast.makeText(getActivity(), "male app"+maleServiceKey.size(), Toast.LENGTH_SHORT).show();
                    for(Map.Entry<String,Object> entry : maleServiceKey.entrySet()) {
                        Toast.makeText(getActivity(), "map value"+entry.getValue(), Toast.LENGTH_SHORT).show();
                        Maleadapter = new FirebaseRecyclerAdapter<Salon_Service, findSalonServiceViewHolder>(Salon_Service.class, R.layout.switch_actionbar, findSalonServiceViewHolder.class,
                                service_name.child(type).child(key).child("services").child("male")) {
                            @Override
                            protected void populateViewHolder(final findSalonServiceViewHolder viewHolder, final Salon_Service model, int position) {
                                if (maleServiceKey.containsValue(Maleadapter.getRef(position).getKey())) {
                                    viewHolder.service.setText(model.getService_name());
                                    viewHolder.servicePrice.setText("Rs : " + model.getTotal_amount());
                                    total_amount += Integer.parseInt(model.getTotal_amount());
                                    totalButton.setText("Total : " + total_amount);
                                    viewHolder.service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked == true) {
                                                total_amount += Integer.parseInt(model.getTotal_amount());
                                                totalButton.setText("Total : " + total_amount);
                                            }
                                            if (isChecked == false) {
                                                total_amount -= Integer.parseInt(model.getTotal_amount());
                                                totalButton.setText("Total : " + total_amount);
                                            }
                                        }
                                    });
                                    viewHolder.setItemClickListener(new ItemClickListener() {
                                        @Override
                                        public void Onclick(View view, int position, boolean isLongClick) {


                                        }
                                    });
                                }
                                else
                                {
                                    Maleadapter.getRef(position).removeValue();
                                }
                            }
                        };
                        recyclerMaleService.setAdapter(Maleadapter);
                    }
                }
                femaleServiceKey = new HashMap<>();
                if(dataSnapshot.child("Female").exists()){
                    femaleServiceKey = (Map) dataSnapshot.child("Female").getValue();
                    for(Map.Entry<String,Object> entry : femaleServiceKey.entrySet()) {
                        Femaleadapter = new FirebaseRecyclerAdapter<Salon_Service, findSalonServiceViewHolder>(Salon_Service.class, R.layout.switch_actionbar, findSalonServiceViewHolder.class,
                                service_name.child(type).child(key).child("services").child("female")) {
                            @Override
                            protected void populateViewHolder(final findSalonServiceViewHolder viewHolder, final Salon_Service model, int position) {
                                if(femaleServiceKey.containsValue(Femaleadapter.getRef(position).getKey())){
                                viewHolder.service.setText(model.getService_name());
                                viewHolder.servicePrice.setText("Rs : " + model.getTotal_amount());
                                total_amount += Integer.parseInt(model.getTotal_amount());
                                totalButton.setText("Total : " + total_amount);
                                viewHolder.service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked == true) {
                                            total_amount += Integer.parseInt(model.getTotal_amount());
                                            totalButton.setText("Total : " + total_amount);
                                        }
                                        if (isChecked == false) {
                                            total_amount -= Integer.parseInt(model.getTotal_amount());
                                            totalButton.setText("Total : " + total_amount);
                                        }
                                    }
                                });

                                viewHolder.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void Onclick(View view, int position, boolean isLongClick) {
                                        //Get categoryId of clicked item
                                        //because category id is key, so we just get key of this  item
                                        //itemList.putExtra("CategoryId",adapter.getRef(position).getKey());

                                    }
                                });
                            }
                            else{
                                    Femaleadapter.getRef(position).removeValue();
                                }
                            }
                        };
                        recyclerFemaleService.setAdapter(Femaleadapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        (this.getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displaywidth = displayMetrics.widthPixels;
        int displayheight = displayMetrics.heightPixels;
        Window window = getDialog().getWindow();
        window.setLayout(displaywidth-100,displayheight-100);
    }
}
