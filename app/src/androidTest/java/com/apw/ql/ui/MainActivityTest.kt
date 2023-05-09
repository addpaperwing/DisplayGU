package com.apw.ql.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apw.ql.data.model.Repo
import com.apw.ql.data.model.User
import com.apw.ql.di.RepositoryModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.apw.ql.R
import com.apw.ql.ToastMatcher
import com.apw.ql.atPosition
import com.apw.ql.data.remote.State
import com.apw.ql.ui.main.MainActivity
import com.apw.ql.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.not
import org.junit.After
import retrofit2.HttpException
import retrofit2.Response

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(RepositoryModule::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val viewModel = mockk<MainViewModel>(relaxed = true)

    private lateinit var scenario: ActivityScenario<MainActivity>

    private val result = MutableLiveData<State<List<Repo>>>()

    @Before
    fun setup() {
        every { viewModel.result } returns result
        scenario = launchActivity()
    }

    @After
    fun cleanUp() {
        scenario.close()
    }

    @Test
    fun successState_recyclerViewSetupDataCorrectly() {
        val repo = Repo(
            name = "repo1",
            owner = User("user1"),
            htmlUrl = "",
            topics = emptyList(),
            stargazersCount = 0,
            updateAt = ""
        )

        result.postValue(State.Success(listOf(repo)))

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("repo1")).atPosition(0)))

//        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(1, click()))
    }

    @Test
    fun loadingState_recyclerViewNotDisplayProgressBarDisplay() {
        result.postValue(State.Loading)
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }
    @Test
    fun failedState_showToastRecyclerViewAndProgressBarNotDisplay() {
        val exception = HttpException(Response.error<ResponseBody>(404, "User not found".toResponseBody("plain/text".toMediaTypeOrNull())))

        result.postValue(State.Error(exception))

        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.retryButton)).check(matches(isDisplayed()))
        onView(withId(R.id.errorTextView)).check(matches(isDisplayed()))
        onView(withId(R.id.errorTextView)).check(matches(withText("User not found")))
    }
}