package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.usecase.GetStarredKotlinReposUseCase
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.repository.KotlinRepoRepository
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideGetStarredKotlinReposUseCase(repository: KotlinRepoRepository) =
        GetStarredKotlinReposUseCase(repository)
}