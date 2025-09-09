package com.example.melichallenge.service

import com.example.melichallenge.model.ProductDetailResponse
import com.example.melichallenge.model.ProductSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreApi {
    @GET("products/search")
    suspend fun searchProducts(
        @Header("Authorization") authorization: String,
        @Query("status") status: String,
        @Query("site_id") siteId: String,
        @Query("q") query: String
    ): ProductSearchResponse

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Header("Authorization") authorization: String,
        @Path("id") productId: String
    ): ProductDetailResponse
}
