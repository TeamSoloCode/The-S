package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.TeamMember;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 29/11/2017.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder>  {
    ArrayList<TeamMember> listMember = new ArrayList<>();
    Context context;
    RecyclerViewClicklistener itemClickListener;
    SharedPreferences sharedPref;
    String leaderId = "";

    public TeamAdapter(ArrayList<TeamMember> listMember, Context context) {
        this.listMember = listMember;
        this.context = context;
        sharedPref = context.getSharedPreferences("my_data", MODE_PRIVATE);
        this.leaderId = sharedPref.getString("leaderId","");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View iteamView = layoutInflater.inflate(R.layout.item_team_member,parent,false);
        return new ViewHolder(iteamView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TeamMember teamMember = listMember.get(position);
        Picasso.with(context).load(teamMember.getImage()).transform(new CircleTransform()).into(holder.imgTeamUser);
        holder.txtTeamNameUser.setText(teamMember.getName());
        holder.txtTeamEmailUser.setText(teamMember.getEmail());
        holder.txtTeamPhoneUser.setText(teamMember.getPhone());
//        if(userProfile.Gender ==true)
//        {
//            holder.txtTeamGendre.setText("Male");
//        } else {
//            holder.txtTeamGendre.setText("Female");
//        }

        if(!this.leaderId.equals("")){
            holder.moveMember.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return listMember.size();
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
            moveMember=itemView.findViewById(R.id.moveMember);
            moveMember.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickMember(v,listMember.get(getPosition()));
            }
            return true;
        }
    }
    public void setClickListener(RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface RecyclerViewClicklistener {
         void itemClickMember(View view, TeamMember teamMember);
    }
}
