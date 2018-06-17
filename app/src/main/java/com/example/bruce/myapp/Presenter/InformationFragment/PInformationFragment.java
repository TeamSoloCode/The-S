package com.example.bruce.myapp.Presenter.InformationFragment;

import com.example.bruce.myapp.Data.TouristLocationDetail;
import com.example.bruce.myapp.Model.MInformation_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.IViewInformationFragment;

import java.util.ArrayList;

/**
 * Created by BRUCE on 11/25/2017.
 */

public class PInformationFragment implements IInformationFragment  {

    private IViewInformationFragment callbackToView;
    private MInformation_Fragment model = new MInformation_Fragment(this);

    public PInformationFragment(IViewInformationFragment callbackToView) {
        this.callbackToView = callbackToView;
    }

    public void receivedGetTouristLocationById(String location_ID){
        model.getTouristLocationById(location_ID);
    }

    @Override
    public void getTouristLocationById(ArrayList<TouristLocationDetail> detail, int resultCode, String resultMessage) {
        callbackToView.getTouristLocationById(detail, resultCode, resultMessage);
    }
}
