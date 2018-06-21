package com.hello.one.x_cut.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.MainActivity;
import com.hello.one.x_cut.Model.Bookings;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.R;
import com.hello.one.x_cut.styleDetails;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by one on 3/11/2018.
 */

public class StyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView styleTextName;
    public ImageView styleImage;
    public ProgressBar styleItemProgressBar;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StyleViewHolder(View itemView) {
        super(itemView);

        styleTextName = (TextView) itemView.findViewById(R.id.styleName);
        styleImage = (ImageView) itemView.findViewById(R.id.styleImage);
        styleItemProgressBar = (ProgressBar) itemView.findViewById(R.id.styleItemProgressBar);
        itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        itemClickListener.Onclick(v,getAdapterPosition(),false);
    }
}


//public class styleAdapter extends RecyclerView.Adapter<StyleViewHolder>{
//
//    private List<Category> listData= new ArrayList<>();
//    private Context context;
//
//    public styleAdapter(List<Category> listData, Context context) {
//        this.listData = listData;
//        this.context = context;
//
//    }
//
//
//    @Override
//    public StyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View itemView = inflater.inflate(R.layout.style_item,parent,false);
//        return new StyleViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(StyleViewHolder holder, int position) {
//        final Category item = listData.get(position);
//        final Uri uri = Uri.parse(item.getImage());
//
//        //YoYo.with(Techniques.Bounce).playOn(holder.cardView);
//
//        holder.styleTextName.setText(holder.styleTextName.getText()+item.getName());
//        holder.styleImage.setImageURI(uri);
//        //holder.styleImage.setText(holder.desc.getText()+item.getImage());
//
//
//        //holder.Iv.setImageResource(item.getImg());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//
//}

