package com.hello.one.x_cut.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.R;

/**
 * Created by one on 3/17/2018.
 */

public class AdvanceBookingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView BookingName,BookingId,BookingStatus,BookingPhone,BookingAddress,BookingPrice;


    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public AdvanceBookingViewHolder(View itemView) {
        super(itemView);

        BookingName = (TextView) itemView.findViewById(R.id.BookingName);
        BookingId = (TextView) itemView.findViewById(R.id.BookingId);
        BookingStatus = (TextView) itemView.findViewById(R.id.BookingStatus);
        BookingPhone = (TextView) itemView.findViewById(R.id.BookingPhone);
        BookingAddress = (TextView) itemView.findViewById(R.id.BookingAddress);
        BookingPrice = (TextView) itemView.findViewById(R.id.BookingPrice);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}

