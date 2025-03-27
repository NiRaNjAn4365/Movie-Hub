package com.example.moviehub.models

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse
    (
            val page:Int,
            val results:List<MovieResults>,
            val total_pages: Int,
            val total_results: Int
)
{
}