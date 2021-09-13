package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase.GetStarredKotlinReposUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
    private val getStarredKotlinReposUseCase: GetStarredKotlinReposUseCase
) : ViewModel() {

    private val _kotlinReposLiveData = MutableLiveData<List<RepoVO>>()
    val kotlinReposLiveData: LiveData<List<RepoVO>> = _kotlinReposLiveData

    private val _isLoadingLiveData = MutableLiveData<Boolean>().apply { value = false }
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    private val _errorLiveData = MutableLiveData<Failure?>()
    val errorLiveData: LiveData<Failure?> = _errorLiveData

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
            _isLoadingLiveData.postValue(true)
            val repoListFuture = async {
                getStarredKotlinReposUseCase.invoke(currentPage)
            }

            val repoListFutureResult = repoListFuture.await()
            when (repoListFutureResult) {
                is Either.Left -> {
                    _errorLiveData.postValue(repoListFutureResult.a)
                    _isLoadingLiveData.postValue(false)
                }
                is Either.Right -> {
                    handleSuccess(repoListFutureResult.b)
                    _isLoadingLiveData.postValue(false)
                }
            }
        }
    }

    private fun handleSuccess(repoList: List<RepoVO>) {
        val arrayList = ArrayList<RepoVO>()
        arrayList.addAll(_kotlinReposLiveData.value ?: listOf())
        arrayList.addAll(repoList)
        _kotlinReposLiveData.postValue(arrayList)
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}