package com.example.bruce.myapp.Model;

import com.example.bruce.myapp.ApiClient;
import com.example.bruce.myapp.ApiGetObject.GetAllCommentOfLocation;
import com.example.bruce.myapp.ApiInterface;
import com.example.bruce.myapp.Presenter.CommentFragment.ICommentFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by BRUCE on 11/29/2017.
 */

public class MCommentFragment {

    ICommentFragment callback;

    public MCommentFragment(ICommentFragment callback) {
        this.callback = callback;
    }

    public void handleGetAllCommentOfLocation(String locationId, String userGetCommentId, String commentId){
        Retrofit retrofit = ApiClient.getApiClient();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        apiInterface.getAllCommentOfLocation(locationId, userGetCommentId, commentId).enqueue(new Callback<GetAllCommentOfLocation>() {
            @Override
            public void onResponse(Call<GetAllCommentOfLocation> call, retrofit2.Response<GetAllCommentOfLocation> response) {
                if(response.isSuccessful()){
                    //on mode get all
                    if(commentId == null){
                        callback.getAllCommentOfLocation(
                                response.body().getResultCode(),
                                response.body().getResultData(),
                                "");
                    }
                    //on mode get new
                    else {
                        callback.getNewCommentOfLocation(
                                response.body().getResultCode(),
                                response.body().getResultData(),
                                "");
                    }

                }
                else{
                    //only on mode get all
                    if(commentId == null) {
                        callback.getAllCommentOfLocation(99, null, "Không thể kết nối với máy chủ");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllCommentOfLocation> call, Throwable t) {
                callback.getAllCommentOfLocation(99, null,"Không thể kết nối với máy chủ");
            }
        });
    }

}
