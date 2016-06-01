package com.keenbrace.api;


import com.keenbrace.bean.request.QueryRunRequest;
import com.keenbrace.bean.request.RunRequest;
import com.keenbrace.bean.response.GetVersionInfoResponse;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.bean.response.Result;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by  on 16/1/20.
 */
public interface BaseApi {


    @POST("GetVersionInfo")
    Observable<GetVersionInfoResponse> getVersionInfo();


    @FormUrlEncoded
    @POST("LoginServlet")
    Observable<LoginResponse> login(@Field("t") String t,
                                    @Field("loginName") String loginName,
                                    @Field("password") String token,
                                    @Field("userInfo") String userInfo);

    @POST("RunServlet")
    Observable<Result> putRunData(@Body RunRequest requst);

    @POST("QueryRunDataServlet")
    Observable<Result> queryRunData(@Body QueryRunRequest requst);

    @POST("QueryWaringsServlet")
    Observable<Result> queryWaringsServlet(@Field("runId") String id);
}
