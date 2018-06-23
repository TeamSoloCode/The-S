package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 29/11/2017.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder>  {
    ArrayList<UserProfile> listUser=new ArrayList<>();
    Context context;
    RecyclerViewClicklistener itemClickListener;

    public TeamAdapter(ArrayList<UserProfile> listUser, Context context) {
        this.listUser = listUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View iteamView=layoutInflater.inflate(R.layout.item_team_member,parent,false);
        return new ViewHolder(iteamView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserProfile userProfile=listUser.get(position);
        Picasso.with(context).load(userProfile.Image).transform(new CircleTransform()).into(holder.imgTeamUser);
        holder.txtTeamNameUser.setText(userProfile.Name);
        holder.txtTeamEmailUser.setText(userProfile.Email);
        holder.txtTeamPhoneUser.setText(userProfile.Phone);
        if(userProfile.Gender ==true)
        {
            holder.txtTeamGendre.setText("Male");
        } else {
            holder.txtTeamGendre.setText("Female");
        }
        FirebaseDatabase.getInstance().getReference("TeamUser").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    holder.moveMember.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        ImageView imgTeamUser;
        ImageButton moveMember;
        TextView txtTeamNameUser,txtTeamEmailUser,txtTeamPhoneUser,txtTeamGendre;
        public ViewHolder(View itemView) {
            super(itemView);
            imgTeamUser=itemView.findViewById(R.id.imgTeamUser);
            txtTeamNameUser=itemView.findViewById(R.id.txtTeamNameUser);
            txtTeamEmailUser=itemView.findViewById(R.id.txtTeamEmailUser);
            txtTeamPhoneUser=itemView.findViewById(R.id.txtTeamPhoneUser);
            txtTeamGendre=itemView.findViewById(R.id.txtTeamGendre);
            moveMember=itemView.findViewById(R.id.moveMember);
            moveMember.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickMember(v,getPosition());
            }
            return true;
        }
    }
    public void setClickListener(RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface RecyclerViewClicklistener {
         void itemClickMember(View view, int position);
    }
}
