package com.hello.one.x_cut.ViewHolder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.hello.one.x_cut.BottomSheets.findSalonBottomSheet;
import com.hello.one.x_cut.BottomSheets.findSalonBottomSheet;
import com.hello.one.x_cut.Fragments.findSalonInfoFragment;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.MainActivity;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Salon_Service;
import com.hello.one.x_cut.Model.findSalonLayout;
import com.hello.one.x_cut.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by one on 4/1/2018.
 */

class findSalonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView findSalonName;
    public TextView findSalonTotalPrice;
    public TextView salonType;
    public TextView findSaloAddress;
    public TextView findSalonPhoneNumber;
    public RatingBar salonRating;
    public ImageView findSalonImage;
    public Button findSalonInfo;


public ItemClickListener itemClickListener;

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        }

public void setCartName(TextView cartName) {
        findSalonName = cartName;
        }

public findSalonViewHolder(View itemView) {
        super(itemView);

        findSalonName = (TextView) itemView.findViewById(R.id.findSalonName);
        findSalonTotalPrice = (TextView) itemView.findViewById(R.id.findSalonServiceTotal);
        salonType = (TextView) itemView.findViewById(R.id.findSalonType);
        findSaloAddress = (TextView) itemView.findViewById(R.id.findSalonAddress);
        findSalonPhoneNumber = (TextView) itemView.findViewById(R.id.findSalonPhone);
        findSalonImage = (ImageView) itemView.findViewById(R.id.findSalonImage);
        salonRating = (RatingBar) itemView.findViewById(R.id.findSalonRating);
        findSalonInfo = (Button) itemView.findViewById(R.id.salonInfo);
    itemView.setOnClickListener(this);
        }

        

@Override
public void onClick(View v) {
        itemClickListener.Onclick(v,getAdapterPosition(),false);
        }
        }


public class findSalonAdapter extends RecyclerView.Adapter<findSalonViewHolder>{
    public  String salonFindKey;
    public  String salonFindtype;
    private Dialog salonInfoDialog;
    private List<findSalonLayout> listData= new ArrayList<>();
    private Context context;
    private ImageView findSalonInfoImage;
    private TextView findSaloInfoName;

    FirebaseDatabase database;
    DatabaseReference service_name;


    public findSalonAdapter(List<findSalonLayout> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public findSalonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.find_salon_view,parent,false);
        final findSalonViewHolder holder = new findSalonViewHolder(itemView);








        holder.findSalonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference findsalonkey = FirebaseDatabase.getInstance().getReference("users").child("salon");
                findsalonkey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(listData.get(holder.getAdapterPosition()).getSalon_type().equals("premium")){
                          Query premquery = FirebaseDatabase.getInstance().getReference("users").child("salon").child("p")
                                  .orderByChild("phone_no").equalTo(listData.get(holder.getAdapterPosition()).getPhone_no());

                                  premquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                      @Override
                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                          for(DataSnapshot child : dataSnapshot.getChildren())
                                          salonFindKey = child.getKey();
                                          salonFindtype = "p";
                                          //loadAllServices(salonFindtype,salonFindKey);
                                          Bundle bundle = new Bundle();
                                          bundle.putString("title",listData.get(holder.getAdapterPosition()).getName());
                                          bundle.putString("image",listData.get(holder.getAdapterPosition()).getImageUrl());
                                          bundle.putString("key",salonFindKey);
                                          bundle.putString("type",salonFindtype);
                                          FragmentActivity fragmentActivity = (FragmentActivity) context;

                                          final findSalonInfoFragment mfindSalonInfoFragment = new findSalonInfoFragment();
                                          mfindSalonInfoFragment.setArguments(bundle);
                                          mfindSalonInfoFragment.show(((Activity) context).getFragmentManager(),"show dialog");
//                                          findSalonBottomSheet mfindSalonBottomSheet = new findSalonBottomSheet();
//                                          mfindSalonBottomSheet.setArguments(bundle);
//                                          mfindSalonBottomSheet.show(fragmentActivity.getSupportFragmentManager(),"hey");
                                          Toast.makeText(context, "prem key = "+salonFindKey, Toast.LENGTH_SHORT).show();
                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  });
                        }
                        else if (listData.get(holder.getAdapterPosition()).getSalon_type().equals("regular")){
                            Query regquery =  FirebaseDatabase.getInstance().getReference("users").child("salon").child("r")
                                    .orderByChild("phone_no").equalTo(listData.get(holder.getAdapterPosition()).getPhone_no());
                                    regquery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot child : dataSnapshot.getChildren())
                                                salonFindKey = child.getKey();
                                            salonFindtype = "r";

                                            Bundle bundle = new Bundle();
                                            bundle.putString("title",listData.get(holder.getAdapterPosition()).getName());
                                            bundle.putString("image",listData.get(holder.getAdapterPosition()).getImageUrl());
                                            Toast.makeText(context, "image1 = "+listData.get(holder.getAdapterPosition()).getImageUrl(), Toast.LENGTH_SHORT).show();
                                            bundle.putString("key",salonFindKey);
                                            bundle.putString("type",salonFindtype);
                                            Activity activity = (Activity) context;
                                            android.app.FragmentManager fragmentManager = activity.getFragmentManager();
                                            final findSalonInfoFragment mfindSalonInfoFragment = new findSalonInfoFragment();
                                            mfindSalonInfoFragment.setArguments(bundle);
                                            mfindSalonInfoFragment.show(fragmentManager,"show dialog");
//                                            DisplayMetrics displayMetrics = new DisplayMetrics();
//                                            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                                            int displaywidth = displayMetrics.widthPixels;
//                                            int displayheight = displayMetrics.heightPixels;
//                                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//                                            layoutParams.copyFrom(mfindSalonInfoFragment.getW)



//                                            FragmentActivity fragmentActivity = (FragmentActivity) context;
//                                            findSalonBottomSheet mfindSalonBottomSheet = new findSalonBottomSheet();
//                                            mfindSalonBottomSheet.setArguments(bundle);
//                                            mfindSalonBottomSheet.show(fragmentActivity.getSupportFragmentManager(),"hey");
                                            //loadAllServices(salonFindtype,salonFindKey);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });


        return holder;
    }

    private void loadAllServices(String type,String key) {

    }


    @Override
    public void onBindViewHolder(findSalonViewHolder holder, final int position) {

        holder.findSalonName.setText(listData.get(position).getName());
        holder.findSaloAddress.setText(listData.get(position).getAddress());
        holder.salonType.setText(listData.get(position).getSalon_type());
        holder.findSalonPhoneNumber.setText(listData.get(position).getPhone_no());
        Glide.with(context).load(listData.get(position).getImageUrl()).into(holder.findSalonImage);



    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}

