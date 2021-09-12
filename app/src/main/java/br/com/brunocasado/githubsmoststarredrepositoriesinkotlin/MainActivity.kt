package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.ViewStatus
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.databinding.ActivityMainBinding
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RepoListViewModel
    private lateinit var binding: ActivityMainBinding

    private val lastVisibleItemPosition: Int
        get() = (binding.repoRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    private val listAdapter: StarredKotlinListAdapter = StarredKotlinListAdapter()

    private var failure: Failure? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupBinding()
        setupViewModel()
        initBinding()
        initObserver()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[RepoListViewModel::class.java]
    }

    private fun initBinding() {
        binding.repoRecyclerView.adapter = listAdapter
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.repoRecyclerView.addItemDecoration(dividerItemDecoration)
        binding.repoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                if ((totalItemCount == lastVisibleItemPosition + 1) &&
                    (failure != RepoRepositoryFailure.LastPageReached) &&
                    (viewModel.kotlinReposLiveData.value?.status != ViewStatus.LOADING)
                ) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun initObserver() {
        viewModel.kotlinReposLiveData.observe(this) {
            it.handleIt(
                onSuccess = ::handleSuccess,
                onFailure = ::handleFailure,
                isLoading = ::handleLoading
            )
        }
    }

    private fun handleSuccess(list: List<RepoVO>) {
        listAdapter.submitList(list)
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is RepoRepositoryFailure.LastPageReached -> {
                this.failure = failure
                showToast(getString(R.string.last_page_reached_error_message))
            }
            is RepoPersistenceSourceFailure -> showToast(getString(R.string.persistence_error_message))
            Failure.GenericError -> showToast(getString(R.string.generic_error_message))
            Failure.NetworkConnection -> showToast(getString(R.string.network_connection_error_message))
            Failure.ServerError -> showToast(getString(R.string.server_error_message))
            else -> {
                showToast(getString(R.string.generic_error_message))
            }
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}
