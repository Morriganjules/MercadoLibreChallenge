package com.example.melichallenge.model

data class ProductSearchResponse(
    val results: List<Product>
)

data class Product(
    val id: String,
    val domain_id: String?,
    val name: String?,
    val pictures: List<ProductPicture>?
)

data class ProductPicture(
    val id: String?,
    val url: String?
)


