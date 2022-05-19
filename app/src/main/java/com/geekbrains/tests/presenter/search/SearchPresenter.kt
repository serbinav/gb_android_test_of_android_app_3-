package com.geekbrains.tests.presenter.search

import com.geekbrains.tests.model.SearchResponse
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.repository.RepositoryCallback
import com.geekbrains.tests.view.search.ViewSearchContract
import retrofit2.Response

/**
 * В архитектуре MVP все запросы на получение данных адресуются в Репозиторий.
 * Запросы могут проходить через Interactor или UseCase, использовать источники
 * данных (DataSource), но суть от этого не меняется.
 * Непосредственно Презентер отвечает за управление потоками запросов и ответов,
 * выступая в роли регулировщика движения на перекрестке.
 */

internal class SearchPresenter<V : ViewSearchContract> internal constructor(
    private val repository: RepositoryContract
) : PresenterSearchContract<V>, RepositoryCallback {

    private var currentView: V? = null

    override fun searchGitHub(searchQuery: String) {
        currentView?.displayLoading(true)
        repository.searchGithub(searchQuery, this)
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        currentView?.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                currentView?.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                currentView?.displayError("Search results or total count are null")
            }
        } else {
            currentView?.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleGitHubError() {
        currentView?.displayLoading(false)
        currentView?.displayError()
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
