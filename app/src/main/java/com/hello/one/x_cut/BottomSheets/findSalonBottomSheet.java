package com.hello.one.x_cut.BottomSheets;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.MainActivity;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.Model.Salon_Service;
import com.hello.one.x_cut.R;
import com.hello.one.x_cut.ViewHolder.StyleViewHolder;
import com.hello.one.x_cut.ViewHolder.findSalonAdapter;
import com.hello.one.x_cut.ViewHolder.findSalonServiceViewHolder;
import com.hello.one.x_cut.styleDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by one on 3/21/2018.
 */

public class findSalonBottomSheet extends BottomSheetDialogFragment {

    private FirebaseRecyclerAdapter<Salon_Service,findSalonServiceViewHolder> Maleadapter,Femaleadapter;
    FirebaseDatabase database;
    DatabaseReference service_name;
    private RecyclerView recyclerMaleService , recyclerFemaleService;
    private String dialogTitle,dialogImage,type,key;
    private RecyclerView.LayoutManager layoutManagerMale,layoutManagerFemale;
    private TextView findSaloInfoName;
    private ImageView findSalonInfoImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        DatabaseReference service_name = FirebaseDatabase.getInstance().getReference("users").child("salon");
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.find_salon_service_information, null);

//
//        recyclerMaleService = (RecyclerView) rootView.findViewById(R.id.findsalonmaleservice_rv);
//        recyclerFemaleService = (RecyclerView) rootView.findViewById(R.id.findsalonfemaleservice_rv);
//        findSaloInfoName = (TextView) rootView.findViewById(R.id.findSalonInfoName);
//        findSalonInfoImage = (ImageView) rootView.findViewById(R.id.findSalonInfoImage);
//        findSaloInfoName.setText(getArguments().getString("title"));
//        Glide.with(this.getActivity()).load(getArguments().getString("image")).into(findSalonInfoImage);
//        type = getArguments().getString("type");
//        key = getArguments().getString("key");
//        recyclerMaleService.setHasFixedSize(true);
//        layoutManagerMale = new LinearLayoutManager(getContext());
//        recyclerMaleService.setLayoutManager(layoutManagerMale);
//
//        Toast.makeText(this.getActivity(), "type = " + type + " key = " + key, Toast.LENGTH_SHORT).show();
//        Maleadapter = new FirebaseRecyclerAdapter<Salon_Service, findSalonServiceViewHolder>(Salon_Service.class, R.layout.switch_actionbar, findSalonServiceViewHolder.class,
//                service_name.child(type).child(key).child("services").child("male")) {
//            @Override
//            protected void populateViewHolder(final findSalonServiceViewHolder viewHolder, Salon_Service model, int position) {
//                viewHolder.service.setText(model.getService_name());
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void Onclick(View view, int position, boolean isLongClick) {
//
//
//                    }
//                });
//            }
//        };
//        Femaleadapter = new FirebaseRecyclerAdapter<Salon_Service, findSalonServiceViewHolder>(Salon_Service.class, R.layout.switch_actionbar, findSalonServiceViewHolder.class,
//                service_name.child(type).child(key).child("services").child("female")) {
//            @Override
//            protected void populateViewHolder(final findSalonServiceViewHolder viewHolder, Salon_Service model, int position) {
//                viewHolder.service.setText(model.getService_name());
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void Onclick(View view, int position, boolean isLongClick) {
//                        //Get categoryId of clicked item
//                        //because category id is key, so we just get key of this  item
//                        //itemList.putExtra("CategoryId",adapter.getRef(position).getKey());
//
//                    }
//                });
//            }
//        };
//        recyclerMaleService.setAdapter(Maleadapter);
//        recyclerFemaleService.setAdapter(Femaleadapter);
//
////
//        final BottomSheetBehavior behavior = BottomSheetBehavior.from(rootView);
//        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                String State = "";
//
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_DRAGGING: {
//
//                        State = "DRAGGING";
//                        break;
//                    }
//                    case BottomSheetBehavior.STATE_SETTLING: {
//                        State = "Settling";
//                        break;
//                    }
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//
//                        recyclerMaleService.setAdapter(Maleadapter);
//                        recyclerFemaleService.setAdapter(Femaleadapter);
//
//                        State = "Expanded";
//                        break;
//                    }
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        State = "Collapsed";
//                        break;
//                    }
//                    case BottomSheetBehavior.STATE_HIDDEN: {
//                        dismiss();
//                        State = "Hidden";
//                        break;
//                    }
//                }
//                Toast.makeText(getContext(), "Bottom sheet state change to : " + State, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//
//        });

        return rootView;
    }


}
