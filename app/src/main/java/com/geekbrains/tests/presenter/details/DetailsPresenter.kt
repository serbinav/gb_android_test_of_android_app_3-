package com.geekbrains.tests.presenter.details

import com.geekbrains.tests.view.details.ViewDetailsContract

internal class DetailsPresenter<V : ViewDetailsContract> internal constructor(
    private var count: Int = 0
) : PresenterDetailsContract<V> {

    private var currentView: V? = null

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        currentView?.setCount(count)
    }

    override fun onDecrement() {
        count--
        currentView?.setCount(count)
    }

    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
        }
    }

    override fun detachView(view: V) {
        if (view == currentView) {
            currentView = null
        }
    }
}
