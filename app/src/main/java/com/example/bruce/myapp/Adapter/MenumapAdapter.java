package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 5/12/2017.
 */

public class MenumapAdapter extends RecyclerView.Adapter<MenumapAdapter.ViewHolder> {

    ArrayList<TouristLocation> touristLocations;
    RecyclerViewClicklistener clicklistener;
    Context context;

    public MenumapAdapter(ArrayList<TouristLocation> touristLocations, Context context) {
        this.touristLocations = touristLocations;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.adapter_locations_info,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final TouristLocation touristlocation = touristLocations.get(position);
        holder.txtAddress.setText(touristlocation.getAddress());
        holder.locationName.setText(touristlocation.getName());
        holder.ratingBar.setRating(touristlocation.getStars());
        Picasso.with(context).load(touristlocation.getImage()).resize(200,200).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return touristLocations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtAddress,locationName;
        ProgressBar progressBar;
        RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ratingBar =  itemView.findViewById(R.id.ratingStar);
            locationName = itemView.findViewById(R.id.txtLocationName);
            imageView =  itemView.findViewById(R.id.imageViewAdapter);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            progressBar=  itemView.findViewById(R.id.progress_bar_download);
        }
        @Override
        public void onClick(View v) {
            if(clicklistener != null){
                clicklistener.itemClick(v,touristLocations.get(getPosition()));
            }
        }
    }

    public void setClickListener(RecyclerViewClicklistener clickListener){
        this.clicklistener = clickListener;
    }

    public interface RecyclerViewClicklistener {
         void itemClick(View view, TouristLocation tl);
    }
}

//    @NonNull Activity context;
//    @LayoutRes int resource;
//    @NonNull List<Constructor_Menumap> objects;
//
//    public MenumapAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Constructor_Menumap> objects) {
//        super(context, resource, objects);
//        this.context=context;
//        this.resource=resource;
//        this.objects=objects;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater layoutInflater=this.context.getLayoutInflater();
//        View row=layoutInflater.inflate(this.resource,null);
//
//        TextView txtLocationName= (TextView) row.findViewById(R.id.txtLocationName);
//        ImageView imageView = (ImageView) row.findViewById(R.id.imageViewAdapter);
//        TextView txtAddress= (TextView) row.findViewById(R.id.txtAddress);
//
//        Constructor_Menumap menu=this.objects.get(position);
//        txtAddress.setText(menu.Address);
//        txtLocationName.setText(menu.LocationName);
//
//
//        Picasso.with(getContext()).load(menu.LocationImg).into(imageView);
//
//        return row;
//    }

    //    Context myContext;
//    int myLayout;
//    List<Constructor_Menumap> myList;
//
//    public MenumapAdapter(Context myContext, int myLayout, List<Constructor_Menumap> myList) {
//        this.myContext = myContext;
//        this.myLayout = myLayout;
//        this.myList = myList;
//    }
//
//    @Override
//    public int getCount() {
//        return myList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        convertView=inflater.inflate(myLayout,null);
//
//
//
//        TextView txtLocationName= (TextView) convertView.findViewById(R.id.txtLocationName);
//        txtLocationName.setText(myList.get(position).LocationName);
//
//        TextView txtAddress= (TextView) convertView.findViewById(R.id.txtAddress);
//        txtAddress.setText(myList.get(position).Address);
//
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewAdapter);
//        Picasso.with(myContext).load(myList.get(position).LocationImg).into(imageView);
//        return convertView;
//    }
