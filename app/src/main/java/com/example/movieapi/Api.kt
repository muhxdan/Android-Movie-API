package com.example.movieapi

import com.example.movieapi.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {
    @GET("popular?api_key=4393638fcade1a550e870da3fb7f9937&language=en-US")
    fun getMovie(
        @QueryMap parameters: HashMap<String, String>
    ) : Call<MovieResponse>
}