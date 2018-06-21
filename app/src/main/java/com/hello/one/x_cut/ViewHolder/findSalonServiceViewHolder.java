package com.hello.one.x_cut.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Salon_Service;
import com.hello.one.x_cut.Model.findSalonLayout;
import com.hello.one.x_cut.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by one on 4/14/2018.
 */


public class findSalonServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CheckBox service;
    public TextView servicePrice;
    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

//    public void setCartName(TextView cartName) {
//
//    }

    public findSalonServiceViewHolder(View itemView) {
        super(itemView);

        service = (CheckBox) itemView.findViewById(R.id.service_checkbox);
        servicePrice = (TextView) itemView.findViewById(R.id.servicePrice);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}


//public class findSalonServiceAdapter extends RecyclerView.Adapter<findSalonServiceViewHolder>{
//
//    private List<Salon_Service> listData= new ArrayList<>();
//    private Context context;
//
//    public findSalonServiceAdapter(List<Salon_Service> listData, Context context) {
//        this.listData = listData;
//        this.context = context;
//    }
//
//    @Override
//    public findSalonServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View itemView = inflater.inflate(R.layout.switch_actionbar,parent,false);
//        return new findSalonServiceViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(findSalonServiceViewHolder holder, int position) {
//        holder.service.setText(listData.get(position).getService_name());
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void Onclick(View view, int position, boolean isLongClick) {
//                Log.d("Onclick","cartAdapter");
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//}
//
