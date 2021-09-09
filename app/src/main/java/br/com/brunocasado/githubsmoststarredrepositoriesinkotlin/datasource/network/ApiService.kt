package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.network

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.GithubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val API_URL = "https://api.github.com/"
        private const val SEARCH_REPOSITORIES = "search/repositories"
        private const val QUERY = "q"
        private const val SORT = "sort"
        private const val PAGE = "page"
    }

    @GET(SEARCH_REPOSITORIES)
    suspend fun getKotlinMostStarredRepos(
        @Query(QUERY) query: String,
        @Query(SORT) sort: String,
        @Query(PAGE) page: Int
    ) : GithubResponse
}