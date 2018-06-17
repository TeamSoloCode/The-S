package com.example.bruce.myapp.View.MenuFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Login.LoginActivity;

public class MenuFragment extends Fragment implements IViewMenuFragment {

    ViewGroup mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mView = (ViewGroup) inflater.inflate(R.layout.fragment_menu,container,false);

        return mView;
    }

    @Override
    public void LogoutSuccess() {

        Intent target = new Intent(getActivity(), LoginActivity.class);
        getActivity().finish();
        startActivity(target);
    }
}
