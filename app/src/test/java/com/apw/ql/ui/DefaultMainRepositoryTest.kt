package com.apw.ql.ui

import com.apw.ql.MainDispatcherRule
import com.apw.ql.data.model.ListResponse
import com.apw.ql.data.model.Repo
import com.apw.ql.data.model.User
import com.apw.ql.data.remote.Api
import com.apw.ql.ui.main.DefaultMainRepository
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

    private val exception = HttpException(Response.error<ResponseBody>(404, "No repo found".toResponseBody("plain/text".toMediaTypeOrNull())))
    private val repo = Repo(
        name = "repo1",
        owner = User("user1"),
        htmlUrl = "",
        topics = emptyList(),
        stargazersCount = 0,
        updateAt = "")

    private var api: Api = mockk(relaxed = true)

    private lateinit var mainRepository: DefaultMainRepository
    @Before
    fun setup() {
        coEvery { api.searchRepositories("repo1") } returns ListResponse(listOf(repo))
        coEvery { api.searchRepositories("") } throws exception
//        coEvery { api.searchRepositories("") } returns ListResponse(emptyList())
        mainRepository = DefaultMainRepository(api, Dispatchers.Main)
    }
    @Test
    fun getData_requestDataFromServer_successful() = runTest {
        val result = mainRepository.getData("repo1", null).single()

        assertThat(result.first(), `is`(repo))
        assertThat(result.size, `is`(1))
    }

    @Test
    fun getData_requestDataWithNameFromServer_failed() = runTest {
        try {
            mainRepository.getData("", null).single()
        } catch (e: HttpException) {
            assertThat(e.code(), `is`(404))
            assertThat(e.response()?.errorBody()?.string(), `is`("No repo found"))
        }
    }
}