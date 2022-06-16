package com.example.notdeapp.API;

import com.example.notdeapp.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRequestData {

    @GET("note_crud_api.php")
    Call<ResponseModel> getData();

    @FormUrlEncoded
    @POST("note_crud_api.php")
    Call<ResponseModel> insertData(
            @Field("judul") String judul,
            @Field("deskripsi") String deskripsi,
            @Field("isi") String isi
    );

    @FormUrlEncoded
    @DELETE("note_crud_api.php")
    Call<ResponseModel> deleteData(
            @Field("id") int id
    );
}
