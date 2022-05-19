package com.geekbrains.tests

import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider

object TestUtils {
    fun getString(@StringRes stringRes: Int): String {
        try {
            return ApplicationProvider.getApplicationContext<Context>().getString(stringRes)
        } catch (ex: Throwable) {
            throw Exception("Не удалось получить строковый ресурс $stringRes\n${ex.message}")
        }
    }
}