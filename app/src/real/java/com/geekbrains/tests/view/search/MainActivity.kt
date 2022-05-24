package com.geekbrains.tests.view.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.tests.R
import com.geekbrains.tests.databinding.ActivityMainBinding
import com.geekbrains.tests.model.SearchResult
import com.geekbrains.tests.presenter.RepositoryContract
import com.geekbrains.tests.presenter.search.PresenterSearchContract
import com.geekbrains.tests.presenter.search.SearchPresenter
import com.geekbrains.tests.repository.GitHubApi
import com.geekbrains.tests.repository.GitHubRepository
import com.geekbrains.tests.view.details.DetailsActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity(), ViewSearchContract {

//    1. Избавьтесь от Espresso в вашем методе test_SearchIsPositive():
//    добавьте отдельную кнопку поиска на экране MainActivity и нажимайте
//    на нее чтобы запустить поиск и получить результат.
//    2. У нас уже написан тест, который просто открывает DetailsScreen.
//    Вам нужно написать еще один тест, в котором вы убедитесь, что после
//    успешного выполнения запроса и получения нужного количества репозиториев,
//    DetailsScreen отображает именно это количество.
//    3. Покройте приложение тестами полностью: проверьте не только позитивные,
//    но и негативные сценарии, проверьте функционал кнопок на DetailsScreen.
//
//    * Настройте UIautomator viewer на вашем компьютере: пропишите Enviroment
//    Variables и запустите uiautomatorviewer.bat в папке bin.
//    * Откройте приложение Настроек на своем смартфоне.
//    * Откройте какое-то приложение помимо Настроек и нажмите в нем какую-нибудь кнопку.

    private lateinit var binding: ActivityMainBinding
    private val adapter = SearchResultAdapter()
    private lateinit var presenter: PresenterSearchContract<ViewSearchContract>
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = SearchPresenter(createRepository())
        setUI()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }

    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setRecyclerView()

        binding.search.setOnClickListener{
            val query = binding.searchEditText.text.toString()
            if (query.isNotBlank()) {
                presenter.searchGitHub(query)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.enter_search_word),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun createRepository(): RepositoryContract {
        return GitHubRepository(createRetrofit().create(GitHubApi::class.java))
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    ) {
        with(binding.totalCountTextView) {
            visibility = View.VISIBLE
            text =
                String.format(Locale.getDefault(), getString(R.string.results_count), totalCount)
        }

        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}
