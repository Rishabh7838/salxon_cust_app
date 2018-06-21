package com.hello.one.x_cut.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.R;
import com.hello.one.x_cut.cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by one on 3/15/2018.
 */

class cartViewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView cartName;
    public TextView cartPrice;
    public ElegantNumberButton serviceQuantity;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setCartName(TextView cartName) {
        this.cartName = cartName;
    }

    public cartViewholder(View itemView) {
        super(itemView);

        cartName = (TextView) itemView.findViewById(R.id.cartItemName);
        cartPrice = (TextView) itemView.findViewById(R.id.cartItemDetails);
        serviceQuantity = (ElegantNumberButton) itemView.findViewById(R.id.addServiceNumber_btn_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}

public class cartAdapter extends RecyclerView.Adapter<cartViewholder>{

    private List<Bookings> listData= new ArrayList<>();
    private Context context;

    public cartAdapter(List<Bookings> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public cartViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cartlayout,parent,false);
        return new cartViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(cartViewholder holder, int position) {

//        TextDrawable textDrawable = TextDrawable.builder()
//                .buildRound(""+listData.get(position).getTimeStamp(), Color.RED);
//        holder.img_cart_count.setImageDrawable(textDrawable);
        holder.serviceQuantity.setNumber(listData.get(position).getQuantity());
        Locale locale =new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        holder.cartPrice.setText(fmt.format(Integer.parseInt(listData.get(position).getPrice())));
        holder.cartName.setText(listData.get(position).getService_Name());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void Onclick(View view, int position, boolean isLongClick) {
                Log.d("Onclick","cartAdapter");
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

}
