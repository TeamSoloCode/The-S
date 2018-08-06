package com.example.bruce.myapp.View.Information_Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.InfoAdapter;
import com.example.bruce.myapp.Data.TouristLocation;
import com.example.bruce.myapp.Data.TouristLocationDetail;
import com.example.bruce.myapp.Presenter.InformationFragment.PInformationFragment;
import com.example.bruce.myapp.R;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/30/2017.
 */

public class Information_Fragment extends android.support.v4.app.Fragment implements IViewInformationFragment {

    View mView;


    ListView listView;
    ArrayList<TouristLocation> tls;
    InfoAdapter moreInfoAdapter;

    //MVP
    PInformationFragment pInformationFragment = new PInformationFragment(this);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mView =  inflater.inflate(R.layout.fragment_information,container,false);
        initialize(mView);

        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tls = getActivity().getIntent().getParcelableArrayListExtra("tourist_location");
        pInformationFragment.receivedGetTouristLocationById(tls.get(0).getLocationId());
    }

    private void initialize(View mView){
        listView = mView.findViewById(R.id.infoListView);
    }


    @Override
    public void getTouristLocationById(ArrayList<TouristLocationDetail> detail, int resultCode, String resultMessage) {
        //nếu không lấy dc dữ liệu hiện thông báo
        if(resultCode != 1){
            Toast.makeText(getContext(), resultMessage, Toast.LENGTH_SHORT);
            return;
        }
        //set up listview
        moreInfoAdapter = new InfoAdapter(getActivity(), R.layout.item_information, detail);
        listView.setAdapter(moreInfoAdapter);
    }
}
