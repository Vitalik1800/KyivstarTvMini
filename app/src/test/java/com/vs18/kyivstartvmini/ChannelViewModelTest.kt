package com.vs18.kyivstartvmini

import com.vs18.kyivstartvmini.fake.FakeChannelRepository
import com.vs18.kyivstartvmini.viewmodel.ChannelState
import com.vs18.kyivstartvmini.viewmodel.ChannelViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChannelViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun `state goes from Loading to Success`() = runTest {
        val viewModel = ChannelViewModel(
            repository = FakeChannelRepository()
        )

        assertTrue(viewModel.state is ChannelState.Loading)

        advanceUntilIdle()

        assertTrue(viewModel.state is ChannelState.Success)
    }
}