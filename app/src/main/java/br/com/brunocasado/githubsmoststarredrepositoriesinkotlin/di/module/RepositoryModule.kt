package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.core.network.NetworkInfo
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSource
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSourceImpl
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.network.ApiService
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository.KotlinRepoRepository
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.repository.KotlinRepoRepositoryImpl
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.Database
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.RepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepoPersistenceSource(database: Database, repoDao: RepoDao): RepoPersistenceSource {
        return RepoPersistenceSourceImpl(database, repoDao)
    }

    @Provides
    fun provideRepoRepository(
        apiService: ApiService,
        persistenceSource: RepoPersistenceSource,
        networkInfo: NetworkInfo
    ): KotlinRepoRepository {
        return KotlinRepoRepositoryImpl(apiService, persistenceSource, networkInfo)
    }
}