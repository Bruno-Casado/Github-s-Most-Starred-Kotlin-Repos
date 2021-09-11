package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.Either
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.exception.Failure
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository.KotlinRepoRepository
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.mapper.toViewObject
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import javax.inject.Inject

class GetStarredKotlinReposUseCase @Inject constructor(private val repository: KotlinRepoRepository) {
    suspend operator fun invoke(page: Int): Either<Failure, List<RepoVO>> {
        return when (val result = repository.getPagedStarredKotlinRepos(page)) {
            is Either.Left -> result
            is Either.Right -> Either.Right(result.b.toViewObject())
        }
    }
}