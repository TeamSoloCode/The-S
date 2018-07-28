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
import com.example.bruce.myapp.Data.InvitersInfo;
import com.example.bruce.myapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 6/23/2018.
 */

public class ListInvitationAdapter extends RecyclerView.Adapter<ListInvitationAdapter.ViewHolder> {
    ArrayList<InvitersInfo> listInviter =new ArrayList<>();
    Context context;
    RecyclerViewClicklistener itemClickListener;
    public ListInvitationAdapter(ArrayList<InvitersInfo> listInviter, Context context) {
        this.listInviter = listInviter;
        this.context = context;
    }

    @Override
    public ListInvitationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View iteamView=layoutInflater.inflate(R.layout.item_inviter_infomation,parent,false);
        return new ListInvitationAdapter.ViewHolder(iteamView);
    }

    @Override
    public void onBindViewHolder(ListInvitationAdapter.ViewHolder holder, int position) {
        InvitersInfo inviter = listInviter.get(position);
        Picasso.with(context).load(inviter.getInvitersPhotoURL()).transform(new CircleTransform()).into(holder.imgTeamUser);
        holder.txtTeamNameUser.setText(inviter.getInvitersName());
        holder.txtTeamsName.setText(inviter.getTeamsName());

//        FirebaseDatabase.getInstance().getReference("TeamUser").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
//                    holder.moveMember.setVisibility(View.VISIBLE);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return listInviter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgTeamUser,imgAccept;
        TextView txtTeamNameUser,txtTeamEmailUser,txtTeamsName;
        public ViewHolder(View itemView) {
            super(itemView);
            imgTeamUser=itemView.findViewById(R.id.imgTeamUser);
            txtTeamNameUser=itemView.findViewById(R.id.txtTeamNameUser);
            txtTeamsName=itemView.findViewById(R.id.txtTeamsName);
            imgAccept=itemView.findViewById(R.id.imgAccept);
            imgAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickMember(v,listInviter.get(getPosition()).getTeamId());
            }
        }
    }

    public void setClickListener(ListInvitationAdapter.RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface RecyclerViewClicklistener {
        void itemClickMember(View view, String teamId);
    }
}
