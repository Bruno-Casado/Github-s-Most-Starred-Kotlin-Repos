package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoPersistenceSourceFailure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.RepoPersistenceSourceSuccess
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Success
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.Database
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.RepoDao
import javax.inject.Inject

class RepoPersistenceSourceImpl @Inject constructor(
    private val database: Database,
    private val repoDao: RepoDao
) : RepoPersistenceSource {

    override suspend fun getPagedRepos(page: Int): Either<Failure, List<RepoModel>> {
        return try {
            Either.Right(repoDao.getPagedRepos(page))
        } catch (ex: Exception) {
            Either.Left(RepoPersistenceSourceFailure.GetKotlinRepoListPersistenceError)
        }
    }

    override suspend fun insertRepos(repos: List<RepoModel>): Either<Failure, Success> {
        return try {
            database.runInTransaction {
                repoDao.insert(*repos.toTypedArray())
            }
            Either.Right(RepoPersistenceSourceSuccess.SaveKotlinRepoListSuccess)
        } catch (ex: Exception) {
            Either.Left(RepoPersistenceSourceFailure.SaveKotlinRepoListPersistenceError)
        }
    }
}