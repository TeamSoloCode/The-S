package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.MessageChats;
import com.example.bruce.myapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by anh khoa on 12/23/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    public static final int SENT =1 ;
    public static final int RECEIVE =2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    ArrayList<MessageChats> listMessage;
    Context context;
    boolean me = true;

    public ChatMessageAdapter(ArrayList<MessageChats> listMessage, Context context) {
        this.listMessage = listMessage;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row ;
        if(viewType==SENT){
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            return new ViewHolder(row);
        }
        else if(viewType==RECEIVE){
            row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
            return new ViewHolder(row);
        }else{
            return null;
        }

    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        boolean msgIsMe=listMessage.get(position).isIsme();
        me=msgIsMe;
        holder.txtten.setText(listMessage.get(position).getUser());
        holder.txtMsgContent.setText(listMessage.get(position).getContent());
        holder.txtMsgTime.setText(listMessage.get(position).getTime());
        Picasso.with(context).load(listMessage.get(position).ImageUser).transform(new CircleTransform()).into(holder.imageUser);

    }

    @Override
    public int getItemViewType(int position) {
        String checkuser= listMessage.get(position).getUserid();
        if(user.getUid().equals(checkuser)){
            return SENT;
        }else{
            return RECEIVE;
        }

    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtMsgContent,txtMsgTime,txtten;
        ImageView imageUser;
        public ViewHolder(View itemView) {
            super(itemView);
            txtten=itemView.findViewById(R.id.txtten);
            txtMsgContent=itemView.findViewById(R.id.msgContent);
            txtMsgTime=itemView.findViewById(R.id.msgTime);
            imageUser=itemView.findViewById(R.id.imgCircleUser);
        }
    }




    public void add(MessageChats message) {
        listMessage.add(message);
    }

    public void add(List<MessageChats> messages) {
        listMessage.addAll(messages);
    }

    public void clear() {
        int size = this.listMessage.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.listMessage.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
}
