package com.example.melichallenge.model

data class ProductDetailResponse(
    val id: String,
    val name: String,
    val permalink: String?,
    val pictures: List<ProductPicture>?,
    val short_description: ShortDescription?,
    val main_features: List<MainFeature>?,
    val attributes: List<ProductAttribute>?
)

data class ShortDescription(
    val type: String?,
    val content: String?
)

data class MainFeature(
    val text: String?,
    val type: String?
)

data class ProductAttribute(
    val id: String?,
    val name: String?,
    val value_name: String?
)

