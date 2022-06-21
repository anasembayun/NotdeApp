package com.example.notdeapp.API;

import com.example.notdeapp.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @DELETE("note_crud_api.php")
    Call<ResponseModel> deleteData(
            @Query("id") int id
    );

    @GET("note_crud_api.php")
    Call<ResponseModel> getDataId(
            @Query("id") int id
    );

    @FormUrlEncoded
    @POST("note_crud_api.php")
    Call<ResponseModel> updateData(
            @Query("id") int id,
            @Field("judul") String judul,
            @Field("deskripsi") String deskripsi,
            @Field("isi") String isi
    );
}
