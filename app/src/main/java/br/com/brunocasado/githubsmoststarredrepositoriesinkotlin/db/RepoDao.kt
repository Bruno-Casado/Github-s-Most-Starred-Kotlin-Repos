package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel

@Dao
abstract class RepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg repo: RepoModel)

    @Query("SELECT * FROM RepoModel LIMIT $PAGE_SIZE*:page-1, $PAGE_SIZE")
    abstract suspend fun getPagedRepos(page: Int): List<RepoModel>

    companion object {
        const val PAGE_SIZE = 30
    }
}
