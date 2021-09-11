package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoPersistenceSourceSuccess
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoRepositoryFailure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.network.NetworkInfo
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSource
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.GithubResponse
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.OwnerModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.network.ApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class KotlinRepoRepositoryImplTest {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val apiServiceMock: ApiService = mockk()
    private val persistenceSourceMock: RepoPersistenceSource = mockk()
    private val networkInfoMock: NetworkInfo = mockk()

    private val subject =
        KotlinRepoRepositoryImpl(apiServiceMock, persistenceSourceMock, networkInfoMock)

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked with firstPage and cache is empty and has network connection then should return Success with RepoModel List`() {
        val githubResponseMock = createGithubResponseMock()
        every { networkInfoMock.isNetworkAvailable() } returns true
        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(listOf()) andThen Either.Right(githubResponseMock.items)
            coEvery {
                apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
            } returns githubResponseMock
            coEvery {
                persistenceSourceMock.insertRepos(githubResponseMock.items)
            } returns Either.Right(RepoPersistenceSourceSuccess.SaveKotlinRepoListSuccess)

            launch(Dispatchers.Main) {
                val result = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(result.isRight, true)
                assertEquals((result as Either.Right).b.size, 2)
                assertEquals(result.b, githubResponseMock.items)

                coVerify(exactly = 2) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 1) {
                    persistenceSourceMock.insertRepos(githubResponseMock.items)
                }
                coVerify(exactly = 1) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
            }
        }
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked with firstPage and has no internet connect and no cache then should return NetworkConnection Failure`() {
        every { networkInfoMock.isNetworkAvailable() } returns false

        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(listOf())

            launch(Dispatchers.Main) {
                val result = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(result.isLeft, true)
                assertEquals((result as Either.Left).a, Failure.NetworkConnection)

                coVerify(exactly = 1) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    persistenceSourceMock.insertRepos(any())
                }
            }
        }
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked with firstPage and has no internet connect and has cache then should return Success with RepoModel List`() {
        val repoModelList = createGithubResponseMock().items
        every { networkInfoMock.isNetworkAvailable() } returns false

        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(repoModelList)

            launch(Dispatchers.Main) {
                val result = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(result.isRight, true)
                assertEquals((result as Either.Right).b.size, 2)
                assertEquals(result.b, repoModelList)

                coVerify(exactly = 1) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    persistenceSourceMock.insertRepos(any())
                }
            }
        }
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked and lastPage is reached then should return LastPageReached Failure`() {
        val githubResponseMock = createGithubResponseMock()
        every { networkInfoMock.isNetworkAvailable() } returns true

        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(listOf()) andThen Either.Right(githubResponseMock.items)
            coEvery {
                apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
            } returns githubResponseMock
            coEvery {
                persistenceSourceMock.insertRepos(githubResponseMock.items)
            } returns Either.Right(RepoPersistenceSourceSuccess.SaveKotlinRepoListSuccess)
            coEvery {
                persistenceSourceMock.getPagedRepos(LAST_PAGE)
            } returns Either.Right(listOf())

            launch(Dispatchers.Main) {
                val firstPageResult = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(firstPageResult.isRight, true)
                assertEquals((firstPageResult as Either.Right).b.size, 2)
                assertEquals(firstPageResult.b, githubResponseMock.items)

                val lastPageResult = subject.getPagedStarredKotlinRepos(LAST_PAGE)

                assertEquals(lastPageResult.isLeft, true)
                assertEquals((lastPageResult as Either.Left).a, RepoRepositoryFailure.LastPageReached)

                coVerify(exactly = 2) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 1) {
                    persistenceSourceMock.insertRepos(githubResponseMock.items)
                }
                coVerify(exactly = 1) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
                coVerify(exactly = 1) {
                    persistenceSourceMock.getPagedRepos(LAST_PAGE)
                }
                coVerify(exactly = 0) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, LAST_PAGE)
                }
            }
        }
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked with internet connection and no cache and some error occurs then should return Failure GenericError`() {
        val githubResponseMock = createGithubResponseMock()
        every { networkInfoMock.isNetworkAvailable() } returns true
        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(listOf())
            coEvery {
                apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
            } throws Exception()

            launch(Dispatchers.Main) {
                val result = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(result.isLeft, true)
                assertEquals((result as Either.Left).a, Failure.GenericError)

                coVerify(exactly = 1) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    persistenceSourceMock.insertRepos(githubResponseMock.items)
                }
                coVerify(exactly = 1) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
            }
        }
    }

    @Test
    fun `when getPagedStarredKotlinRepos is invoked with internet connection and no cache and server fails then should return Failure ServerError`() {
        val githubResponseMock = createGithubResponseMock()
        every { networkInfoMock.isNetworkAvailable() } returns true
        runBlocking {
            coEvery {
                persistenceSourceMock.getPagedRepos(FIRST_PAGE)
            } returns Either.Right(listOf())
            coEvery {
                apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
            } throws HttpException(mockk(relaxed = true))

            launch(Dispatchers.Main) {
                val result = subject.getPagedStarredKotlinRepos(FIRST_PAGE)

                assertEquals(result.isLeft, true)
                assertEquals((result as Either.Left).a, Failure.ServerError)

                coVerify(exactly = 1) {
                    persistenceSourceMock.getPagedRepos(FIRST_PAGE)
                }
                coVerify(exactly = 0) {
                    persistenceSourceMock.insertRepos(githubResponseMock.items)
                }
                coVerify(exactly = 1) {
                    apiServiceMock.getKotlinMostStarredRepos(QUERY_PARAM, SORT_PARAM, FIRST_PAGE)
                }
            }
        }
    }

    private fun createGithubResponseMock() = GithubResponse(
        listOf(
            RepoModel(
                id = 5152285,
                name = "okhttp",
                stars = 40761,
                forks = 8624,
                owner = OwnerModel(
                    login = "square",
                    avatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4"
                )
            ),
            RepoModel(
                id = 51148780,
                name = "architecture-samples",
                stars = 39420,
                forks = 10814,
                owner = OwnerModel(
                    login = "android",
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"
                )
            )
        )
    )


    companion object {
        private const val FIRST_PAGE = 1
        private const val LAST_PAGE = 2
        private const val QUERY_PARAM = "language:kotlin"
        private const val SORT_PARAM = "stars"
    }
}