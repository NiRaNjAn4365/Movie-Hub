package com.example.moviehub.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviehub.data.repository.Repository
import com.example.moviehub.models.MovieResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    val getAllMovies: Flow<PagingData<MovieResults>> = repository.getAllMovies().cachedIn(viewModelScope)
}