package br.com.brunocasado.githubsmoststarredrepositoriesinkotlin.domain.model

data class RepoVO(
    val repoName: String,
    val stars: Int,
    val forks: Int,
    val photo: String,
    val authorsName: String
)
