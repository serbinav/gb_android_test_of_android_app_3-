package com.geekbrains.tests.espresso

import android.content.Context
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object TestUtils {
    fun getString(@StringRes stringRes: Int): String {
        try {
            return ApplicationProvider.getApplicationContext<Context>().getString(stringRes)
        } catch (ex: Throwable) {
            throw Exception("Не удалось получить строковый ресурс $stringRes\n${ex.message}")
        }
    }

    class ToastMatcher : TypeSafeMatcher<Root>() {
        override fun describeTo(description: Description) {
            description.appendText("is toast")
        }

        override fun matchesSafely(root: Root): Boolean {
            try {
                val type = root.windowLayoutParams.get().type
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    val windowToken = root.decorView.windowToken
                    val appToken = root.decorView.applicationWindowToken
                    if (windowToken === appToken) {
                        return true
                    }
                }
            } catch (ex: Exception) {
                throw Exception(ex.message!!)
            }
            return false
        }
    }
}