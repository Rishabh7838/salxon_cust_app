package com.hello.one.x_cut;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hello.one.x_cut.Interface.ItemClickListener;
import com.hello.one.x_cut.Model.Category;
import com.hello.one.x_cut.Model.Style_items;
import com.hello.one.x_cut.ViewHolder.StyleItemViewHolder;
//import com.hello.one.x_cut.ViewHolder.styleAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class styleDetails extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference styleItemList;
    private RecyclerView recyclerViewStyle;
    private String categoryId = "";

    private FirebaseRecyclerAdapter<Style_items,StyleItemViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    //search functionality here
    private FirebaseRecyclerAdapter<Style_items,StyleItemViewHolder> searchadapter;
    List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_details);

        database = FirebaseDatabase.getInstance();
        styleItemList = database.getReference("Styledetails");

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.styleSearchBar);

        recyclerViewStyle = (RecyclerView) findViewById(R.id.recycler_styleDetails);
        recyclerViewStyle.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewStyle.setLayoutManager(layoutManager);

        //Get intent here

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty() && categoryId != null) {


                loadStyleItemMenu(categoryId);
            }
        }

        //Searching
        materialSearchBar.setHint("Search your service");
        //materialSearchBar.setSpeechMode();
        loadSuggestList();//function to get suggested search list from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<String>();
                for(String search :suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed restored orignal suggest adapter

                if(!enabled)
                {
                    recyclerViewStyle.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //when search finishes show  result of search adapter

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }

    private void startSearch(CharSequence text) {
        searchadapter = new FirebaseRecyclerAdapter<Style_items,StyleItemViewHolder>(Style_items.class,R.layout.style_item_detail,
                StyleItemViewHolder.class,
                styleItemList.orderByChild("Name").equalTo(text.toString())) {


            @Override
            protected void populateViewHolder(StyleItemViewHolder viewHolder, Style_items model, int position) {

                Log.e("loadSytem","Error 3");
                Toast.makeText(styleDetails.this, "populaatemenu = "+model.getName(), Toast.LENGTH_SHORT).show();
                viewHolder.styleItemTextName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.styleItemImage);

                final Style_items click = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {

                        //start new activity

                        Intent styleCart = new Intent(styleDetails.this,StyleDetailCart.class);
                        styleCart.putExtra("StyleId",searchadapter.getRef(position).getKey());
                        startActivity(styleCart);


                    }
                });
            }
        };
        recyclerViewStyle.setAdapter(searchadapter);
    }

    private void loadSuggestList() {
        styleItemList.orderByChild("ItemId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapShot : dataSnapshot.getChildren())
                {
                    Style_items style_items = postSnapShot.getValue(Style_items.class);
                    suggestList.add(style_items.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadStyleItemMenu(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Style_items,StyleItemViewHolder>(Style_items.class,R.layout.style_item_detail,
                StyleItemViewHolder.class,
                styleItemList.orderByChild("ItemId").equalTo(categoryId)) {


            @Override
            protected void populateViewHolder(final StyleItemViewHolder viewHolder, Style_items model, int position) {

                Log.e("loadSytem","Error 3");
                Toast.makeText(styleDetails.this, "populaatemenu = "+model.getName(), Toast.LENGTH_SHORT).show();
                viewHolder.styleItemTextName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.styleItemImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolder.styleItemDetailProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

                final Style_items click = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void Onclick(View view, int position, boolean isLongClick) {

                        //start new activity

                        Intent styleCart = new Intent(styleDetails.this,StyleDetailCart.class);
                        styleCart.putExtra("StyleId",adapter.getRef(position).getKey());
                        startActivity(styleCart);


                    }
                });
            }
        };

        recyclerViewStyle.setAdapter(adapter);
    }
}