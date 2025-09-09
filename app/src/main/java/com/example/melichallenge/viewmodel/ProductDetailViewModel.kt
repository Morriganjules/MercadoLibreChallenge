package com.example.melichallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.UiState
import com.example.melichallenge.model.ProductDetailResponse
import com.example.melichallenge.service.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<ProductDetailResponse>>(UiState.Idle)
    val state: StateFlow<UiState<ProductDetailResponse>> = _state


    fun loadProduct(productId: String, token: String) {
        viewModelScope.launch {
            _state.value = UiState.Loading
            val result = repository.getProductDetail(productId, token)
            result
                .onSuccess { response ->
                    _state.value = UiState.Success(response)
                }
                .onFailure { e ->
                    _state.value = UiState.Error(
                        e.message ?: "Error desconocido al buscar producto"
                    )
                }
        }
    }

}

