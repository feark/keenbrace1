package com.keenbrace.api;


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



    @FormUrlEncoded
    @POST("GetPdf")
    Observable<ResponseBody> getPDF(@Field("loginName") String loginName,
                                    @Field("ID") String id,
                                    @Field("token") String token,
                                    @Field("uPlatform") String uPlatform);


    @FormUrlEncoded
    @POST("GetPdf")
    Observable<ResponseBody> downLoadImage(@Field("loginName") String loginName,
                                           @Field("ID") String id,
                                           @Field("token") String token,
                                           @Field("uPlatform") String uPlatform);


}
