package com.example.moviehub.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("results")
    val results: List<MovieResults>,
)