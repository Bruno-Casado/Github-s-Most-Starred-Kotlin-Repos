package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel

interface KotlinRepoRepository {
    suspend fun getPagedStarredKotlinRepos(page: Int): Either<Failure, List<RepoModel>>
}