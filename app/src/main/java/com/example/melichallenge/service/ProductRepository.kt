package com.example.melichallenge.service

import com.example.melichallenge.model.ProductDetailResponse
import com.example.melichallenge.model.ProductSearchResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val api: MercadoLibreApi
) {
    suspend fun searchProducts(
        accessToken: String,
        status: String,
        siteId: String,
        query: String
    ): Result<ProductSearchResponse> {
        return try {
            val response = api.searchProducts(
                authorization = "Bearer $accessToken",
                status = status,
                siteId = siteId,
                query = query
            )
            if (response.results.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se encontraron resultados"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getProductDetail(productId: String, token: String): Result<ProductDetailResponse> {
        return try {
            val response = api.getProductDetail("Bearer $token", productId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

