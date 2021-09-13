package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.repository.RepoRepositoryFailure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase.GetStarredKotlinReposUseCase
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.helpers.MainCoroutineRule
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.helpers.createRepoVoList
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.presentation.RepoListViewModel.Companion.FIRST_PAGE
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RepoListViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val useCaseMock: GetStarredKotlinReposUseCase = mockk()
    private lateinit var subject: RepoListViewModel

    private val kotlinReposObserver: Observer<List<RepoVO>> = mockk()
    private val loadingObserver: Observer<Boolean> = mockk()
    private val errorObserver: Observer<Failure?> = mockk()

    @Before
    fun setup() {
        mainCoroutineRule.pauseDispatcher()
    }

    @After
    fun tearDown() {
        subject.kotlinReposLiveData.removeObserver(kotlinReposObserver)
        subject.isLoadingLiveData.removeObserver(loadingObserver)
        subject.errorLiveData.removeObserver(errorObserver)
    }

    @Test
    fun `when viewModel init without internet connection and has no cache then errorLiveData should post NetworkConnection Failure`() {
        val errorMock = Failure.NetworkConnection

        justRun { loadingObserver.onChanged(any()) }
        justRun { errorObserver.onChanged(any()) }
        subject = RepoListViewModel(useCaseMock)
        subject.kotlinReposLiveData.observeForever(kotlinReposObserver)
        subject.isLoadingLiveData.observeForever(loadingObserver)
        subject.errorLiveData.observeForever(errorObserver)

        runBlocking {
            coEvery {
                useCaseMock.invoke(FIRST_PAGE)
            } returns Either.Left(errorMock)
        }

        mainCoroutineRule.resumeDispatcher()

        coVerify(exactly = 1) {
            useCaseMock.invoke(FIRST_PAGE)
        }
        coVerifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        coVerify(exactly = 1) {
            errorObserver.onChanged(errorMock)
        }
        coVerify(exactly = 0) {
            kotlinReposObserver.onChanged(any())
        }
    }

    @Test
    fun `when viewModel init with internet connection and no cache then kotlinReposLiveData should post RepoVO List`() {
        val repoVoListMock = createRepoVoList()

        justRun { loadingObserver.onChanged(any()) }
        justRun { kotlinReposObserver.onChanged(repoVoListMock) }

        subject = RepoListViewModel(useCaseMock)
        subject.kotlinReposLiveData.observeForever(kotlinReposObserver)
        subject.isLoadingLiveData.observeForever(loadingObserver)
        subject.errorLiveData.observeForever(errorObserver)

        runBlocking {
            coEvery {
                useCaseMock.invoke(FIRST_PAGE)
            } returns Either.Right(repoVoListMock)
        }

        mainCoroutineRule.resumeDispatcher()

        coVerify(exactly = 1) {
            useCaseMock.invoke(FIRST_PAGE)
        }
        coVerifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        coVerify(exactly = 1) {
            kotlinReposObserver.onChanged(repoVoListMock)
        }
        coVerify(exactly = 0) {
            errorObserver.onChanged(any())
        }
    }

    @Test
    fun `given viewModel is initialized when loadNextPage is invoked with success then kotlinReposLiveData should post RepoVO List`() {
        val repoVoListMock = createRepoVoList()
        val secondPageListMock = createRepoVoList()
        val arrayList = ArrayList<RepoVO>()
        arrayList.addAll(repoVoListMock)
        arrayList.addAll(secondPageListMock)

        justRun { loadingObserver.onChanged(any()) }
        justRun {
            kotlinReposObserver.onChanged(repoVoListMock)
        }

        subject = RepoListViewModel(useCaseMock)
        subject.kotlinReposLiveData.observeForever(kotlinReposObserver)
        subject.isLoadingLiveData.observeForever(loadingObserver)
        subject.errorLiveData.observeForever(errorObserver)

        runBlocking {
            coEvery {
                useCaseMock.invoke(FIRST_PAGE)
            } returns Either.Right(repoVoListMock)
            coEvery {
                useCaseMock.invoke(SECOND_PAGE)
            } returns Either.Right(secondPageListMock)

        }

        mainCoroutineRule.resumeDispatcher()

        justRun {
            kotlinReposObserver.onChanged(arrayList)
        }
        subject.loadNextPage()

        coVerify(exactly = 1) {
            useCaseMock.invoke(FIRST_PAGE)
        }
        coVerify(exactly = 1) {
            useCaseMock.invoke(SECOND_PAGE)
        }
        coVerifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        coVerifyOrder {
            kotlinReposObserver.onChanged(repoVoListMock)
            kotlinReposObserver.onChanged(arrayList)
        }
        coVerify(exactly = 0) {
            errorObserver.onChanged(any())
        }
    }

    @Test
    fun `given viewModel is initialized when loadNextPage is invoked and is LastPage then erroLiveData should post RepoRepositoryFailure LastPageReached`() {
        val repoVoListMock = createRepoVoList()

        justRun { loadingObserver.onChanged(any()) }
        justRun { kotlinReposObserver.onChanged(repoVoListMock) }

        subject = RepoListViewModel(useCaseMock)
        subject.kotlinReposLiveData.observeForever(kotlinReposObserver)
        subject.isLoadingLiveData.observeForever(loadingObserver)
        subject.errorLiveData.observeForever(errorObserver)

        runBlocking {
            coEvery {
                useCaseMock.invoke(FIRST_PAGE)
            } returns Either.Right(repoVoListMock)
            coEvery {
                useCaseMock.invoke(SECOND_PAGE)
            } returns Either.Left(RepoRepositoryFailure.LastPageReached)

        }

        mainCoroutineRule.resumeDispatcher()

        justRun {
            errorObserver.onChanged(RepoRepositoryFailure.LastPageReached)
        }

        subject.loadNextPage()

        coVerify(exactly = 1) {
            useCaseMock.invoke(FIRST_PAGE)
        }
        coVerify(exactly = 1) {
            useCaseMock.invoke(SECOND_PAGE)
        }
        coVerifyOrder {
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            loadingObserver.onChanged(false)
        }
        coVerify(exactly = 1) {
            kotlinReposObserver.onChanged(repoVoListMock)
        }
        coVerify(exactly = 1) {
            errorObserver.onChanged(RepoRepositoryFailure.LastPageReached)
        }
    }

    companion object {
        private const val SECOND_PAGE = 2
    }
}