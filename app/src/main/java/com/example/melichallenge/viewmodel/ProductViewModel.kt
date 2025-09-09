package com.example.melichallenge.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.UiState
import com.example.melichallenge.model.Product
import com.example.melichallenge.service.ProductRepository
import com.example.melichallenge.service.SearchPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    open val state: StateFlow<UiState<List<Product>>> = _state
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun search(query: String, token: String, siteId: String = "MLA", status: String = "active") {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val result = repository.searchProducts(token, status, siteId, query)

            result
                .onSuccess { response ->
                    _state.value = UiState.Success(response.results)
                }
                .onFailure { e ->
                    _state.value = UiState.Error(
                        e.message ?: "Error desconocido al buscar productos"
                    )
                }
        }
    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun saveSearch(context: Context, query: String) {
        viewModelScope.launch {
            SearchPreferences.saveSearch(context, query)
        }
    }

    fun fetchSuggestions(context: Context, query: String) {
        viewModelScope.launch {
            val lastSearches = SearchPreferences.getLastSearches(context)
                .first()
            _suggestions.value = if (query.isBlank()) {
                lastSearches
            } else {
                lastSearches.filter { it.contains(query, ignoreCase = true) }
            }
        }
    }
}

