package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSource
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.local.RepoPersistenceSourceImpl
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
}