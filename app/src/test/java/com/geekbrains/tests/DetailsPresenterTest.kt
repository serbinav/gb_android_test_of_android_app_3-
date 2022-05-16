package com.geekbrains.tests

import com.geekbrains.tests.presenter.details.DetailsPresenter
import com.geekbrains.tests.view.details.DetailsActivity
import com.geekbrains.tests.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.never
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter<ViewDetailsContract>

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = DetailsPresenter()
    }

    @Test
    fun onIncrement_test() {
        presenter.attachView(viewContract)
        presenter.onIncrement()
        Mockito.verify(viewContract, Mockito.times(1)).setCount(1)
    }

    @Test
    fun onDecrement_test() {
        presenter.attachView(viewContract)
        presenter.onDecrement()
        Mockito.verify(viewContract, Mockito.times(1)).setCount(-1)
    }

    @Test
    fun attachView_null() {
        presenter.onIncrement()
        Mockito.verify(viewContract, never()).setCount(1)
    }

    @Test
    fun detachView_notNull() {
        presenter.attachView(viewContract)
        presenter.detachView(DetailsActivity())
        presenter.onIncrement()
        Mockito.verify(viewContract, Mockito.times(1)).setCount(1)
    }

    @Test
    fun detachView_null() {
        presenter.attachView(viewContract)
        presenter.detachView(viewContract)
        presenter.onIncrement()
        Mockito.verify(viewContract, never()).setCount(1)
    }
}