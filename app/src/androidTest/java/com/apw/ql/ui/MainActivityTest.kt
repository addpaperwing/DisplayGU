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
import com.apw.ql.exts.TaskState
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

    private val result = MutableLiveData<Pair<User, List<Repo>>>()
    private val taskState = MutableLiveData<TaskState>()

    @Before
    fun setup() {
//        every { viewModel.result } returns result
//        every { viewModel.taskState } returns taskState
        scenario = launchActivity()
    }

    @After
    fun cleanUp() {
        scenario.close()
    }

    @Test
    fun successState_recyclerViewSetupDataCorrectly() {
        val stargazersCount = 0
        val forks = 0

        val user = User("user1")
//        val repo = Repo("repo1", null, "", stargazersCount, forks)



//        result.postValue(Pair(user, listOf(repo)))
        taskState.postValue(TaskState.SUCCEED)

        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
//        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("user1")).atPosition(0)))
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("repo1")).atPosition(1)))

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolder>(1, click()))
        val starAndForkText = getApplicationContext<Context>().resources.getString(R.string.star_fork_, stargazersCount, forks)
        onView(withText(starAndForkText)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun loadingState_recyclerViewNotDisplayProgressBarDisplay() {
        taskState.postValue(TaskState.LOADING)
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }
    @Test
    fun failedState_showToastRecyclerViewAndProgressBarNotDisplay() {
        val exception = HttpException(Response.error<ResponseBody>(404, "User not found".toResponseBody("plain/text".toMediaTypeOrNull())))

        taskState.postValue(TaskState.error(exception))

        onView(withText("User not found")).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
    }
}