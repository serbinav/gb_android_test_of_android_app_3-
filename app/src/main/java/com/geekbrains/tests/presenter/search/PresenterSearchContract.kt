package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.presenter.PresenterContract
import com.geekbrains.tests.view.search.ViewSearchContract

internal interface PresenterSearchContract<V : ViewSearchContract> : PresenterContract<V> {
    fun searchGitHub(searchQuery: String)
}
