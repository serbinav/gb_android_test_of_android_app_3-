package com.geekbrains.tests.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.TestUtils
import com.geekbrains.tests.view.search.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val materialButtonToDetails = onView(withId(R.id.toDetailsActivityButton))
        materialButtonToDetails.check(matches(withText(TestUtils.getString(R.string.to_details))))
        materialButtonToDetails.check(matches((isDisplayed())))
        materialButtonToDetails.perform(click())

        val buttonIncrement = onView(withId(R.id.incrementButton))
        buttonIncrement.check(matches(withText(TestUtils.getString(R.string.increment_text))))
        buttonIncrement.check(matches((isDisplayed())))
        buttonIncrement.perform(click())

        val textViewTotalCount = onView(withId(R.id.totalCountTextView))
        textViewTotalCount.check(
            matches(
                withText(
                    String.format(
                        TestUtils.getString(R.string.results_count),
                        1
                    )
                )
            )
        )
        textViewTotalCount.check(matches((isDisplayed())))

        buttonIncrement.check(matches((isDisplayed())))
    }
}