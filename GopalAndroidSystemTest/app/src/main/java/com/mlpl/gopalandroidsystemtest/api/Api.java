package com.mlpl.gopalandroidsystemtest.api;

import com.mlpl.gopalandroidsystemtest.api.model.PicsSum;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("v2/list")
    Call<List<PicsSum>> PICS_SUM_CALL(@Query("page") int page, @Query("limit") int limit);

}
