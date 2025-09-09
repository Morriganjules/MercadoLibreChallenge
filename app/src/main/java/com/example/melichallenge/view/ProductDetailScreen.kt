package com.example.melichallenge.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.melichallenge.UiState
import com.example.melichallenge.viewmodel.ProductDetailViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(
            productId,
            token = "APP_USR-8717828415401413-090814-7ec8f6d8f5489715e816f666aa44fbc2-2674758223"
        )
    }

    when (state) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {
            val product = (state as UiState.Success).data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = product.name ?: "Producto sin título",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                val pictures = product.pictures ?: emptyList()
                if (pictures.isNotEmpty()) {
                    val pagerState = rememberPagerState()
                    HorizontalPager(
                        count = pictures.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) { page ->
                        val url = pictures[page].url ?: pictures[page].url
                        if (!url.isNullOrEmpty()) {
                            AsyncImage(
                                model = url,
                                contentDescription = product.name,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Sin imagen")
                            }
                        }
                    }
                    if (pictures.size > 1) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            activeColor = MaterialTheme.colorScheme.primary,
                            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                } else {
                    Text("No hay imágenes disponibles", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                val description = product.short_description?.content?.takeIf { it.isNotBlank() }
                    ?: "No hay descripción disponible."
                Text("Descripción:", style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                val features = product.main_features ?: emptyList()
                if (features.isNotEmpty()) {
                    Text(
                        "Características principales:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    features.forEach { feature ->
                        val text = feature.text?.takeIf { it.isNotBlank() } ?: return@forEach
                        Text("• $text", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                val attributes = product.attributes ?: emptyList()
                if (attributes.isNotEmpty()) {
                    Text("Atributos:", style = MaterialTheme.typography.titleMedium)
                    attributes.forEach { attr ->
                        val name = attr.name ?: "Atributo"
                        val value = attr.value_name ?: "No disponible"
                        Text("• $name: $value", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error al cargar el producto: ${(state as UiState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else -> {}
    }
}


