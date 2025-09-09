package com.example.melichallenge

import com.example.melichallenge.model.Product
import com.example.melichallenge.model.ProductSearchResponse
import com.example.melichallenge.service.ProductRepository
import com.example.melichallenge.viewmodel.ProductViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: ProductRepository
    private lateinit var viewModel: ProductViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = ProductViewModel(repository)
    }

    @Test
    fun `cuando search es exitoso emite Success`() = runTest {
        val fakeProduct = Product("123", "Celular", "MLA-CELLPHONE", pictures = null)
        coEvery {
            repository.searchProducts(any(), any(), any(), any())
        } returns Result.success(ProductSearchResponse(listOf(fakeProduct)))

        viewModel.search("celular", "token")

        val state = viewModel.state.first { it is UiState.Success }
        assertTrue(state is UiState.Success)
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

