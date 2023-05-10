package com.apw.ql.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apw.ql.MainDispatcherRule
import com.apw.ql.data.model.User
import com.apw.ql.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
internal class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val user = User("user1")
    private val exception = HttpException(Response.error<ResponseBody>(404, "User not found".toResponseBody("plain/text".toMediaTypeOrNull())))
//    private val repo = Repo("repo1", null, "", 0, 0)

    private lateinit var fakeRepository: FakeMainRepository
    private lateinit var mainViewModel: MainViewModel
    @Before
    fun setup() {
//        fakeRepository = FakeMainRepository(
//            user, exception, repo
//        )
        mainViewModel = MainViewModel(fakeRepository)
    }
    @Test
    fun getData_fetchDataFromServerAndUpdateLoadingState_successful() = runTest{
        mainViewModel.getData("user1")
        advanceTimeBy(299)

//        assertThat(mainViewModel.taskState.getOrAwaitValue().status, (`is`(Status.LOADING)))

        advanceUntilIdle()

        val resultValue = mainViewModel.result.getOrAwaitValue()
//        assertThat(resultValue?.first, `is`(user))
//        assertThat(resultValue?.second?.size, `is`(1))
//        assertThat(resultValue?.second?.get(0), `is`(repo))
//        assertThat(mainViewModel.taskState.getOrAwaitValue().status, (`is`(Status.SUCCESS)))
    }

    @Test
    fun getData_fetchDataFromServerAndUpdateLoadingState_failed() = runTest{
        mainViewModel.getData("")
        advanceTimeBy(299)

//        assertThat(mainViewModel.taskState.getOrAwaitValue().status, (`is`(Status.LOADING)))

        advanceUntilIdle()

//        val taskStateFailedValue = mainViewModel.taskState.getOrAwaitValue()
//        assertThat(taskStateFailedValue.status, (`is`(Status.FAILED)))
//        assertThat(taskStateFailedValue.exception, (`is`(exception)))
    }
}