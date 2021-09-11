package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.mapper

import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.datasource.model.RepoModel
import br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model.RepoVO

fun List<RepoModel>.toViewObject() = run {
    this.map {
        RepoVO(
            repoName = it.name,
            stars = it.stars,
            forks = it.forks,
            photo = it.owner.avatarUrl,
            authorsName = it.owner.login
        )
    }
}