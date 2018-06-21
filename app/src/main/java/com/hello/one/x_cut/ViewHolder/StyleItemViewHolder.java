package com.hello.one.x_cut.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;

/**
 * Created by one on 3/12/2018.
 */

public class StyleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView styleItemTextName;
    public ImageView styleItemImage;
    public ProgressBar styleItemDetailProgressBar;
    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StyleItemViewHolder(View itemView) {
        super(itemView);

        styleItemTextName = (TextView) itemView.findViewById(R.id.styleItemName);
        styleItemImage = (ImageView) itemView.findViewById(R.id.styleItemImage);
        styleItemDetailProgressBar = (ProgressBar) itemView.findViewById(R.id.styleItemDetailProgressBar);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}
