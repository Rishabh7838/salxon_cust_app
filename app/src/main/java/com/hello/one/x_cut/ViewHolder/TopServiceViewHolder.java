package com.hello.one.x_cut.ViewHolder;

/**
 * Created by one on 5/5/2018.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;
public class TopServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
public TextView topServiceName,topServiceDescription;
public ImageView topServiceImage,topServiceArrow;
        public RecyclerView particularServiveRecyclerView;



public ItemClickListener itemClickListener;

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        }

public TopServiceViewHolder(View itemView) {
        super(itemView);

        topServiceName = (TextView) itemView.findViewById(R.id.topServiceName);
        topServiceDescription = (TextView) itemView.findViewById(R.id.topServiceDescription);
        topServiceImage = (ImageView) itemView.findViewById(R.id.top_Service_Image);
        topServiceArrow = (ImageView) itemView.findViewById(R.id.topServiceArrow);
        particularServiveRecyclerView = (RecyclerView) itemView.findViewById(R.id.particularTypeServices);
        itemView.setOnClickListener(this);
        }

@Override
public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
        }

}
