package com.example.melichallenge

import com.example.melichallenge.service.MercadoLibreApi
import com.example.melichallenge.service.ProductRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: MercadoLibreApi
    private lateinit var repository: ProductRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(MercadoLibreApi::class.java)
        repository = ProductRepository(api)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `cuando la respuesta es exitosa devuelve Result success`() = runTest {
        val mockResponse = """
            {
              "results": [
                {"id": "123", "name": "Producto Test", "domain_id": "MLA-TODO"}
              ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val result = repository.searchProducts("token", "active", "MLA", "celular")

        assertTrue(result.isSuccess)
        assertEquals("123", result.getOrThrow().results.first().id)
    }

    @Test
    fun `cuando la respuesta es vacía devuelve failure`() = runTest {
        val mockResponse = """{"results": []}"""

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val result = repository.searchProducts("token", "active", "MLA", "nada")

        assertTrue(result.isFailure)
    }

    @Test
    fun `getProductDetail devuelve Result success cuando API responde correctamente`() = runTest {
        val productId = "MLA14719808"
        val token = "dummy_token"

        val mockResponse = """
            {
                "id": "$productId",
                "name": "iPad Test",
                "short_description": {
                    "type": "plaintext",
                    "content": "Descripción corta"
                },
                "pictures": [
                    {"id": "1", "url": "https://example.com/img1.jpg"}
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        val result = repository.getProductDetail(productId, token)

        assertTrue(result.isSuccess)
        val product = result.getOrThrow()
        assertEquals(productId, product.id)
        assertEquals("iPad Test", product.name)
        assertEquals(1, product.pictures?.size)
        assertEquals("https://example.com/img1.jpg", product.pictures?.first()?.url)
    }

    @Test
    fun `getProductDetail devuelve Result failure cuando API falla`() = runTest {
        val productId = "MLA14719808"
        val token = "dummy_token"

        // Simulamos un error del servidor
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val result = repository.getProductDetail(productId, token)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}
