package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/3/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<Tourist_Location> tourist_locations;
    Context context;
    RecyclerViewClicklistener clicklistener;

    public HistoryAdapter(ArrayList<Tourist_Location> tourist_locations, Context context) {
        this.tourist_locations = tourist_locations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View mView = layoutInflater.inflate(R.layout.adater_history,viewGroup,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Tourist_Location tl = tourist_locations.get(i);
        viewHolder.textView.setText(tl.LocationName);
        Picasso.with(context).load(tl.LocationImg).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {

        return tourist_locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            // imageView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.imageHistory);
            textView = itemView.findViewById(R.id.txtHistoryName);
        }

        @Override
        public void onClick(View v) {
            if(clicklistener != null){

                clicklistener.onClickItemRecyclerView(v,getPosition(),String.valueOf(tourist_locations.hashCode()));
            }
        }
    }
    public void setClickListenerRecyclerView(RecyclerViewClicklistener clickListener){
        this.clicklistener = clickListener;
    }

    public interface RecyclerViewClicklistener{

        void onClickItemRecyclerView(View view, int position, String listId);
    }
}
