package com.hello.one.x_cut.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;

/**
 * Created by one on 3/27/2018.
 */

public class SalonservicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView serviceName,serviceDescription,serviceOffer;
    public ImageView serviceImage;
    public Button addServiceButton;
    public  Button serviceDetailButton;



    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SalonservicesViewHolder(View itemView) {
        super(itemView);

        serviceName = (TextView) itemView.findViewById(R.id.serviceName);
        serviceDescription = (TextView) itemView.findViewById(R.id.serviceDescription);
        serviceOffer = (TextView) itemView.findViewById(R.id.serviceOffers);
        serviceImage = (ImageView) itemView.findViewById(R.id.serviceImage);
        addServiceButton= (Button) itemView.findViewById(R.id.addServiceButton);
        serviceDetailButton = (Button) itemView.findViewById(R.id.ServiceDetailsButton);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}
