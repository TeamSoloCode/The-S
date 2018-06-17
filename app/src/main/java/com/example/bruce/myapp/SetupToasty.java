package com.example.bruce.myapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import es.dmoral.toasty.Toasty;

/**
 * Created by BRUCE on 12/3/2017.
 */

public class SetupToasty {

    Context context;

    public SetupToasty(Context context) {
        this.context = context;
    }

    public void setupToasty(Context context){
        context = this.context;
        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(context,R.color.error))
                .setWarningColor(ContextCompat.getColor(context,R.color.warning))
                .setInfoColor(ContextCompat.getColor(context,R.color.information))
                .setSuccessColor(ContextCompat.getColor(context,R.color.success))
                .tintIcon(true)
                .apply();

    }
}
