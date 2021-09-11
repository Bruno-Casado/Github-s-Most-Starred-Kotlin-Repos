package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel

@Database(
    entities = [
        RepoModel::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    abstract fun repoDao(): RepoDao
}