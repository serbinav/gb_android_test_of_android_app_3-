package com.geekbrains.tests.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.*
import com.geekbrains.tests.R
import com.geekbrains.tests.TestUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    //Класс UiDevice предоставляет доступ к вашему устройству.
    //Именно через UiDevice вы можете управлять устройством, открывать приложения
    //и находить нужные элементы на экране
    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())

    //Контекст нам понадобится для запуска нужных экранов и получения packageName
    private val context = ApplicationProvider.getApplicationContext<Context>()

    //Путь к классам нашего приложения, которые мы будем тестировать
    private val packageName = context.packageName

    @Before
    fun setup() {
        //Для начала сворачиваем все приложения, если у нас что-то запущено
        uiDevice.pressHome()

        //Запускаем наше приложение
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            //Чистим бэкстек от запущенных ранее Активити
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            context.startActivity(intent)

            //Ждем, когда приложение откроется на смартфоне чтобы начать тестировать его элементы
            uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TestUtils.TIMEOUT)
        } else {
            throw Exception("Не удалось запустить Activity")
        }
    }

    //Убеждаемся, что приложение открыто. Для этого достаточно найти на экране любой элемент
    //и проверить его на null
    @Test
    fun test_MainActivityIsStarted() {
        //Через uiDevice находим editText и проверяем на null
        Assert.assertNotNull(TestUtils.getSearchEditText(uiDevice, packageName))
    }

    //Убеждаемся, что поиск работает как ожидается
    @Test
    fun test_SearchIsPositive() {
        //Через uiDevice находим editText и устанавливаем значение
        TestUtils.getSearchEditText(uiDevice, packageName).text = TestUtils.SEARCH_QUERY

        TestUtils.getSearch(uiDevice, packageName).click()

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что сервер вернул ответ с какими-то данными, то есть запрос отработал.
        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)

        //Убеждаемся, что сервер вернул корректный результат. Обратите внимание, что количество
        //результатов может варьироваться во времени, потому что количество репозиториев постоянно меняется.
        Assert.assertEquals(changedText.text.toString(), TestUtils.getString(R.string.test_result))
    }

    //Убеждаемся, что DetailsScreen открывается
    @Test
    fun test_OpenDetailsScreen() {
        //Находим кнопку и кликаем по ней
        TestUtils.getToDetailsActivityButton(uiDevice, packageName)
            .clickAndWait(Until.newWindow(), TestUtils.TIMEOUT)

        //Ожидаем конкретного события: появления текстового поля totalCountTextView.
        //Это будет означать, что DetailsScreen открылся и это поле видно на экране.
        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)

        //Убеждаемся, что поле видно и содержит предполагаемый текст.
        //Обратите внимание, что текст должен быть "Number of results: 0",
        //так как мы кликаем по кнопке не отправляя никаких поисковых запросов.
        //Чтобы проверить отображение определенного количества репозиториев,
        //вам в одном и том же методе нужно отправить запрос на сервер и открыть DetailsScreen.
        Assert.assertEquals(
            changedText.text,
            String.format(TestUtils.getString(R.string.results_count), 0)
        )
    }

    @Test
    fun test_DetailsScreenDisplayCorrectData() {
        TestUtils.getSearchEditText(uiDevice, packageName).text = TestUtils.SEARCH_QUERY

        TestUtils.getSearch(uiDevice, packageName).click()

        TestUtils.getToDetailsActivityButton(uiDevice, packageName)
            .clickAndWait(Until.newWindow(), TestUtils.TIMEOUT)

        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)
        Assert.assertEquals(changedText.text.toString(), TestUtils.getString(R.string.test_result))
    }

    @Test
    fun test_EmptySearch() {
        TestUtils.getSearch(uiDevice, packageName).click()

        onView(withText(TestUtils.getString(R.string.enter_search_word)))
            .inRoot(TestUtils.ToastMatcher())
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun test_SearchWithSpace() {
        TestUtils.getSearchEditText(uiDevice, packageName).text = " "

        TestUtils.getSearch(uiDevice, packageName).click()

        onView(withText(TestUtils.getString(R.string.enter_search_word)))
            .inRoot(TestUtils.ToastMatcher())
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun test_DecrementButton() {
        TestUtils.getToDetailsActivityButton(uiDevice, packageName)
            .clickAndWait(Until.newWindow(), TestUtils.TIMEOUT)

        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)
        Assert.assertEquals(
            changedText.text.toString(),
            String.format(TestUtils.getString(R.string.results_count), 0)
        )

        val decrementBtn: UiObject2 = uiDevice.findObject(
            By.res(
                packageName,
                "decrementButton"
            )
        )
        decrementBtn.click()

        Assert.assertEquals(
            changedText.text.toString(),
            String.format(TestUtils.getString(R.string.results_count), -1)
        )
    }

    @Test
    fun test_IncrementButton() {
        TestUtils.getToDetailsActivityButton(uiDevice, packageName)
            .clickAndWait(Until.newWindow(), TestUtils.TIMEOUT)

        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)
        Assert.assertEquals(
            changedText.text.toString(),
            String.format(TestUtils.getString(R.string.results_count), 0)
        )

        val incrementBtn: UiObject2 = uiDevice.findObject(
            By.res(
                packageName, "incrementButton"
            )
        )
        incrementBtn.click()

        Assert.assertEquals(
            changedText.text.toString(),
            String.format(TestUtils.getString(R.string.results_count), 1)
        )
    }

    @Test
    fun test_ScreenBackButton() {
        val toDetails = TestUtils.getToDetailsActivityButton(uiDevice, packageName)
        toDetails.clickAndWait(Until.newWindow(), TestUtils.TIMEOUT)

        val changedText = TestUtils.getTotalCountTextView(uiDevice, packageName)
        Assert.assertEquals(
            changedText.text.toString(),
            String.format(TestUtils.getString(R.string.results_count), 0)
        )

        val uiDevice = UiDevice.getInstance(getInstrumentation())
        uiDevice.pressBack()

        Assert.assertEquals(
            toDetails.text.toString(),
            TestUtils.getString(R.string.to_details).toUpperCase()
        )
    }
}
