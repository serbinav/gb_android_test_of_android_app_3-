package com.geekbrains.tests.presenter.details

import com.geekbrains.tests.presenter.PresenterContract
import com.geekbrains.tests.view.details.ViewDetailsContract

internal interface PresenterDetailsContract<V : ViewDetailsContract> : PresenterContract<V> {
    fun setCounter(count: Int)
    fun onIncrement()
    fun onDecrement()
}
