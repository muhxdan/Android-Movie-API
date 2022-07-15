package com.example.movieapi.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: ArrayList<Movie>
)
