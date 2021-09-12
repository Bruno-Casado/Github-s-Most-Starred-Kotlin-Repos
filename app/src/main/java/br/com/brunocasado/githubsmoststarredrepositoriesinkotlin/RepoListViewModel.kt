package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.*
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase.GetStarredKotlinReposUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
    private val getStarredKotlinReposUseCase: GetStarredKotlinReposUseCase
) : ViewModel() {

    private val _kotlinReposLiveData = MutableLiveData<ViewState<List<RepoVO>>>()
    val kotlinReposLiveData: LiveData<ViewState<List<RepoVO>>> = _kotlinReposLiveData

    private var currentPage = FIRST_PAGE

    init {
        getRepos()
    }

    fun loadNextPage() {
        currentPage++
        getRepos()
    }

    private fun getRepos() {
        viewModelScope.launch {
            _kotlinReposLiveData.postLoading(_kotlinReposLiveData.value?.data ?: listOf())
            val repoListFuture = async {
                getStarredKotlinReposUseCase.invoke(currentPage)
            }

            val repoListFutureResult = repoListFuture.await()
            when (repoListFutureResult) {
                is Either.Left -> _kotlinReposLiveData.postFailure(repoListFutureResult.a)
                is Either.Right -> handleSuccess(repoListFutureResult.b)
            }
        }
    }

    private fun handleSuccess(repoList: List<RepoVO>) {
        val arrayList = ArrayList<RepoVO>()
        arrayList.addAll(_kotlinReposLiveData.value?.data ?: listOf())
        arrayList.addAll(repoList)
        _kotlinReposLiveData.postSuccess(arrayList)
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}