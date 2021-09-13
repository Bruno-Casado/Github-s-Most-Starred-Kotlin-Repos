package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.helpers

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.GithubResponse
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.OwnerModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.mapper.toViewObject
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO

fun createGithubResponseMock() = GithubResponse(
    listOf(
        RepoModel(
            id = 5152285,
            name = "okhttp",
            stars = 40761,
            forks = 8624,
            owner = OwnerModel(
                login = "square",
                avatarUrl = "https://avatars.githubusercontent.com/u/82592?v=4"
            )
        ),
        RepoModel(
            id = 51148780,
            name = "architecture-samples",
            stars = 39420,
            forks = 10814,
            owner = OwnerModel(
                login = "android",
                avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4"
            )
        )
    )
)

fun createRepoModelList() = createGithubResponseMock().items

fun createRepoVoList() = createRepoModelList().toViewObject()