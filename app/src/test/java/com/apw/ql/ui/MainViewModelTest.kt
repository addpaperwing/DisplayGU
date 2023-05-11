package com.apw.ql.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apw.ql.MainDispatcherRule
import com.apw.ql.data.model.Repo
import com.apw.ql.data.model.User
import com.apw.ql.data.remote.State
import com.apw.ql.getOrAwaitValue
import com.apw.ql.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
internal class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val httpException = HttpException(Response.error<ResponseBody>(404, "No repo found".toResponseBody("plain/text".toMediaTypeOrNull())))
    private val ioException = IOException()
    private val repo = Repo(
        name = "repo1",
        owner = User("user1"),
        htmlUrl = "",
        topics = emptyList(),
        stargazersCount = 0,
        updateAt = "")

    private lateinit var fakeRepository: FakeMainRepository
    private lateinit var mainViewModel: MainViewModel
    @Before
    fun setup() {
        fakeRepository = FakeMainRepository(
            ioException, httpException, repo
        )
        mainViewModel = MainViewModel(fakeRepository)
    }
    @Test
    fun getData_fetchDataFromServerAndUpdateLoadingState_successful() = runTest{
        mainViewModel.getData("repo1", null)
        advanceTimeBy(299)

        assertThat(mainViewModel.result.getOrAwaitValue(), (`is`(State.Loading)))

        advanceUntilIdle()

        val resultValue = mainViewModel.result.getOrAwaitValue()
        assertThat(resultValue, (`is`(State.Success(listOf(repo)))))
//        assertThat(resultValue?.first, `is`(user))
//        assertThat(resultValue?.second?.size, `is`(1))
//        assertThat(resultValue?.second?.get(0), `is`(repo))
//        assertThat(mainViewModel.taskState.getOrAwaitValue().status, (`is`(Status.SUCCESS)))
    }

    @Test
    fun getData_fetchDataFromServerAndUpdateLoadingState_retryAndFailed() = runTest{
        mainViewModel.getData("ioException", null)
        advanceTimeBy(299)

        assertThat(mainViewModel.result.getOrAwaitValue(), (`is`(State.Loading)))

        //Call delay for 300 milliseconds, retry default delay is 2000
        advanceTimeBy(6599)
        assertThat(mainViewModel.result.getOrAwaitValue(), (`is`(State.Loading)))

        advanceUntilIdle()

        val taskStateFailedValue = mainViewModel.result.getOrAwaitValue()
        assertThat(taskStateFailedValue, (`is`(State.Error(ioException))))
//        assertThat(taskStateFailedValue.exception, (`is`(exception)))
    }

    @Test
    fun getData_fetchDataFromServerAndUpdateLoadingState_failed() = runTest{
        mainViewModel.getData("", null)
        advanceTimeBy(299)

        assertThat(mainViewModel.result.getOrAwaitValue(), (`is`(State.Loading)))

        advanceUntilIdle()
        val taskStateFailedValue = mainViewModel.result.getOrAwaitValue()
        assertThat(taskStateFailedValue, (`is`(State.Error(httpException))))
//        assertThat(taskStateFailedValue.exception, (`is`(exception)))
    }
}