package com.example.displaygu.ui

import com.example.displaygu.MainDispatcherRule
import com.example.displaygu.data.Repo
import com.example.displaygu.data.User
import com.example.displaygu.network.Api
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
internal class DefaultMainRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val user = User("user1")
    private val exception = HttpException(Response.error<ResponseBody>(404, "User not found".toResponseBody("plain/text".toMediaTypeOrNull())))
    private val repo = Repo("repo1", null, "", 0, 0)

    private var api: Api = mockk(relaxed = true)

    private lateinit var mainRepository: DefaultMainRepository
    @Before
    fun setup() {
        coEvery { api.getUser("user1") } returns user
        coEvery { api.getRepos("user1") } returns listOf(repo)

        coEvery { api.getUser("") } throws exception
        coEvery { api.getRepos("") } returns emptyList()
        mainRepository = DefaultMainRepository(api, Dispatchers.Main)
    }
    @Test
    fun getData_requestDataWithNameFromServer_successful() = runTest {
        val result = mainRepository.getData("user1").single()

        assertThat(result.first, `is`(user))
        assertThat(result.second.size, `is`(1))
        assertThat(result.second[0], `is`(repo))
    }

    @Test
    fun getData_requestDataWithNameFromServer_failed() = runTest {
        try {
            mainRepository.getData("").single()
        } catch (e: HttpException) {
            assertThat(e.code(), `is`(404))
            assertThat(e.response()?.errorBody()?.string(), `is`("User not found"))
        }
    }
}