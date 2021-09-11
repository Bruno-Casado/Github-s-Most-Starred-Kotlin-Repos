package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.di.module

import android.app.Application
import androidx.room.Room
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.Database
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db.RepoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: Application): Database {
        return Room
            .databaseBuilder(application, Database::class.java, "repos.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRepoDao(database: Database): RepoDao = database.repoDao()
}