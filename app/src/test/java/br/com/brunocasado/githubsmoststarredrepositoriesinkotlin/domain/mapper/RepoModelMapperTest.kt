package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.mapper

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.helpers.createRepoModelList
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepoModelMapperTest {

    @Test
    fun `given a RepoModel List when toViewObject is invoked then should return a RepoVO List`() {
        val repoModelList = createRepoModelList()
        val expected = listOf(
            RepoVO(
                repoName = "okhttp",
                stars = 40761,
                forks = 8624,
                photo = "https://avatars.githubusercontent.com/u/82592?v=4",
                authorsName = "square"
            ),
            RepoVO(
                repoName = "architecture-samples",
                stars = 39420,
                forks = 10814,
                photo = "https://avatars.githubusercontent.com/u/32689599?v=4",
                authorsName = "android"
            )
        )

        assertEquals(expected, repoModelList.toViewObject())
    }
}