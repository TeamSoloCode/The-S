package com.example.bruce.myapp.View.BigMap;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.myapp.R;

/**
 * Created by BRUCE on 11/22/2017.
 */

public class MenuBigMapFragment extends Fragment{

    View mView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = (View) inflater.inflate(R.layout.fragment_menu_bigmap,container,false);

        return mView;
    }
}
