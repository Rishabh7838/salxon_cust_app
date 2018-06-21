package com.hello.one.x_cut.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;

/**
 * Created by one on 5/5/2018.
 */

public class ParticularServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView particularServiceName;
    public ImageView particularServiceImage,particularServiceArrow;
    public RecyclerView haircutRV;


    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ParticularServiceViewHolder(View itemView) {
        super(itemView);

        particularServiceName = (TextView) itemView.findViewById(R.id.particularServiceName);
//        particularServiceDescription = (TextView) itemView.findViewById(R.id.particularServiceDescription);
        particularServiceImage = (ImageView) itemView.findViewById(R.id.particularServiceImage);
        particularServiceArrow = (ImageView) itemView.findViewById(R.id.particularServiceArrow);
        haircutRV = (RecyclerView) itemView.findViewById(R.id.hairCutRV);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }

}
