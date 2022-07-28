package com.example.uploadmultiplefiles.api;

import static com.example.uploadmultiplefiles.api.ApiUtilities.API_KEY;

import com.example.uploadmultiplefiles.Model.ImageModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface {

   @Headers("Authorization: Client-ID "+API_KEY)
   @GET("/photos")

    Call<List<ImageModel>>getImages(
            @Query("page") int page,
            @Query("per_page") int perpage
   );











}
