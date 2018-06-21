package com.hello.one.x_cut.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;

/**
 * Created by one on 5/10/2018.
 */

public class HaircutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView haircutServiceName;
    public TextView complementryServices;
    public ElegantNumberButton addServiceButton;
    public TextView serviceStatus;
    public CardView Background_card;
    //public ImageView particularServiceImage;



    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HaircutViewHolder(View itemView) {
        super(itemView);

        haircutServiceName = (TextView) itemView.findViewById(R.id.haircutTV);
        complementryServices = (TextView) itemView.findViewById(R.id.complementryServices);
        addServiceButton = (ElegantNumberButton) itemView.findViewById(R.id.addServiceNumber_btn);
        serviceStatus = (TextView) itemView.findViewById(R.id.serviceStatusTV);
        Background_card = (CardView) itemView.findViewById(R.id.particularServiceCardView);

//        particularServiceDescription = (TextView) itemView.findViewById(R.id.particularServiceDescription);
        //particularServiceImage = (ImageView) itemView.findViewById(R.id.particularServiceImage);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }

}
