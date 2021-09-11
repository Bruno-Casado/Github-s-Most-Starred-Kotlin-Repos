package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoRepositoryFailure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Success
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.network.NetworkInfo
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSource
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class KotlinRepoRepositoryImpl @Inject constructor (
    private val apiService: ApiService,
    private val persistenceSource: RepoPersistenceSource,
    private val networkInfo: NetworkInfo
) : KotlinRepoRepository {

    private var lastPage = -1

    override suspend fun getPagedStarredKotlinRepos(page: Int): Either<Failure, List<RepoModel>> {
        val cachedRepos = getPagedReposFromDisk(page)
        return when (cachedRepos) {
            is Either.Left -> cachedRepos
            is Either.Right -> {
                if (shouldFetchFromNetwork(cachedRepos.b)) {
                    if (networkInfo.isNetworkAvailable()) {
                        handleNetworkRequest(loadRepoListFromNetwork(page), page)
                    } else {
                        Either.Left(Failure.NetworkConnection)
                    }
                } else if (isLastPage(page, cachedRepos.b)) {
                    Either.Left(RepoRepositoryFailure.LastPageReached)
                } else {
                    cachedRepos
                }
            }
        }
    }

    private suspend fun getPagedReposFromDisk(page: Int): Either<Failure, List<RepoModel>> {
        return persistenceSource.getPagedRepos(page)
    }

    private fun shouldFetchFromNetwork(data: List<RepoModel>) = data.isEmpty()

    private suspend fun handleNetworkRequest(
        networkRequest: Either<Failure, List<RepoModel>>,
        page: Int
    ): Either<Failure, List<RepoModel>> {
        val databaseRequest = when (networkRequest) {
            is Either.Left -> {
                return networkRequest
            }
            is Either.Right -> {
                insertRepoListToDisk(networkRequest.b)
            }
        }
        return when (databaseRequest) {
            is Either.Left -> databaseRequest
            is Either.Right -> {
                getPagedReposFromDisk(page)
            }
        }
    }

    private suspend fun loadRepoListFromNetwork(page: Int): Either<Failure, List<RepoModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getKotlinMostStarredRepos(
                    query = QUERY_PARAM,
                    sort = SORT_PARAM,
                    page = page
                )
                Either.Right(response.items)
            } catch (ex: Exception) {
                Either.Left(Failure.ServerError)
            }
        }
    }

    private suspend fun insertRepoListToDisk(repos: List<RepoModel>): Either<Failure, Success> {
        return withContext(Dispatchers.IO) {
            persistenceSource.insertRepos(repos)
        }
    }

    private fun isLastPage(page: Int, repoList: List<RepoModel>) =
        page > lastPage && repoList.isEmpty()

    companion object {
        private const val QUERY_PARAM = "language:kotlin"
        private const val SORT_PARAM = "stars"
    }
}