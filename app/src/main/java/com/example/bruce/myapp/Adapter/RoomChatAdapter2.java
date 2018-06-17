package com.example.bruce.myapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bruce.myapp.ActivityChat;
import com.example.bruce.myapp.Data.ChatRoom;
import com.example.bruce.myapp.R;

import java.util.List;

/**
 * Created by anh khoa on 12/24/2017.
 */

public class RoomChatAdapter2 extends ArrayAdapter<ChatRoom> {
    @NonNull Activity context;
    int resource;
    @NonNull List<ChatRoom> objects;
    public RoomChatAdapter2(@NonNull Activity context, int resource, @NonNull List<ChatRoom> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(R.layout.item_chatroom,null);
        TextView txttenphong=row.findViewById(R.id.txttenphong1);
        ChatRoom chatRoom=this.objects.get(position);
        txttenphong.setText(chatRoom.getTenRoom());
        return row;
    }
}
