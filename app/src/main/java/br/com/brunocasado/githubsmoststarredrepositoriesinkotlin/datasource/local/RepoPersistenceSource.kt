package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Success
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel

interface RepoPersistenceSource {
    suspend fun getPagedRepos(page: Int): Either<Failure, List<RepoModel>>
    suspend fun insertRepos(repos: List<RepoModel>): Either<Failure, Success>
}