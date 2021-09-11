package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository.KotlinRepoRepository
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.helpers.createRepoModelList
import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetStarredKotlinReposUseCaseTest {

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val repositoryMock: KotlinRepoRepository = mockk()

    private val subject = GetStarredKotlinReposUseCase(repositoryMock)

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when GetStarredKotlinReposUseCase is invoked and receives empty list then should return success with empty list`() {
        runBlocking {
            launch(Dispatchers.Main) {
                coEvery {
                    repositoryMock.getPagedStarredKotlinRepos(SECOND_PAGE)
                } returns Either.Right(listOf())

                val result = subject.invoke(SECOND_PAGE)

                assertEquals(result.isRight, true)
                assertEquals((result as Either.Right).b.size, 0)

                coVerify(exactly = 1) {
                    repositoryMock.getPagedStarredKotlinRepos(SECOND_PAGE)
                }
            }
        }
    }

    @Test
    fun `when GetStarredKotlinReposUseCase is invoked and receives RepoModel List then should return success with RepoVO List`() {
        runBlocking {
            launch(Dispatchers.Main) {
                coEvery {
                    repositoryMock.getPagedStarredKotlinRepos(FIRST_PAGE)
                } returns Either.Right(createRepoModelList())

                val result = subject.invoke(FIRST_PAGE)

                assertEquals(result.isRight, true)
                assertEquals((result as Either.Right).b.size, 2)

                coVerify(exactly = 1) {
                    repositoryMock.getPagedStarredKotlinRepos(FIRST_PAGE)
                }
            }
        }
    }

    @Test
    fun `when GetStarredKotlinReposUseCase is invoked and receives Failure then should return Failure`() {
        runBlocking {
            launch(Dispatchers.Main) {
                coEvery {
                    repositoryMock.getPagedStarredKotlinRepos(FIRST_PAGE)
                } returns Either.Left(Failure.NetworkConnection)

                val result = subject.invoke(FIRST_PAGE)

                assertEquals(result.isLeft, true)
                assertEquals((result as Either.Left).a, Failure.NetworkConnection)

                coVerify(exactly = 1) {
                    repositoryMock.getPagedStarredKotlinRepos(FIRST_PAGE)
                }
            }
        }
    }

    companion object {
        private const val SECOND_PAGE = 2
        private const val FIRST_PAGE = 1
    }

}