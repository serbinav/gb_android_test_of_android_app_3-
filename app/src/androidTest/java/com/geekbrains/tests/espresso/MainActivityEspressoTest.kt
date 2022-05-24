package com.geekbrains.tests.espresso

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText("algol"), closeSoftKeyboard())
        onView(withId(R.id.search)).perform(click())

        onView(isRoot()).perform(delay())
        onView(withId(R.id.totalCountTextView)).check(matches(withText(TestUtils.getString(R.string.test_result))))
    }

    @Test
    fun searchEditText_CheckHint() {
        onView(withId(R.id.searchEditText)).check(matches(withHint(R.string.search_hint)))
    }

    @Test
    fun searchEditText_CheckText() {
        val text = "root"
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText)).perform(replaceText(text), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).check(matches(withText(text)))
    }

    @Test
    fun toDetailsActivityButton_IsVisibleIsEnabled() {
        onView(withId(R.id.toDetailsActivityButton)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
        onView(withId(R.id.toDetailsActivityButton)).check(matches((isEnabled())))
    }

    @Test
    fun toDetailsActivityButton_CheckText() {
        onView(withId(R.id.toDetailsActivityButton)).check(matches(withText(TestUtils.getString(R.string.to_details))))
    }

    @Test
    fun toDetailsActivityButton_Click() {
        onView(withId(R.id.toDetailsActivityButton)).perform(click())

        onView(isRoot()).perform(delay())
        onView(withId(R.id.decrementButton)).check(matches((isDisplayed())))
        onView(withId(R.id.totalCountTextView))
            .check(
                matches(
                    withText(
                        String.format(
                            TestUtils.getString(R.string.results_count),0
                        )
                    )
                )
            )
        onView(withId(R.id.incrementButton)).check(matches((isDisplayed())))
    }

    private fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for $2 seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(2000)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}